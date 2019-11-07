package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef.BizValidateType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletChannelSignContract;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectInfo;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletCollectMethod.WalletCollectMethodBuilder;
import com.rfchina.wallet.domain.model.WalletConsume;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstQueryBalanceResult;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletConsumeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletWithdrawExtDao;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Alipay;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.BankCard;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Wechat;
import com.rfchina.wallet.server.model.ext.DeductionReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.EnumWallet.ChannelType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.DirtyType;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.msic.EnumWallet.OrderType;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.msic.EnumWallet.YunstFileType;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeniorPayService {

	public static final String PREFIX_REFUND = "WB";
	public static final String PREFIX_AGENT_PAY = "WO";
	public static final String PREFIX_COLLECT = "WC";
	public static final String PREFIX_RECHARGE = "WR";
	public static final String PREFIX_WITHDRAW = "WD";
	public static final String PREFIX_DEDUCTION = "WS";

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private WalletCollectExtDao walletCollectDao;

	@Autowired
	private WalletCollectMethodExtDao walletCollectMethodDao;

	@Autowired
	private WalletCollectInfoExtDao walletCollectInfoDao;

	@Autowired
	private WalletClearingExtDao walletClearingDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private WalletRefundExtDao walletRefundDao;

	@Autowired
	private WalletRefundDetailExtDao walletRefundDetailDao;

	@Autowired
	private WalletRechargeExtDao walletRechargeDao;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletWithdrawExtDao walletWithdrawDao;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private VerifyService verifyService;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Autowired
	private WalletConsumeExtDao walletConsumeDao;


	private Long anonyPayerWalletId = 10001L;
	private Long agentEntWalletId = 10000L;

	/**
	 * 充值
	 */
	public RechargeResp recharge(Long walletId, WalletCard walletCard, Long amount, String jumpUrl,
		String customerIp) {

		// 检查钱包
		Wallet payerWallet = verifyService.checkSeniorWallet(walletId);
		// 检查银行卡状态
		verifyService.checkCard(walletCard, payerWallet);

		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletOrderDao.selectCountByBatchNo(id) == 0;
		});
		String orderNo = IdGenerator.createBizId(PREFIX_RECHARGE, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});

		WalletOrder rechargeOrder = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(IdGenerator.createBizId(PREFIX_RECHARGE, 19, (id) -> true))
			.walletId(walletId)
			.type(OrderType.RECHARGE.getValue())
			.amount(amount)
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.createTime(new Date())
			.build();
		walletOrderDao.insertSelective(rechargeOrder);

		// 支付方式
		BankCard bankCard = new BankCard();
		bankCard.setBankCardNo(walletCard.getBankAccount());
		bankCard.setAmount(amount);
		bankCard.setPayType(CollectPayType.BANKCARD.getValue());
		WalletPayMethod payMethod = new WalletPayMethod();
		payMethod.setBankCard(bankCard);

		// 充值记录
		WalletRecharge recharge = WalletRecharge.builder()
			.orderId(rechargeOrder.getId())
			.validateType(BizValidateType.SMS.getValue())
			.payMethod(payMethod.getMethods())
			.createTime(new Date())
			.build();
		walletRechargeDao.insertSelective(recharge);
		savePayMethod(recharge.getId(), OrderType.RECHARGE.getValue(), payMethod);

		// 支付人
		WalletTunnel payer = walletTunnelDao
			.selectByWalletId(walletId, rechargeOrder.getTunnelType());

		// 充值
		EBankHandler handler = handlerHelper.selectByTunnelType(rechargeOrder.getTunnelType());
		RechargeResp result = handler.recharge(rechargeOrder, recharge, payer);

		String signedParams = ((YunstBizHandler) handler)
			.passwordConfirm(rechargeOrder, payer, jumpUrl, customerIp);
		result.setSignedParams(signedParams);

		return result;
	}

	/**
	 * 提现
	 */
	public WithdrawResp withdraw(Long walletId, WalletCard walletCard, Long amount, String jumpUrl,
		String customerIp) {

		// 检查钱包
		Wallet payerWallet = verifyService.checkSeniorWallet(walletId);
		// 检查银行卡状态
		verifyService.checkCard(walletCard, payerWallet);

		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletOrderDao.selectCountByBatchNo(id) == 0;
		});
		String orderNo = IdGenerator.createBizId(PREFIX_WITHDRAW, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});

		WalletOrder withdrawOrder = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(IdGenerator.createBizId(PREFIX_WITHDRAW, 19, (id) -> true))
			.walletId(walletId)
			.type(OrderType.WITHDRAWAL.getValue())
			.amount(amount)
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.createTime(new Date())
			.build();
		walletOrderDao.insertSelective(withdrawOrder);

		// 充值记录
		WalletWithdraw withdraw = WalletWithdraw.builder()
			.orderId(withdrawOrder.getId())
			.cardId(walletCard.getId())
			.bankAccount(walletCard.getBankAccount())
			.validateType(BizValidateType.SMS.getValue())
			.payMethod(ChannelType.BANKCARD.getValue())
			.createTime(new Date())
			.build();
		walletWithdrawDao.insertSelective(withdraw);

		// 提现人
		WalletTunnel payer = walletTunnelDao
			.selectByWalletId(withdrawOrder.getWalletId(), withdrawOrder.getTunnelType());
		// 充值
		EBankHandler handler = handlerHelper.selectByTunnelType(withdrawOrder.getTunnelType());
		WithdrawResp result = handler.withdraw(withdrawOrder, withdraw, payer);

		String signedParams = ((YunstBizHandler) handler)
			.passwordConfirm(withdrawOrder, payer, jumpUrl, customerIp);
		result.setSignedParams(signedParams);
		return result;
	}

	/**
	 * 预代收
	 */
	public WalletCollectResp collect(CollectReq req, String jumpUrl, String customerIp) {

		// 定义付款人
		Long payerWalletId = (req.getPayerWalletId() != null) ? req.getPayerWalletId()
			: anonyPayerWalletId;
		verifyService.checkSeniorWallet(payerWalletId);

		// 工单记录
		String orderNo = IdGenerator.createBizId(PREFIX_COLLECT, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletOrderDao.selectCountByBatchNo(id) == 0;
		});
		WalletOrder collectOrder = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(req.getBizNo())
			.walletId(payerWalletId)
			.type(OrderType.COLLECT.getValue())
			.amount(req.getAmount())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.createTime(new Date())
			.build();
		walletOrderDao.insertSelective(collectOrder);

		// 生成代收单
		Byte validateType =
			req.getWalletPayMethod().getBalance() != null ? BizValidateType.PASSWORD.getValue()
				: (req.getWalletPayMethod().getBankCard() != null ? BizValidateType.SMS.getValue()
					: 0);
		WalletCollect collect = WalletCollect.builder()
			.orderId(collectOrder.getId())
			.agentWalletId(agentEntWalletId)
			.refundLimit(req.getAmount())
			.payMethod(req.getWalletPayMethod().getMethods())
			.validateType(validateType)
			.createTime(new Date())
			.build();
		walletCollectDao.insertSelective(collect);
		savePayMethod(collect.getId(), OrderType.COLLECT.getValue(),
			req.getWalletPayMethod());

		// 生成清分记录
		List<WalletCollectInfo> collectInfos = req.getRecievers().stream().map(reciever -> {
			WalletCollectInfo clearInfo = WalletCollectInfo.builder()
				.collectId(collect.getId())
				.payeeWalletId(reciever.getWalletId())
				.budgetAmount(reciever.getAmount())
				.clearAmount(0L)
				.refundAmount(0L)
				.createTime(new Date())
				.build();
			walletCollectInfoDao.insertSelective(clearInfo);
			return clearInfo;
		}).collect(Collectors.toList());

		WalletTunnel payer = walletTunnelDao
			.selectByWalletId(collectOrder.getWalletId(), collectOrder.getTunnelType());

		EBankHandler handler = handlerHelper.selectByTunnelType(collectOrder.getTunnelType());
		WalletCollectResp result = handler.collect(collectOrder, collect, collectInfos, payer);
		if (collect.getValidateType().byteValue() == BizValidateType.PASSWORD.getValue()) {
			String signedParams = ((YunstBizHandler) handler)
				.passwordConfirm(collectOrder, payer, jumpUrl, customerIp);
			result.setSignedParams(signedParams);
		}

		return result;
	}

	/**
	 * 发起代付（加锁）
	 */
	public SettleResp agentPay(WalletOrder collectOrder, String bizNo, Reciever receiver) {

		// 代收明细
		WalletCollect walletCollect = walletCollectDao.selectByOrderId(collectOrder.getId());
		List<WalletCollectInfo> collectInfos = walletCollectInfoDao
			.selectByCollectId(walletCollect.getId());

		// 匹配原始分账记录
		WalletCollectInfo collectInfo = collectInfos.stream()
			.filter(c -> c.getPayeeWalletId().longValue() == receiver.getWalletId().longValue())
			.findFirst()
			.orElseThrow(() -> new WalletResponseException(
				EnumWalletResponseCode.AGENT_PAY_RECEIVER_NOT_MATCH));

		// 代付金额不超过剩余代付
		if (collectInfo.getBudgetAmount() < collectInfo.getClearAmount()
			+ collectInfo.getRefundAmount() + receiver.getAmount()) {
			throw new WalletResponseException(
				EnumWalletResponseCode.AGENT_PAY_AMOUNT_OVER_LIMIT);
		}

		// 工单记录
		String orderNo = IdGenerator.createBizId(PREFIX_AGENT_PAY, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletOrderDao.selectCountByBatchNo(id) == 0;
		});

		WalletOrder payOrder = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(bizNo)
			.walletId(receiver.getWalletId())
			.type(OrderType.AGENT_PAY.getValue())
			.amount(receiver.getAmount())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.createTime(new Date())
			.build();
		walletOrderDao.insertSelective(payOrder);

		// 代付明细
		WalletClearing clearing = WalletClearing.builder()
			.orderId(payOrder.getId())
			.collectOrderNo(collectOrder.getOrderNo())
			.collectInfoId(collectInfo.getId())
			.agentWalletId(agentEntWalletId)
			.amount(receiver.getAmount())
			.createTime(new Date())
			.build();
		walletClearingDao.insertSelective(clearing);

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByTunnelType(payOrder.getTunnelType());
		handler.agentPay(payOrder, clearing);

		return SettleResp.builder()
			.order(payOrder)
			.clearing(clearing)
			.build();

	}

	/**
	 * 退款
	 */
	public WalletOrder refund(WalletOrder collectOrder, String bizNo, List<RefundInfo> refundList) {

		// 不能超过可退金额
		Long histValue = getCollectSpand(collectOrder);
		Long applyValue = refundList.stream()
			.collect(Collectors.summingLong(RefundInfo::getAmount));
		if (histValue.longValue() + applyValue.longValue() > collectOrder.getAmount().longValue()) {
			throw new WalletResponseException(EnumWalletResponseCode.REFUND_AMOUNT_OVER_LIMIT);
		}
		// 代收明细
		WalletCollect walletCollect = walletCollectDao.selectByOrderId(collectOrder.getId());
		List<WalletCollectInfo> collectInfos = walletCollectInfoDao
			.selectByCollectId(walletCollect.getId());
		// 核对清分记录
		Map<String, WalletCollectInfo> infoMap = refundList.stream()
			.map(r -> {
				WalletCollectInfo collectInfo = collectInfos.stream()
					.filter(info -> info.getPayeeWalletId().longValue() == r.getWalletId())
					.findFirst()
					.orElseThrow(() -> new WalletResponseException(
						EnumWalletResponseCode.REFUND_RECEIVER_NOT_EXISTS,
						r.getWalletId().toString())
					);
				// 退款金额不超过分帐金额+已退金额+已清金额
				if (collectInfo.getBudgetAmount() < collectInfo.getRefundAmount()
					+ collectInfo.getClearAmount() + r.getAmount()) {
					throw new WalletResponseException(
						EnumWalletResponseCode.REFUND_AMOUNT_OVER_LIMIT);
				}
				return collectInfo;
			}).collect(Collectors.toMap(c -> c.getPayeeWalletId().toString(), c -> c));

		// 工单记录
		String orderNo = IdGenerator.createBizId(PREFIX_REFUND, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletOrderDao.selectCountByBatchNo(id) == 0;
		});

		WalletOrder refundOrder = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(bizNo)
			.walletId(collectOrder.getWalletId())
			.type(OrderType.REFUND.getValue())
			.amount(refundList.stream().collect(Collectors.summingLong(RefundInfo::getAmount)))
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.createTime(new Date())
			.build();
		walletOrderDao.insertSelective(refundOrder);

		// 记录退款单
		WalletRefund refund = WalletRefund.builder()
			.orderId(refundOrder.getId())
			.collectOrderNo(collectOrder.getOrderNo())
			.agentWalletId(agentEntWalletId)
			.collectAmount(collectOrder.getAmount())
			.createTime(new Date())
			.build();
		walletRefundDao.insertSelective(refund);

		// 记录退款明细
		List<WalletRefundDetail> details = refundList.stream()
			.map(r -> {
				WalletCollectInfo info = infoMap.get(r.getWalletId().toString());
				WalletRefundDetail detail = WalletRefundDetail.builder()
					.refundId(refund.getId())
					.payeeWalletId(Long.valueOf(r.getWalletId()))
					.collectInfoId(info != null ? info.getId() : null)
					.amount(r.getAmount())
					.createTime(new Date())
					.build();
				walletRefundDetailDao.insertSelective(detail);
				return detail;
			}).collect(Collectors.toList());

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByTunnelType(refundOrder.getTunnelType());
		handler.refund(refundOrder, refund, details);
		return refundOrder;
	}


	/**
	 * 预代收
	 */
	public WalletCollectResp deduction(DeductionReq req, String jumpUrl, String customerIp) {

		// 定义付款人
		Long payerWalletId = (req.getPayerWalletId() != null) ? req.getPayerWalletId()
			: anonyPayerWalletId;
		verifyService.checkSeniorWallet(payerWalletId);

		// 工单记录
		String orderNo = IdGenerator.createBizId(PREFIX_DEDUCTION, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletOrderDao.selectCountByBatchNo(id) == 0;
		});
		WalletOrder consumeOrder = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(req.getBizNo())
			.walletId(payerWalletId)
			.type(OrderType.DEDUCTION.getValue())
			.amount(req.getAmount())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.createTime(new Date())
			.build();
		walletOrderDao.insertSelective(consumeOrder);

		// 生成代收单
		WalletConsume consume = WalletConsume.builder()
			.orderId(consumeOrder.getId())
			.payeeWalletId(agentEntWalletId)
			.validateType(BizValidateType.NONE.getValue())
			.createTime(new Date())
			.build();
		walletConsumeDao.insertSelective(consume);
		WalletCollectMethod method = savePayMethod(consume.getId(),
			OrderType.DEDUCTION.getValue(),
			req.getWalletPayMethod());

		WalletTunnel payer = walletTunnelDao
			.selectByWalletId(consumeOrder.getWalletId(), consumeOrder.getTunnelType());
		WalletTunnel payee = walletTunnelDao
			.selectByWalletId(consume.getPayeeWalletId(), consumeOrder.getTunnelType());

		EBankHandler handler = handlerHelper.selectByTunnelType(consumeOrder.getTunnelType());
		WalletCollectResp result = handler.consume(consumeOrder, consume, payer, payee,
			Arrays.asList(method));
		if (consume.getValidateType().byteValue() == BizValidateType.PASSWORD.getValue()) {
			String signedParams = ((YunstBizHandler) handler)
				.passwordConfirm(consumeOrder, payer, jumpUrl, customerIp);
			result.setSignedParams(signedParams);
		}

		return result;
	}


	private Long getCollectSpand(WalletOrder collectOrder) {
		// 在途和已清算金额总额
		List<WalletClearing> histClearings = walletClearingDao
			.selectByCollectOrderNo(collectOrder.getOrderNo());
		Long clearedValue = histClearings.stream()
			.map(clearing -> {
				WalletOrder order = walletOrderDao.selectByPrimaryKey(clearing.getOrderId());
				boolean accumulate =
					OrderStatus.FAIL.getValue().byteValue() != order.getStatus().byteValue();
				return accumulate ? order.getAmount() : 0L;
			})
			.collect(Collectors.summingLong(Long::valueOf));
		// 在途和已退款金额总额
		List<WalletRefund> histRefunds = walletRefundDao
			.selectByCollectOrderNo(collectOrder.getOrderNo());
		Long refundedValue = histRefunds.stream()
			.map(refund -> {
				WalletOrder order = walletOrderDao.selectByPrimaryKey(refund.getOrderId());
				boolean accumulate =
					OrderStatus.FAIL.getValue().byteValue() != order.getStatus().byteValue();
				return accumulate ? order.getAmount() : 0L;
			})
			.collect(Collectors.summingLong(Long::valueOf));

		return clearedValue + refundedValue;
	}

	/**
	 * 保存支付方式
	 */
	private WalletCollectMethod savePayMethod(Long collectId, Byte type,
		WalletPayMethod payMethod) {
		// 支付方式
		WalletCollectMethodBuilder builder = WalletCollectMethod.builder()
			.refId(collectId)
			.type(type);
		if (payMethod.getBalance() != null) {
			Balance balance = payMethod.getBalance();
			builder.channelType(ChannelType.BALANCE.getValue())
				.payType(CollectPayType.BALANCE.getValue())
				.amount(balance.getAmount());
		} else if (payMethod.getWechat() != null) {
			Wechat wechat = payMethod.getWechat();
			CollectPayType payType = EnumUtil.parse(CollectPayType.class, wechat.getPayType());
			builder.channelType(ChannelType.WECHAT.getValue())
				.payType(payType.getValue().byteValue())
				.amount(wechat.getAmount())
				.openId(wechat.getOpenId())
				.cusIp(wechat.getCusip())
				.appId(wechat.getSubAppId())
				.sceneInfo(wechat.getSceneInfo());
		} else if (payMethod.getAlipay() != null) {
			Alipay alipay = payMethod.getAlipay();
			CollectPayType payType = EnumUtil.parse(CollectPayType.class, alipay.getPayType());
			builder.channelType(ChannelType.ALIPAY.getValue())
				.payType(payType.getValue().byteValue())
				.amount(alipay.getAmount())
				.openId(alipay.getUserId());
		} else if (payMethod.getCodePay() != null) {
			CodePay codePay = payMethod.getCodePay();
			CollectPayType payType = EnumUtil.parse(CollectPayType.class, codePay.getPayType());
			builder.channelType(ChannelType.ALIPAY.getValue())
				.payType(payType.getValue().byteValue())
				.amount(codePay.getAmount())
				.sceneInfo(codePay.getAuthcode());
		} else if (payMethod.getBankCard() != null) {
			BankCard bankCard = payMethod.getBankCard();
			builder.channelType(ChannelType.BANKCARD.getValue())
				.payType(CollectPayType.BANKCARD.getValue())
				.amount(bankCard.getAmount())
				.openId(bankCard.getBankCardNo());
		}
		WalletCollectMethod method = builder.build();
		walletCollectMethodDao.insertSelective(method);
		return method;
	}

	/**
	 * 订单查询
	 */
	public WalletOrder orderQuery(String orderNo) {
		return walletOrderDao.selectByOrderNo(orderNo);
	}

	/**
	 * 短信确认
	 */
	public void smsConfirm(WalletOrder order, String tradeNo, String verifyCode, String ip) {

		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		if (handler instanceof YunstBizHandler) {
			((YunstBizHandler) handler).smsConfirm(order, tradeNo, verifyCode, ip);
		}
	}

	/**
	 * 🔁重发短信
	 */
	public void smsRetry(WalletOrder order) {

		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		if (handler instanceof YunstBizHandler) {
			((YunstBizHandler) handler).smsRetry(order);
		}
	}

	@PostMq(routingKey = MqConstant.ORDER_STATUS_CHANGE)
	public WalletOrder updateOrderStatus(String orderNo) {

		WalletOrder order = verifyService.checkOrder(orderNo, OrderStatus.WAITTING.getValue());
		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		if (handler instanceof YunstBizHandler) {
			WalletOrder walletOrder = ((YunstBizHandler) handler).updateOrderStatus(order);
			WalletTunnel walletTunnel = walletTunnelDao
				.selectByWalletId(order.getWalletId(), order.getTunnelType());
			// 判断签约
			if (walletTunnel.getIsSignContact() == null
				|| walletTunnel.getIsSignContact() == WalletChannelSignContract.NONE.getValue()
				.byteValue()) {
				return null;
			}
			// 通联查余额
			YunstQueryBalanceResult result = yunstUserHandler
				.queryBalance(walletTunnel.getBizUserId());
			// 更新通道余额
			walletTunnel.setBalance(result.getAllAmount());
			walletTunnel.setFreezenAmount(result.getFreezenAmount());
			walletTunnel.setIsDirty(DirtyType.NORMAL.getValue());
			walletTunnelDao.updateByPrimaryKeySelective(walletTunnel);
			// 更新钱包余额
			Wallet wallet = walletDao.selectByPrimaryKey(walletTunnel.getWalletId());
			wallet.setWalletBalance(walletTunnel.getBalance());
			wallet.setFreezeAmount(walletTunnel.getFreezenAmount());
			walletDao.updateByPrimaryKeySelective(wallet);

			return walletOrder;
		}

		return null;
	}

	/**
	 * 对账
	 */
	public String balance(Date date) {
		EBankHandler handler = handlerHelper.selectByTunnelType(TunnelType.YUNST.getValue());
		if (handler instanceof YunstBizHandler) {
			((YunstBizHandler) handler).balanceUrl(date, YunstFileType.DETAIL);
		}

		return null;
	}


}
