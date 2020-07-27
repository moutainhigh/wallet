package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EmailUtil;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

	/**
	 * 结算不成功的订单发送邮件通知
	 *
	 * @param orderStartDate 订单开始时间
	 * @param orderEndDate   订单结束时间
	 */
	public void failedSettleOrderSendMail(Date orderStartDate, Date orderEndDate) {
		String startDateStr = DateUtil.formatDate(
				orderStartDate == null ? DateUtils.addDays(new Date(), -7) : orderStartDate,
				DateUtil.STANDARD_DTAETIME_PATTERN);

		String endDateStr = DateUtil.formatDate(orderEndDate == null ? DateUtils.addDays(new Date(), -2) :
						orderEndDate,
				DateUtil.STANDARD_DTAETIME_PATTERN);

		log.info("[发送结算失败订单邮件通知] 订单时间[{}###{}] 开始", startDateStr, endDateStr);
		//获取orderDate当天和之前结算不成功的订单
		List<WalletOrder> walletOrderList = walletOrderExtDao.selectSattleFailedOrder(startDateStr, endDateStr);
		if (CollectionUtils.isEmpty(walletOrderList)) {
			log.info("[发送结算失败订单邮件通知] 订单时间[{}###{}]: 订单列表为空", startDateStr, endDateStr);
			return;
		}

		//拼接邮件内容
		StringBuilder sb = new StringBuilder(128);

		sb.append("<table align=\"left\"><tr><th style=\"padding: 0 40px\" align=\"left\">申请单号</th>")
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
		EmailUtil.send(emailBody, () -> javaMailSender.createMimeMessage(), (m) -> javaMailSender.send(m));

	}

	/**
	 * 转换状态值
	 *
	 * @param value
	 * @return
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

}
