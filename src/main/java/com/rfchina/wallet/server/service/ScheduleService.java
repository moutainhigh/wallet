package com.rfchina.wallet.server.service;

import com.alibaba.fastjson.JSON;
import com.rfchina.biztools.functional.MaxIdIterator;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.misc.Triple;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.BankCodeDao;
import com.rfchina.wallet.domain.mapper.ext.WalletBalanceDetailDao;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletFinanceDao;
import com.rfchina.wallet.domain.misc.EnumDef.BizValidateType;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderSubStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletBalanceDetail;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletConfig;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletOrderCriteria;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.bank.pudong.domain.util.ExceptionUtil;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCompanyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletConfigExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletUserExtDao;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.model.ext.SLWalletMqMessage;
import com.rfchina.wallet.server.model.ext.SLWalletMqMessage.SLWalletMqMessageBuilder;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceFreezeMode;
import com.rfchina.wallet.server.msic.EnumWallet.CardPro;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.GwPayeeType;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumWallet.NotifyType;
import com.rfchina.wallet.server.msic.EnumWallet.WithdrawType;
import com.rfchina.wallet.server.msic.EnumWallet.YunstCompanyInfoAuditStatus;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import com.rfchina.wallet.server.service.handler.yunst.YunstNotifyHandler;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduleService {

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private WalletCompanyExtDao walletCompanyDao;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private WalletUserExtDao walletUserDao;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private AppService appService;

	@Autowired
	private BankCodeDao bankCodeDao;

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@Autowired
	private ConfigService configService;

	@Autowired
	private ExactErrPredicate exactErrPredicate;

	@Autowired
	private GatewayTransService gatewayTransService;

	@Autowired
	private SeniorWalletService seniorWalletService;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private WalletFinanceDao walletFinanceDao;

	@Autowired
	private JuniorPayService juniorPayService;

	@Autowired
	private SeniorPayService seniorPayService;

	@Autowired
	private WalletConfigExtDao walletConfigDao;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private WalletClearingExtDao walletClearingDao;

	@Autowired
	private WalletBalanceDetailDao walletBalanceDetailDao;

	@Autowired
	private WalletBalanceDetailService walletBalanceDetailService;

	@Autowired
	private YunstNotifyHandler yunstNotifyHandler;

	@Autowired
	private YunstUserHandler yunstUserHandler;


	/**
	 * 通道转账
	 */
	public void doTunnelFinance(List<Tuple<WalletOrder, WalletFinance>> tuples) {

		tuples.forEach(tuple -> {
			WalletOrder walletOrder = tuple.left;
			WalletFinance walletFinance = tuple.right;

			WalletCard walletCard = walletCardDao
				.selectDefCardByWalletId(walletOrder.getWalletId());
			if (walletCard == null) {
				log.warn("钱包[{}]没有绑定银行卡，跳过申请单[{}]", walletOrder.getWalletId(),
					walletOrder.getId());
				return;
			}
			// 更新收跨人信息
			fillCardInfo(walletFinance, walletCard);
		});
		// 请求网关
		try {
			EBankHandler handler = handlerHelper
				.selectByTunnelType(tuples.get(0).left.getTunnelType());
			Tuple<GatewayMethod, PayTuple> rs = handler.finance(tuples);
			// 记录结果
			GatewayMethod method = rs.left;
			PayTuple payInResp = rs.right;
			// 更新申请单
			tuples.forEach(tuple -> {
				WalletOrder walletOrder = tuple.left;
				WalletFinance walletFinance = tuple.right;

				walletOrder.setProgress(GwProgress.HAS_SEND.getValue());
				walletOrder.setStatus(OrderStatus.WAITTING.getValue());
				walletOrder.setStartTime(new Date());
				walletOrderDao.updateByPrimaryKeySelective(walletOrder);
				// 更新交易记录
				GatewayTrans gatewayTrans = gatewayTransService
					.selOrCrtTrans(walletOrder, walletFinance);
				gatewayTrans.setStage(payInResp.getStage());
				gatewayTrans.setAcceptNo(payInResp.getAcceptNo());
				gatewayTrans.setPacketId(payInResp.getPacketId());
				gatewayTrans.setElecChequeNo(
					payInResp.getElecMap().get(walletOrder.getId().toString()));
				gatewayTrans.setRefMethod(method.getValue());
				gatewayTrans.setLanchTime(new Date());
				gatewayTransService.updateTrans(gatewayTrans);
			});

		} catch (Exception e) {

			IGatewayError err = ExceptionUtil.explain(e);
			try {
				onFinanceError(tuples, err);
			} catch (Exception ex) {
				log.error("", ex);
			}
			log.error("银行网关支付错误", e);
		}
	}

	@PostMq(routingKey = MqConstant.WALLET_PAY_RESULT)
	public List<PayStatusResp> onFinanceError(List<Tuple<WalletOrder, WalletFinance>> tuples,
		IGatewayError err) {
		// 确切失败的单业务会重新发起新的转账，其他的单进入待处理状态
		boolean exactErr = exactErrPredicate.test(err);

		return tuples.stream().map(tuple -> {
			WalletOrder walletOrder = tuple.left;
			WalletFinance walletFinance = tuple.right;

			walletOrder.setTunnelErrCode(err.getErrCode());
			walletOrder.setTunnelErrMsg(err.getErrMsg());
			walletOrder.setUserErrMsg("发起交易异常");
			if (exactErr) {
				// 确切失败
				walletOrder.setStatus(OrderStatus.FAIL.getValue());
				walletOrder.setProgress(GwProgress.HAS_RESP.getValue());
			} else {
				// 人工处理
				walletOrder.setSubStatus(OrderSubStatus.WAIT_DEAL.getValue());
			}
			walletOrderDao.updateByPrimaryKeySelective(walletOrder);
			// 记录网关信息
			GatewayTrans gatewayTrans = gatewayTransService
				.selOrCrtTrans(walletOrder, walletFinance);
			gatewayTrans.setStage(err.getTransCode());
			gatewayTrans.setErrCode(err.getErrCode());
			gatewayTrans.setSysErrMsg(err.getErrMsg());
			gatewayTrans.setUserErrMsg("发起交易异常");
			gatewayTransService.updateTrans(gatewayTrans);

			return getPayStatusMQ(walletOrder, walletFinance, gatewayTrans);
		}).collect(Collectors.toList());
	}


	public PayStatusResp getPayStatusMQ(WalletOrder walletOrder, WalletFinance walletFinance,
		GatewayTrans gatewayTrans) {

		return PayStatusResp.builder()
			.bizNo(walletOrder.getBizNo())
			.batchNo(walletOrder.getBatchNo())
			.payeeAccount(walletFinance.getPayeeAccount())
			.payeeName(walletFinance.getPayeeName())
			.payeeType(walletFinance.getCardPro().byteValue() == CardPro.COMPANY.getValue()
				? GwPayeeType.COMPANY.getValue() : GwPayeeType.PERSON.getValue())
			.payeeBankCode(walletFinance.getPayeeBankCode())
			.payeeBankInfo(null)
			.elecChequeNo(gatewayTrans.getElecChequeNo())
			.note(walletOrder.getNote())
			.remark(walletOrder.getRemark())
			.amount(walletOrder.getAmount())
			.status(walletOrder.getStatus())
			.errCode(walletOrder.getTunnelErrCode())
			.userErrMsg(walletOrder.getUserErrMsg())
			.sysErrMsg(walletOrder.getTunnelErrMsg())
			.createTime(walletOrder.getCreateTime())
			.lanchTime(walletOrder.getStartTime())
			.bizTime(gatewayTrans.getBizTime())
			.endTime(walletOrder.getEndTime())
			.transDate(DateUtil.formatDate(walletOrder.getCreateTime()))
			.walletId(walletOrder.getWalletId())
			.build();
	}

	/**
	 * 发起通道支付
	 */
	public void doTunnelFinanceJob(String batchNo) {
		List<WalletOrder> walletOrders = walletOrderDao.selectByBatchNo(batchNo);

		List<Tuple<WalletOrder, WalletFinance>> tuples = walletOrders.stream().map(walletOrder -> {
			Optional<WalletOrder> opt = Optional.ofNullable(walletOrder)
				.filter(o ->
					o.getProgress().byteValue() == GwProgress.WAIT_SEND.getValue().byteValue()
						&& o.getStatus().byteValue() == OrderStatus.WAITTING.getValue()
						&& o.getType().byteValue() == OrderType.FINANCE.getValue());
			if (!opt.isPresent()) {
				log.error("订单进度或类型不正确 {}", walletOrder);
				return null;
			}
			WalletFinance walletFinance = walletFinanceDao.selectByOrderId(walletOrder.getId());
			return new Tuple<>(walletOrder, walletFinance);
		}).filter(t -> t != null)
			.collect(Collectors.toList());

		doTunnelFinance(tuples);
	}

	/**
	 * 卡信息填入钱包申请
	 */
	private void fillCardInfo(WalletFinance walletFinance, WalletCard walletCard) {
		// 卡信息填入钱包申请
		walletFinance.setPayerAccount(configService.getAcctNo());
		walletFinance.setPayeeAccount(walletCard.getBankAccount());
		walletFinance.setPayeeName(walletCard.getDepositName());
		walletFinance.setCardPro(walletCard.getIsPublic().byteValue() == 1 ?
			CardPro.COMPANY.getValue() : CardPro.PERSON.getValue());
		walletFinance.setPayeeBankCode(walletCard.getBankCode());
		walletFinanceDao.updateByPrimaryKeySelective(walletFinance);
	}

	/**
	 * 定时更新支付状态
	 */

	public void quartzUpdateJunior(Integer batchSize) {

		log.info("quartz: 开始更新支付状态[银企直连]");

		List<String> batchNos = walletOrderDao
			.selectUnFinishBatchNo(OrderType.FINANCE.getValue(), batchSize);

		for (String batchNo : batchNos) {
			try {
				List<Triple<WalletOrder, WalletFinance, GatewayTrans>> triples = juniorPayService
					.updateOrderStatus(batchNo);
				List<PayStatusResp> resps = triples.stream()
					.map(triple -> getPayStatusMQ(triple.x, triple.y, triple.z))
					.collect(Collectors.toList());
				juniorPayService.sendMQ(resps);
			} catch (Exception e) {
				log.error("", e);
			}
		}
		log.info("quartz: 结束更新支付状态[银企直连]");
	}

	/**
	 * 定时更新支付状态
	 */
	public void quartzUpdateSenior(Integer batchSize) {

		log.info("quartz: 开始更新支付状态[高级钱包]");

		List<Byte> types = Stream.of(OrderType.values())
			.filter(t -> t.getValue().byteValue() != OrderType.FINANCE.getValue().byteValue())
			.map(t -> t.getValue())
			.collect(Collectors.toList());

		List<WalletOrder> walletOrders = new ArrayList<>();
		long maxId = 0;
		do {

			WalletOrderCriteria example = new WalletOrderCriteria();
			example.setOrderByClause("id asc");
			example.createCriteria()
				.andProgressEqualTo(GwProgress.HAS_SEND.getValue())
				.andStatusEqualTo(OrderStatus.WAITTING.getValue())
				.andTypeIn(types)
				.andIdGreaterThan(maxId);
			walletOrders = walletOrderDao
				.selectByExampleWithRowbounds(example, new RowBounds(0, batchSize));
			for (WalletOrder walletOrder : walletOrders) {
				try {
					log.info("开始更新高级订单 [{}]", walletOrder.getOrderNo());
					seniorPayService.updateOrderStatusWithMq(walletOrder.getOrderNo(), true);
				} catch (Exception e) {
					log.error("定时更新支付状态, 异常！", e);
				}

				maxId = walletOrder.getId() > maxId ? walletOrder.getId() : maxId;
			}

		} while (!walletOrders.isEmpty());
		log.info("quartz: 结束更新支付状态[高级钱包]");
	}

	/**
	 * 通知研发
	 */
	public void notifyDeveloper(List<WalletOrder> walletOrders) {

		if (walletOrders.isEmpty()) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append("<table border='1'>")
			.append("<tr>")
			.append("<td>").append("流水").append("</td>")
			.append("<td>").append("业务号").append("</td>")
			.append("<td>").append("金额").append("</td>")
			.append("<td>").append("错误原因").append("</td>")
			.append("<td>").append("下单时间").append("</td>")
			.append("<td>").append("备注").append("</td>")
			.append("</tr>");
		for (WalletOrder walletOrder : walletOrders) {
			BigDecimal amount = new BigDecimal(walletOrder.getAmount());
			amount = amount.divide(new BigDecimal("100"), 2, RoundingMode.DOWN);
			builder.append("<tr>")
				.append("<td>").append(walletOrder.getId()).append("</td>")
				.append("<td>").append(walletOrder.getBizNo()).append("</td>")
				.append("<td>").append(amount).append("</td>")
				.append("<td>").append(walletOrder.getTunnelErrMsg()).append("</td>")
				.append("<td>").append(DateUtil.formatDate(walletOrder.getCreateTime()))
				.append("</td>").append("<td>").append(JSON.toJSONString(walletOrder))
				.append("</td>")
				.append("</tr>");
		}
		builder.append("</table>");

		String title = String.format("*******[技术邮件][银企直连][%s]转账失败，等待人工处理", configService.getEnv());
		String msg = builder.toString();
		sendEmail(title, msg, configService.getNotifyDevEmail());

		List<Long> ids = walletOrders.stream()
			.map(walletOrder -> walletOrder.getId())
			.collect(Collectors.toList());
		if (!ids.isEmpty()) {
			walletOrderDao.updateNotified(ids, NotifyType.DEVELOPER.getValue());
		}
	}

	/**
	 * 发送邮件
	 */
	public void sendEmail(String title, String msg, String emails) {
		if (StringUtil.isBlank(emails)) {
			return;
		}
		String[] receives = emails.split(",");
		if (receives == null || receives.length == 0) {
			return;
		}
		log.info("发送邮件给 [{}] , 内容 {}", emails, msg);
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(configService.getEmailSender());
			helper.setTo(receives);
			helper.setSubject(title);
			helper.setText(msg, true);
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("邮件发送失败", e);
		}
	}


	/**
	 * 发起自动提现
	 */
	public void doWithdraw(WalletConfig walletConfig) {

		WalletTunnel walletTunnel = walletTunnelDao
			.selectByWalletId(walletConfig.getWalletId(), TunnelType.YUNST.getValue());
		seniorPayService.syncTunnelAmount(walletTunnel);
		// 钱包
		if (walletTunnel.getBalance() < walletConfig.getAutoWithdrawThreshold()) {
			log.info("[自动提现] 钱包[{}] 未达到自动限额", walletConfig.getWalletId());
			return;
		}
		log.info("[自动提现] 钱包[{}] 开始自动提现", walletConfig.getWalletId());
		// 支付单退款
		WalletCard walletCard = walletCardDao.selectDefCardByWalletId(walletConfig.getWalletId());
		Optional.ofNullable(walletCard)
			.orElseThrow(() -> {
				log.error("[自动提现] 钱包[{}] 未设置默认卡", walletConfig.getWalletId());
				return new WalletResponseException(EnumWalletResponseCode.BANK_CARD_NOT_EXISTS);
			});

		if (WithdrawType.COLLECT_ORDER.getValue().byteValue() == walletConfig
			.getAutoWithdrawType()) {
			new MaxIdIterator<WalletBalanceDetail>().apply(
				maxId -> walletBalanceDetailDao.selectUnWithdraw(walletConfig.getWalletId(), maxId),
				payDetail -> {
					log.info("[自动提现] 发起按单提现  入金单号 {} ", payDetail.getOrderNo());
					WithdrawResp order = seniorPayService.doWithdraw(walletConfig.getWalletId(),
						walletCard, payDetail.getBalance(), BizValidateType.NONE.getValue(),
						null, null);
					WalletBalanceDetail withdrawDetail = walletBalanceDetailService
						.consumePayDetail(order, order.getWithdrawId(), payDetail,
							order.getAmount(), BalanceFreezeMode.FREEZEN);
					log.info("[自动提现] 发起按单提现  出金单号 {}", withdrawDetail.getOrderNo());
					return payDetail.getId();
				});
		} else if (WithdrawType.WALLET_AMOUNT.getValue().byteValue() == walletConfig
			.getAutoWithdrawType()) {

			seniorPayService
				.balanceWithdraw(walletConfig.getWalletId(), walletCard, walletTunnel.getBalance(),
					BizValidateType.NONE.getValue(), null, null, BalanceFreezeMode.FREEZEN, true);
		}
		log.info("[自动提现] 钱包[{}] 结束自动提现", walletConfig.getWalletId());

	}

	@PostMq(routingKey = MqConstant.WALLET_SENIOR_COMPANY_AUDIT)
	public SLWalletMqMessage syncCompanyTunnel(WalletTunnel walletTunnel) {
		try {

			CompanyInfoResult companyInfo = (CompanyInfoResult) yunstUserHandler
				.getMemberInfo(walletTunnel.getBizUserId());

			log.info("[通道同步]更新通道[{}]会员[{}]的状态[{}]", walletTunnel.getId(),
				walletTunnel.getBizUserId(), companyInfo.getStatus());
			// 审核中，不发MQ
			if (YunstCompanyInfoAuditStatus.WAITING.getValue().longValue()
				== companyInfo.getStatus().longValue()) {
				log.info("[通道同步]更新通道[{}]会员[{}]，终态未明确，不进行更新", walletTunnel.getId(),
					walletTunnel.getBizUserId());
				return null;
			}

			// 设置checkTime
			Date checkTime = DateUtil
				.parse(companyInfo.getCheckTime(), DateUtil.STANDARD_DTAETIME_PATTERN);
			walletTunnel.setCheckTime(checkTime);

			SLWalletMqMessageBuilder builder = SLWalletMqMessage.builder();
			// 终态
			if (YunstCompanyInfoAuditStatus.SUCCESS.getValue().longValue()
				== companyInfo.getStatus()) {

				log.info("[通道同步]更新通道[{}]会员[{}]，通道成功", walletTunnel.getId(),
					walletTunnel.getBizUserId());
				yunstNotifyHandler.handleCompanyAuditSucc(walletTunnel, companyInfo);
				builder.isPass(true);

			} else if (YunstCompanyInfoAuditStatus.FAIL.getValue().longValue()
				== companyInfo.getStatus()) {

				log.info("[通道同步]更新通道[{}]会员[{}]，通道失败", walletTunnel.getId(),
					walletTunnel.getBizUserId());
				yunstNotifyHandler.handleAuditFail(walletTunnel, companyInfo.getFailReason()
					, companyInfo.getRemark());
				builder.isPass(false);
			}

			return builder.walletId(walletTunnel.getWalletId())
				.checkTime(companyInfo.getCheckTime())
				.failReason(companyInfo.getFailReason())
				.build();

		} catch (Exception e) {
			log.error("定时任务同步通道信息错误", e);
		}

		return null;
	}

}
