package com.rfchina.wallet.server.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.biztools.functional.MaxIdIterator;
import com.rfchina.platform.biztool.excel.ExcelBean;
import com.rfchina.platform.biztool.excel.ExcelFactory;
import com.rfchina.platform.biztools.fileserver.EnumFileAcl;
import com.rfchina.platform.biztools.fileserver.FileServer;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EmailUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.DownloadStatus;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.ReportDownloadVo;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.model.ext.WalletOrderExcelVo;
import com.rfchina.wallet.server.msic.RedisConstant;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author liujiecong
 * @date 2020/7/15 15:20
 * @description 订单业务处理
 */
@Service
@Slf4j
public class WalletOrderService {

	@Autowired
	private WalletOrderExtDao walletOrderExtDao;
	@Autowired
	private ConfigService configService;
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private FileServer fileServer;

	@Autowired
	private RedisTemplate redisTemplate;


	/**
	 * 结算不成功的订单发送邮件通知
	 *
	 * @param orderStartDate 订单开始时间
	 * @param orderEndDate 订单结束时间
	 */
	public void failedSettleOrderSendMail(Date orderStartDate, Date orderEndDate) {
		String startDateStr = DateUtil.formatDate(
			orderStartDate == null ? DateUtils.addDays(new Date(), -7) : orderStartDate,
			DateUtil.STANDARD_DTAETIME_PATTERN);

		String endDateStr = DateUtil
			.formatDate(orderEndDate == null ? DateUtils.addDays(new Date(), -2) :
					orderEndDate,
				DateUtil.STANDARD_DTAETIME_PATTERN);

		log.info("[发送结算失败订单邮件通知] 订单时间[{}###{}] 开始", startDateStr, endDateStr);
		//获取orderDate当天和之前结算不成功的订单
		List<WalletOrder> walletOrderList = walletOrderExtDao
			.selectSattleFailedOrder(startDateStr, endDateStr);
		if (CollectionUtils.isEmpty(walletOrderList)) {
			log.info("[发送结算失败订单邮件通知] 订单时间[{}###{}]: 订单列表为空", startDateStr, endDateStr);
			return;
		}

		//拼接邮件内容
		StringBuilder sb = new StringBuilder(128);

		sb.append(
			"<table align=\"left\"><tr><th style=\"padding: 0 40px\" align=\"left\">申请单号</th>")
			.append("<th style=\"padding: 0 40px\" align=\"left\">单据状态</th></tr>");

		walletOrderList.forEach(walletOrder -> sb.append("<tr><td style=\"padding: 0 40px\">")
			.append(walletOrder.getOrderNo())
			.append("</td>")
			.append("<td style=\"padding: 0 40px\">")
			.append(transOrderStatusName(walletOrder.getStatus()))
			.append("</td>")
			.append("</tr>"));
		sb.append("</table>");

		//发送通知邮件
		try {
			sendNotifyMail(sb.toString());
		} catch (Exception e) {
			log.info("[发送结算失败订单邮件通知] 失败");
			log.error("[结算失败订单]通知邮件发送失败, " + sb.toString(), e);
			return;
		}
		//更新数据库
		walletOrderExtDao.batchUpdateNotifiedByIds(
			walletOrderList.stream().map(WalletOrder::getId).collect(Collectors.toList()));

		log.info("[发送结算失败订单邮件通知] 订单时间[{}###{}] 成功", startDateStr, endDateStr);
	}

	/**
	 * 发送邮件
	 *
	 * @param content 邮件内容：转账单的申请单号  单据状态
	 */
	private void sendNotifyMail(String content) throws Exception {
		// 发送通知邮件
		EmailUtil.EmailBody emailBody = new EmailUtil.EmailBody("【请关注】存在转账单据状态不是结算成功", content,
			configService.getEmailSender(), configService.getEmailSender());
		String notifyContract = configService.getNotifyContract();
		if (StringUtils.isBlank(notifyContract)) {
			return;
		}
		for (String email : notifyContract.split(",")) {
			emailBody.addReceiver(email, email);
		}
		EmailUtil.send(emailBody, () -> javaMailSender.createMimeMessage(),
			(m) -> javaMailSender.send(m));

	}

	/**
	 * 转换状态值
	 */
	private String transOrderStatusName(Byte value) {
		String name = StringUtils.EMPTY;
		for (EnumDef.OrderStatus orderStatus : EnumDef.OrderStatus.values()) {
			if (orderStatus.getValue().equals(value)) {
				return orderStatus.getValueName();
			}
		}
		log.error("枚举找不到状态:{}", value);
		return name;
	}

	@Async
	public void exportOrderDetail(String uniqueCode, String fileName, Long walletId,
		Byte tradeType, Byte status, String beginTime, String endTime) {

		final Date beginTime2 = DateUtil.parse(beginTime, DateUtil.STANDARD_DTAE_PATTERN);
		final Date endTime2 = DateUtil
			.addDate2(DateUtil.parse(endTime, DateUtil.STANDARD_DTAE_PATTERN), 1);
		String threadName = Thread.currentThread().getName();
		log.info("线程[{}]正在导出钱包[{}]订单明细报表[{}]", threadName, walletId, fileName);

		ExcelBean excelBean = ExcelFactory.build2007();
		Sheet sheet = excelBean.creatSheet(fileName);
		excelBean.addTitle(sheet, 0, StatChargingDetailVo.class);

		AtomicInteger cursor = new AtomicInteger(1);
		new MaxIdIterator<WalletOrderExcelVo>().apply((maxId) -> {

			List<WalletOrder> data = walletOrderExtDao.selectByMaxId(maxId, walletId,
				Arrays.asList(tradeType), Arrays.asList(status), beginTime2, endTime2);

			log.info("读取{}maxId{}", data.size(), maxId);
			return data.stream()
				.map(item -> {
					WalletOrderExcelVo vo = BeanUtil.newInstance(item, WalletOrderExcelVo.class);
					vo.setOwnerId(walletId);
					return vo;
				}).collect(Collectors.toList());
		}, (row) -> {
			excelBean.addData(sheet, cursor.getAndIncrement(), Arrays.asList(row));
			return row.getId();
		});

		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			excelBean.getWorkbook().write(byteOut);

			String fileKey = String.format("report/temp/%s/%s", walletId, fileName);
			fileServer
				.upload(fileKey, byteOut.toByteArray(), "application/octet-stream",
					EnumFileAcl.PUBLIC_READ, null);
			BoundHashOperations hashOps = redisTemplate
				.boundHashOps(RedisConstant.PREX_WALLET_REPORT_DOWNLOAD);
			if (hashOps.hasKey(uniqueCode)) {
				String val = (String) hashOps.get(uniqueCode);
				ReportDownloadVo downloadVo = JsonUtil
					.toObject(val, ReportDownloadVo.class, getObjectMapper());
				downloadVo.setStatus(DownloadStatus.BUILDED.getValue());
				downloadVo.setLocation(getFileSrvPrefix() + fileKey);
				hashOps.put(uniqueCode, JsonUtil.toJSON(downloadVo, getObjectMapper()));
			}
		} catch (Exception e) {
			log.error("", e);
		}

		log.info("线程[{}]完成导出钱包[{}]订单明细报表[{}]", threadName, walletId, fileName);
	}

	private ObjectSetter<ObjectMapper> getObjectMapper() {
		return objectMapper -> {
			objectMapper.setTimeZone(TimeZone.getDefault());
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		};
	}

	private String getFileSrvPrefix() {

		String prefix = (fileServer.getSvrEndpoint().endsWith(SymbolConstant.SYMBOL_SLASH) ?
			fileServer.getSvrEndpoint()
			: fileServer.getSvrEndpoint() + SymbolConstant.SYMBOL_SLASH);
		return prefix + "_f" + SymbolConstant.SYMBOL_SLASH + fileServer.getSrvBucket()
			+ SymbolConstant.SYMBOL_SLASH;
	}
}
