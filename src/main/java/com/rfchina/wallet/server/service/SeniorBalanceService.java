package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.biztool.mapper.string.StringObject;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.BalanceResultMapper;
import com.rfchina.wallet.domain.model.BalanceResult;
import com.rfchina.wallet.domain.model.BalanceTunnelDetail;
import com.rfchina.wallet.domain.model.BalanceTunnelDetailCriteria;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletOrderCriteria;
import com.rfchina.wallet.server.bank.yunst.response.CheckAccount;
import com.rfchina.wallet.server.mapper.ext.BalanceResultExtDao;
import com.rfchina.wallet.server.mapper.ext.BalanceTunnelDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.BalanceVo;
import com.rfchina.wallet.server.model.ext.WalletOrderVo;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceStatus;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 对账
	 */
	public void balance(Date date) {

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

		// 完全匹配
		saveResult(succSet, date, BalanceStatus.SUCC);
		// 通道多
		Set<BalanceVo> tunnelMoreSet = new HashSet<>(tunnelDiffSet);
		tunnelMoreSet.removeAll(walletDiffSet);
		saveResult(tunnelMoreSet, date, BalanceStatus.TUNNEL_MORE);
		// 钱包多
		Set<BalanceVo> walletMoreSet = new HashSet<>(walletDiffSet);
		walletMoreSet.removeAll(tunnelDiffSet);
		saveResult(walletMoreSet, date, BalanceStatus.WALLET_MORE);
		// 金额不匹配
		Set<BalanceVo> diffSet = new HashSet<>(walletDiffSet);
		diffSet.removeAll(walletMoreSet);
		saveResult(diffSet, date, BalanceStatus.AMOUNT_NOT_MATCH);

		if (tunnelMoreSet.size() > 0) {

		} else if (walletMoreSet.size() > 0) {

		} else if (diffSet.size() > 0) {

		} else {
			write2File(date, succSet);
		}


	}

	private void write2File(Date date, Set<BalanceVo> succSet) {
		// 对账成功的数据生成文件
		String balanceFile = configService.getStorageDir() + "/result/" + DateUtil
			.formatDate(date, DateUtil.STANDARD_DTAE_PATTERN);
		try (BufferedWriter writer = Files
			.newBufferedWriter(Paths.get(balanceFile), Charset.forName("UTF-8"),
				StandardOpenOption.TRUNCATE_EXISTING)) {

			List<String> orderNos = succSet.stream()
				.map(order -> order.getOrderNo())
				.collect(Collectors.toList());
			for (int offset = 0, limit = 1; offset <= orderNos.size(); offset += limit) {
				String orderNo = orderNos.get(offset);
				WalletOrder order = walletOrderDao.selectByOrderNo(orderNo);
				WalletOrderVo orderVo = BeanUtil.newInstance(order, WalletOrderVo.class);
				String line = StringObject.toObjectString(orderVo, WalletOrderVo.class, "|");
				writer.write(line);
			}
		} catch (Exception e) {
			log.error("写入对账文件错误", e);
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
			try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileUrl))) {

				String line = reader.readLine();
				for (line = reader.readLine(); line != null; line = reader.readLine()) {
					CheckAccount check = StringObject
						.parseStringObject(line, CheckAccount.class, "\\" + SPLIT_TAG);
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
		WalletOrderCriteria example = new WalletOrderCriteria();
		example.createCriteria()
			.andStatusEqualTo(OrderStatus.SUCC.getValue())
			.andEndTimeBetween(beginDate, endDate);
		BoundSetOperations<String, String> walletOps = local2Redis(walletKey,
			() -> walletOrderDao.countByExample(example),
			(offset, limit) -> {
				List<WalletOrder> orders = walletOrderDao
					.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
				return orders.stream()
					.map(order -> order.getOrderNo() + SPLIT_TAG + order.getAmount())
					.collect(Collectors.toList());
			});

		log.info("Redis加载数据 tunnel = {}, wallet = {}", tunnelOps.size(), walletOps.size());
		return new Tuple(tunnelOps, walletOps);
	}

	private void saveResult(Set<BalanceVo> resultSet, Date date, BalanceStatus status) {
		// 保存结果
		resultSet.forEach(value -> {
			if (value != null) {
				BalanceResult result = BalanceResult.builder()
					.balanceDate(date)
					.orderNo(value.getOrderNo())
					.balanceStatus(status.getValue())
					.build();
				balanceResultDao.insertSelective(result);
			}
		});
	}

}
