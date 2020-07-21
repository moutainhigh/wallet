package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EmailUtil;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletOrderCriteria;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

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
	 * @param orderDate 订单日期
	 */
	public void failedSettleOrderSendMail(Date orderDate) {
		String formatDate = DateUtil.formatDate(orderDate, DateUtil.STANDARD_DTAETIME_PATTERN);
		log.info("[发送结算失败订单邮件通知] 订单时间[{}] 开始", formatDate);

		//获取orderDate当天和之前结算不成功的订单
		WalletOrderCriteria walletOrderCriteria = new WalletOrderCriteria();
		walletOrderCriteria.createCriteria()
				.andCreateTimeLessThan(orderDate == null ? DateUtil.addDate2(new Date(), -2) : orderDate)
				.andTypeEqualTo(EnumDef.OrderType.FINANCE.getValue())
				.andStatusEqualTo(EnumDef.OrderStatus.WAITTING.getValue());
		List<WalletOrder> walletOrderList = walletOrderExtDao.selectByExample(walletOrderCriteria);
		if (CollectionUtils.isEmpty(walletOrderList)) {
			log.info("[发送结算失败订单邮件通知] 订单时间[{}]: 订单列表为空", formatDate);
			return;
		}
		//拼接邮件内容
		StringBuilder sb = new StringBuilder(128);
		sb.append("申请单号").append(" ").append("单据状态").append("\n");
		walletOrderList.forEach(walletOrder -> sb.append(walletOrder.getOrderNo())
				.append(" ")
				.append(walletOrder.getStatus())
				.append("\n"));
		//发送通知邮件
		sendNotifyMail(sb.toString());
		log.info("[发送结算失败订单邮件通知] 订单时间[{}] 成功", formatDate);
	}

	/**
	 * 发送邮件
	 *
	 * @param content 邮件内容：转账单的申请单号  单据状态
	 */
	private void sendNotifyMail(String content) {
		// 发送通知邮件
		try {
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
		} catch (Exception e) {
			log.info("[发送结算失败订单邮件通知] 失败");
			log.error("[结算失败订单]通知邮件发送失败, " + content, e);
		}

	}
}
