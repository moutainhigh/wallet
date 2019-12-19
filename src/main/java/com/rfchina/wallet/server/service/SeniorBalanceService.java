package com.rfchina.wallet.server.service;

import com.rfchina.platform.biztool.mapper.string.StringObject;
import com.rfchina.platform.biztools.fileserver.EnumFileAcl;
import com.rfchina.platform.biztools.fileserver.FileServer;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EmailUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BalanceJob;
import com.rfchina.wallet.domain.model.BalanceJobCriteria;
import com.rfchina.wallet.domain.model.BalanceResult;
import com.rfchina.wallet.domain.model.BalanceTunnelDetail;
import com.rfchina.wallet.domain.model.BalanceTunnelDetailCriteria;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletOrderCriteria;
import com.rfchina.wallet.server.bank.yunst.response.CheckAccount;
import com.rfchina.wallet.server.mapper.ext.BalanceJobExtDao;
import com.rfchina.wallet.server.mapper.ext.BalanceResultExtDao;
import com.rfchina.wallet.server.mapper.ext.BalanceTunnelDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.BalanceVo;
import com.rfchina.wallet.server.model.ext.WalletOrderVo;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceJobStatus;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceResultStatus;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeniorBalanceService {


	public static final String SPLIT_TAG = "|";
	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private BalanceTunnelDetailExtDao balanceTunnelDetailDao;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private BalanceResultExtDao balanceResultDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private BalanceJobExtDao balanceJobDao;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private FileServer fileServer;

	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * 对账
	 */
	public void doBalance(Date date) {
		String statDate = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
			// 下载数据
			EBankHandler handler = handlerHelper.selectByTunnelType(TunnelType.YUNST.getValue());
			String fileUrl = handler.downloadBalanceFile(date);

			Date beginDate = DateUtil.getDate2(date);
			Date endDate = DateUtil.getDate(date);
			// 保存数据
			saveLocal(fileUrl, beginDate, endDate);
			// 加载数据
			String keyPrefix =
				"wallet:balance:" + DateUtil.formatDate(date, DateUtil.STANDARD_DTAE_PATTERN);
			String tunnelKey = keyPrefix + ":tunnelDetails";
			String walletKey = keyPrefix + ":walletOrders";
			Tuple<BoundSetOperations<String, String>, BoundSetOperations<String, String>> tuple = loadData(
				tunnelKey, walletKey, beginDate, endDate);
			BoundSetOperations<String, String> tunnelOps = tuple.left;
			BoundSetOperations<String, String> walletOps = tuple.right;
			// 集合比较
			String tunnelDiffKey = keyPrefix + ":tunnelDiff";
			String walletDiffKey = keyPrefix + ":walletDiff";
			String succDiffKey = keyPrefix + ":succ";
			Set<BalanceVo> tunnelDiffSet = diffSet(tunnelOps, walletKey, tunnelDiffKey);
			Set<BalanceVo> walletDiffSet = diffSet(walletOps, tunnelKey, walletDiffKey);
			Set<BalanceVo> succSet = diffSet(tunnelOps, tunnelDiffKey, succDiffKey);

			// 创建JOB
			BalanceJob job = createJob(beginDate, endDate);
			// 完全匹配
			saveResult(succSet, beginDate, endDate, job.getId(), BalanceResultStatus.SUCC);
			// 通道条目多
			Set<BalanceVo> tunnelMoreSet = new HashSet<>(tunnelDiffSet);
			tunnelMoreSet.removeAll(walletDiffSet);
			saveResult(tunnelMoreSet, beginDate, endDate, job.getId(),
				BalanceResultStatus.TUNNEL_MORE);
			// 钱包条目多
			Set<BalanceVo> walletMoreSet = new HashSet<>(walletDiffSet);
			walletMoreSet.removeAll(tunnelDiffSet);
			saveResult(walletMoreSet, beginDate, endDate, job.getId(),
				BalanceResultStatus.WALLET_MORE);
			// 金额不匹配
			Set<BalanceVo> diffSet = new HashSet<>(walletDiffSet);
			diffSet.removeAll(walletMoreSet);
			saveResult(diffSet, beginDate, endDate, job.getId(),
				BalanceResultStatus.AMOUNT_NOT_MATCH);

			// 对账失败时结束
			if (!tunnelMoreSet.isEmpty() || !walletMoreSet.isEmpty() || !diffSet.isEmpty()) {
				job.setStatus(BalanceJobStatus.FAIL.getValue());
				balanceJobDao.updateByPrimaryKeySelective(job);
				// 邮件通知对账错误
				sendFailMail(statDate, tunnelMoreSet, walletMoreSet, diffSet);
			} else {
				// 生成对账文件
				try {
					Path path = write2File(date, succSet);
					byte[] bytes = Files.readAllBytes(path);
					String fileKey = path.getFileName().toString();
					fileServer.upload(fileKey, bytes, "text/plain", EnumFileAcl.PUBLIC_READ, null);
					Files.delete(path);
					job.setStatus(BalanceJobStatus.SUCC.getValue());
					job.setWalletFileUrl(fileServer.getSvrEndpoint() + "/_f/" +
						fileServer.getSrvBucket() + "/" + fileKey);
					balanceJobDao.updateByPrimaryKeySelective(job);
				} catch (Exception e) {
					log.error("上传对账文件异常", e);
				}
			}
		} catch (Exception e) {
			log.error("对账失败 " + statDate, e);
			sendErrMail(statDate, e.toString());
		}

	}

	private void sendErrMail(String statDate, String errMsg) {
		// 发送通知邮件
		try {
			EmailUtil.EmailBody emailBody = new EmailUtil.EmailBody(
				"**********[钱包服务]对账异常通知 " + statDate, errMsg, configService.getEmailSender(),
				configService.getEmailSender());
			String errorContract = configService.getNotifyContract();
			if (StringUtils.isNotBlank(errorContract)) {
				for (String email : errorContract.split(",")) {
					emailBody.addReceiver(email, email);
				}
				EmailUtil.send(emailBody, () -> javaMailSender.createMimeMessage(),
					(m) -> javaMailSender.send(m));
			}
		} catch (Exception e) {
			log.error("[钱包服务]通知邮件发送失败, " + errMsg, e);
		}

	}

	private void sendFailMail(String statDate, Set<BalanceVo> tunnelMoreSet,
		Set<BalanceVo> walletMoreSet, Set<BalanceVo> diffSet) {
		// 邮件通知
		StringBuilder content = new StringBuilder();
		content.append("通道条目多&nbsp&nbsp")
			.append(tunnelMoreSet.stream().map(v -> v.toString()).collect(Collectors.joining(",")))
			.append("</br>")
			.append("钱包条目多&nbsp&nbsp")
			.append(walletMoreSet.stream().map(v -> v.toString()).collect(Collectors.joining(",")))
			.append("</br>")
			.append("金额不匹配&nbsp&nbsp")
			.append(diffSet.stream().map(v -> v.toString()).collect(Collectors.joining(",")))
			.append("</br>");

		// 发送通知邮件
		try {
			EmailUtil.EmailBody emailBody = new EmailUtil.EmailBody(
				"**********[钱包服务]对账异常通知 " + statDate,
				content.toString(),
				configService.getEmailSender(),
				configService.getEmailSender()
			);
			String errorContract = configService.getNotifyContract();
			if (StringUtils.isNotBlank(errorContract)) {
				for (String email : errorContract.split(",")) {
					emailBody.addReceiver(email, email);
				}
				EmailUtil.send(emailBody, () -> javaMailSender.createMimeMessage(),
					(m) -> javaMailSender.send(m));
			}

		} catch (Exception e) {
			log.error("[钱包服务]通知邮件发送失败, " + content, e);
		}
	}

	private BalanceJob createJob(Date beginDate, Date endDate) {
		balanceJobDao.deleteByDate(beginDate, endDate);
		BalanceJob job = BalanceJob.builder()
			.balanceDate(beginDate)
			.status(BalanceJobStatus.RUNNING.getValue())
			.deleted((byte) 0)
			.createTime(new Date())
			.build();
		balanceJobDao.insertSelective(job);
		return job;
	}

	private Path write2File(Date date, Set<BalanceVo> succSet) {
		String balanceFile = configService.getStorageDir() + "/result/" + DateUtil
			.formatDate(date, DateUtil.STANDARD_DTAE_PATTERN) + ".csv";
		Path path = Paths.get(balanceFile);
		try (BufferedWriter writer = Files
			.newBufferedWriter(path, Charset.forName("UTF-8"),
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

			String end = SPLIT_TAG + "\n";
			writer.write(StringObject.toTitle(WalletOrderVo.class, SPLIT_TAG) + end);

			List<String> orderNos = succSet.stream()
				.map(order -> order.getOrderNo())
				.collect(Collectors.toList());
			for (int offset = 0, limit = 1; offset < orderNos.size(); offset += limit) {
				String orderNo = orderNos.get(offset);
				WalletOrder order = walletOrderDao.selectByOrderNo(orderNo);
				WalletOrderVo orderVo = BeanUtil.newInstance(order, WalletOrderVo.class);
				String line = StringObject.toObjectString(orderVo, WalletOrderVo.class, SPLIT_TAG);
				writer.write(line + end);
			}
			return path;
		} catch (Exception e) {
			log.error("写入对账文件错误", e);
			throw new WalletResponseException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}

	private Set<BalanceVo> diffSet(BoundSetOperations<String, String> ops, String walletKey,
		String tunnelDiffKey) {
		redisTemplate.delete(tunnelDiffKey);
		ops.diffAndStore(walletKey, tunnelDiffKey);
		return redisTemplate.boundSetOps(tunnelDiffKey)
			.members().stream()
			.map(value -> (BalanceVo) StringObject
				.parseStringObject(value, BalanceVo.class, "\\" + SPLIT_TAG))
			.collect(Collectors.toSet());
	}


	private void saveLocal(String fileUrl, Date beginDate, Date endDate) {
		// 删除历史数据
		balanceTunnelDetailDao.deleteByBalanceDate(beginDate, endDate);
		// 解析保存数据
		try {
			long count = 0;
			String prefix = configService.getOrderNoPrefix();
			try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileUrl))) {

				String line = reader.readLine();
				for (line = reader.readLine(); line != null; line = reader.readLine()) {
					CheckAccount check = StringObject
						.parseStringObject(line, CheckAccount.class, "\\" + SPLIT_TAG);
					if (StringUtil.isNotBlank(prefix) && !check.getOrderNo().startsWith(prefix)) {
						continue;
					}
					BalanceTunnelDetail detail = BeanUtil
						.newInstance(check, BalanceTunnelDetail.class);
					detail.setTunnelType(TunnelType.YUNST.getValue());
					detail.setWalletBalanceDate(beginDate);
					detail.setCreateTime(new Date());
					balanceTunnelDetailDao.insertSelective(detail);
					count++;
				}
			}
			log.info("下载数据结果 {},{}", fileUrl, count);
		} catch (Exception e) {
			log.error("【通联】更新对账单异常", e);
		}
	}

	private BoundSetOperations<String, String> local2Redis(String key, Supplier<Long> totalSupplier,
		BiFunction<Integer, Integer, List<String>> valueSupplier) {

		redisTemplate.delete(key);
		BoundSetOperations<String, String> tunnelOps = redisTemplate.boundSetOps(key);

		Long total = totalSupplier.get();
		// 加载通单订单
		for (int offset = 0, limit = 300; offset <= total; offset += limit) {
			valueSupplier.apply(offset, limit)
				.forEach(value -> tunnelOps.add(value));
		}
		tunnelOps.expire(7, TimeUnit.DAYS);

		return tunnelOps;

	}

	private Tuple<BoundSetOperations<String, String>, BoundSetOperations<String, String>> loadData(
		String tunnelKey, String walletKey, Date beginDate, Date endDate) {
		// 加载通道明细数据
		BalanceTunnelDetailCriteria tunnelExample = new BalanceTunnelDetailCriteria();
		tunnelExample.createCriteria()
			.andDeletedEqualTo((byte) 0)
			.andWalletBalanceDateBetween(beginDate, endDate);
		tunnelExample.setOrderByClause("id asc");
		BoundSetOperations<String, String> tunnelOps = local2Redis(tunnelKey,
			() -> balanceTunnelDetailDao.countByExample(tunnelExample),
			(offset, limit) -> {
				List<BalanceTunnelDetail> list = balanceTunnelDetailDao
					.selectByExampleWithRowbounds(tunnelExample, new RowBounds(offset, limit));
				return list.stream()
					.map(v -> v.getOrderNo() + SPLIT_TAG + v.getTotalAmount())
					.collect(Collectors.toList());
			});
		// 加载钱包数据
		WalletOrderCriteria OrderExample = new WalletOrderCriteria();
		OrderExample.createCriteria()
			.andStatusEqualTo(OrderStatus.SUCC.getValue())
			.andEndTimeBetween(beginDate, endDate);
		OrderExample.setOrderByClause("id asc");
		BoundSetOperations<String, String> walletOps = local2Redis(walletKey,
			() -> walletOrderDao.countByExample(OrderExample),
			(offset, limit) -> {
				List<WalletOrder> orders = walletOrderDao
					.selectByExampleWithRowbounds(OrderExample, new RowBounds(offset, limit));
				return orders.stream()
					.map(order -> order.getOrderNo() + SPLIT_TAG + order.getAmount())
					.collect(Collectors.toList());
			});

		log.info("Redis加载数据 tunnel = {}, wallet = {}", tunnelOps.size(), walletOps.size());
		return new Tuple(tunnelOps, walletOps);
	}

	private void saveResult(Set<BalanceVo> resultSet, Date beginDate, Date endDate, Long jobId,
		BalanceResultStatus status) {
		balanceResultDao.deleteByDate(beginDate, endDate);
		// 保存结果
		resultSet.forEach(value -> {
			if (value != null) {
				BalanceResult result = BalanceResult.builder()
					.balanceDate(beginDate)
					.jobId(jobId)
					.orderNo(value.getOrderNo())
					.balanceStatus(status.getValue())
					.build();
				balanceResultDao.insertSelective(result);
			}
		});
	}

	public BalanceJob balanceFile(Date balanceDate) {
		BalanceJobCriteria example = new BalanceJobCriteria();
		Date beginDate = DateUtil.getDate2(balanceDate);
		Date endDate = DateUtil.addDate2(beginDate, 1);
		example.createCriteria()
			.andStatusEqualTo(BalanceJobStatus.SUCC.getValue())
			.andDeletedEqualTo((byte) 0)
			.andBalanceDateGreaterThanOrEqualTo(beginDate)
			.andBalanceDateLessThan(endDate);
		example.setOrderByClause("id desc");
		List<BalanceJob> balanceJobs = balanceJobDao.selectByExample(example);
		if (!balanceJobs.isEmpty()) {
			return balanceJobs.get(0);
		}
		return null;
	}
}
