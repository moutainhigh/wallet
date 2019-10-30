package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef.BizValidateType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletClearInfo;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletCollectMethod.WalletCollectMethodBuilder;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletWithdrawExtDao;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Alipay;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.BankCard;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Wechat;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import com.rfchina.wallet.server.msic.EnumWallet.ChannelType;
import com.rfchina.wallet.server.msic.EnumWallet.ClearInfoStatus;
import com.rfchina.wallet.server.msic.EnumWallet.ClearingStatus;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletStatus;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeniorPayService {

	public static final String PREFIX_REFUND = "WB";
	public static final String PREFIX_AGENT_PAY = "WO";
	public static final String PREFIX_COLLECT = "WC";
	public static final String PREFIX_RECHARGE = "WR";
	public static final String PREFIX_WITHDRAW = "WW";

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
	private WalletClearInfoExtDao walletClearInfoDao;

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

	private Long anonyPayerWalletId = 10001L;
	private Long agentEntWalletId = 10000L;

	/**
	 * 充值
	 */
	public RechargeResp recharge(RechargeReq req) {
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletApplyDao.selectCountByBatchNo(id) == 0;
		});
		String orderNo = IdGenerator.createBizId(PREFIX_RECHARGE, 19, id -> {
			return walletRechargeDao.selectCountByOrderNo(id) == 0;
		});

		WalletOrder order = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(req.getBizNo())
			.walletId(req.getPayerWalletId())
			.type(WalletApplyType.RECHARGE.getValue())
			.amount(req.getAmount())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.build();
		walletOrderDao.insertSelective(order);

		// 充值记录
		WalletPayMethod payMethod = req.getWalletPayMethod();
		Wallet payerWallet = walletDao.selectByPrimaryKey(req.getPayerWalletId());
		checkWalletStatus(payerWallet);

		WalletRecharge recharge = WalletRecharge.builder()
			.orderId(order.getId())
			.payerWalletId(payerWallet.getId())
			.payeeWalletId(payerWallet.getId())
			.validateType(BizValidateType.SMS.getValue())
			.payMethod(payMethod.getMethods())
			.build();
		walletRechargeDao.insertSelective(recharge);
		savePayMethod(recharge.getId(), WalletApplyType.RECHARGE.getValue(), payMethod);

		// 充值
		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		return handler.recharge(order, recharge);
	}

	private void checkWalletStatus(Wallet payerWallet) {
		Optional.ofNullable(payerWallet)
			.filter(wallet -> wallet.getStatus() == WalletStatus.ACTIVE.getValue())
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.WALLET_STATUS_ERROR));
	}

	/**
	 * 充值确认
	 */
	public void smsConfirm(Long orderId, String tradeNo, String verifyCode, String ip) {
		WalletOrder order = walletOrderDao.selectByPrimaryKey(orderId);
		Wallet wallet = walletDao.selectByPrimaryKey(order.getWalletId());
		// 充值
		EBankHandler handler = handlerHelper.selectByWalletLevel(wallet.getLevel());
		if (handler instanceof YunstBizHandler) {
			((YunstBizHandler) handler).smsConfirm(order, tradeNo, verifyCode, ip);
		}
	}


	/**
	 * 提现
	 */
	public WalletOrder withdraw(WithdrawReq req) {
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		String orderNo = IdGenerator.createBizId(PREFIX_WITHDRAW, 19, id -> {
			return walletRechargeDao.selectCountByOrderNo(id) == 0;
		});

		WalletOrder order = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(req.getBizNo())
			.walletId(req.getPayerWalletId())
			.type(WalletApplyType.WITHDRAWAL.getValue())
			.amount(req.getAmount())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.build();
		walletOrderDao.insertSelective(order);

		// 检查钱包状态
		Wallet payerWallet = walletDao.selectByPrimaryKey(req.getPayerWalletId());
		checkWalletStatus(payerWallet);

		// 检查银行卡状态
		WalletCard walletCard = walletCardDao.selectByPrimaryKey(req.getCardId());
		Optional.ofNullable(walletCard)
			.filter(card -> card.getWalletId().longValue() == payerWallet.getId().longValue()
				&& card.getStatus().byteValue() == 1)
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.BANK_CARD_STATUS_ERROR));

		// 充值记录

		WalletWithdraw withdraw = WalletWithdraw.builder()
			.orderId(order.getId())
			.cardId(walletCard.getId())
			.bankAccount(walletCard.getBankAccount())
			.validateType(BizValidateType.SMS.getValue())
			.payMethod(ChannelType.BANKCARD.getValue())
			.build();
		walletWithdrawDao.insertSelective(withdraw);

		// 充值
		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		handler.withdraw(order, withdraw);
		return order;
	}

	/**
	 * 预代收
	 */
	public WalletCollectResp collect(CollectReq req) {

		// 定义付款人
		Long payerWalletId = (req.getPayerWalletId() != null) ? req.getPayerWalletId()
			: anonyPayerWalletId;
		Wallet payerWallet = walletDao.selectByPrimaryKey(payerWalletId);
		checkWalletStatus(payerWallet);

		// 工单记录
		String orderNo = IdGenerator.createBizId(PREFIX_COLLECT, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletOrder order = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(req.getBizNo())
			.walletId(req.getPayerWalletId())
			.type(WalletApplyType.COLLECT.getValue())
			.amount(req.getAmount())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.build();
		walletOrderDao.insertSelective(order);

		// 生成代收单
		WalletCollect collect = WalletCollect.builder()
			.orderId(order.getId())
			.agentWalletId(agentEntWalletId)
			.refundLimit(req.getAmount())
			.payMethod(req.getWalletPayMethod().getMethods())
			.build();
		walletCollectDao.insertSelective(collect);
		savePayMethod(collect.getId(), WalletApplyType.COLLECT.getValue(),
			req.getWalletPayMethod());

		// 生成清分记录
		List<WalletClearInfo> clearInfos = req.getRecievers().stream().map(reciever -> {
			WalletClearInfo clearInfo = WalletClearInfo.builder()
				.collectId(collect.getId())
				.payeeWalletId(reciever.getWalletId())
				.budgetAmount(reciever.getAmount())
				.clearAmount(0L)
				.refundAmount(0L)
				.status(ClearInfoStatus.WAITING.getValue())
				.build();
			walletClearInfoDao.insertSelective(clearInfo);
			return clearInfo;
		}).collect(Collectors.toList());

		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		return handler.collect(order, collect, clearInfos);
	}


	/**
	 * 发起代付（加锁）
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public SettleResp agentPay(String collectOrderNo, String bizNo, List<Reciever> receivers) {

		// 检查代收单
		WalletOrder collect = walletOrderDao.selectByOrderNo(collectOrderNo);
		Optional.ofNullable(collect)
			.orElseThrow(() -> new WalletResponseException(EnumResponseCode.COMMON_INVALID_PARAMS,
				"collectOrderNo"));

		// 工单记录
		String orderNo = IdGenerator.createBizId(PREFIX_AGENT_PAY, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});

		WalletOrder order = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(bizNo)
			.walletId(null)
			.type(WalletApplyType.AGENT_PAY.getValue())
			.amount(receivers.stream().map(req -> req.getAmount()).reduce(0L, (x, y) -> x + y))
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.build();
		walletOrderDao.insertSelective(order);

		List<WalletClearInfo> clearInfos = walletClearInfoDao.selectByCollectId(collect.getId());

		// 匹配原始分账记录
		Map<Long, Long> infoMap = receivers.stream()
			.collect(Collectors.toMap(receiver -> receiver.getWalletId(), receiver -> {
				Optional<WalletClearInfo> opt = clearInfos.stream().filter(
					clear -> clear.getPayeeWalletId().longValue() == receiver.getWalletId()
						.longValue())
					.findFirst();
				WalletClearInfo clearInfo = opt.orElseThrow(() -> new WalletResponseException(
					EnumWalletResponseCode.AGENT_PAY_RECEIVER_NOT_MATCH));
				// 代付金额不超过剩余代付
				if (clearInfo.getBudgetAmount() - clearInfo.getClearAmount()
					- clearInfo.getRefundAmount() < receiver.getAmount()) {
					throw new WalletResponseException(
						EnumWalletResponseCode.AGENT_PAY_AMOUNT_OVER_LIMIT);
				}
				return clearInfo.getId();
			}));

		List<WalletClearing> clearings = receivers.stream().map(receiver -> {
			WalletClearing clearing = WalletClearing.builder()
				.orderId(order.getId())
				.collectOrderNo(collect.getOrderNo())
				.collectInfoId(infoMap.get(receiver.getWalletId()))
				.payeeWalletId(receiver.getWalletId())
				.build();
			walletClearingDao.insertSelective(clearing);
			return clearing;
		}).collect(Collectors.toList());

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		handler.agentPay(order, clearings);

		return SettleResp.builder()
			.order(order)
			.clearings(clearings)
			.build();

	}


	/**
	 * 退款
	 */
	public WalletOrder refund(String collectOrderNo, String bizNo, List<RefundInfo> refundList) {

		// 在途和已清算金额总额
		List<WalletClearing> histClearings = walletClearingDao
			.selectByCollectOrderNo(collectOrderNo);
		Long clearedValue = histClearings.stream()
			.map(clearing -> {
				WalletOrder order = walletOrderDao.selectByPrimaryKey(clearing.getOrderId());
				boolean accumulate =
					ClearingStatus.FAIL.getValue().byteValue() != order.getStatus().byteValue();
				return accumulate ? order.getAmount() : 0L;
			})
			.collect(Collectors.summingLong(Long::valueOf));
		// 在途和已退款金额总额
		List<WalletRefund> histRefunds = walletRefundDao.selectByCollectOrderNo(collectOrderNo);
		Long refundedValue = histRefunds.stream()
			.map(refund -> {
				WalletOrder order = walletOrderDao.selectByPrimaryKey(refund.getOrderId());
				boolean accumulate =
					ClearingStatus.FAIL.getValue().byteValue() != order.getStatus().byteValue();
				return accumulate ? order.getAmount() : 0L;
			})
			.collect(Collectors.summingLong(Long::valueOf));
		// 不能超过可退金额
		Long applyValue = refundList.stream()
			.collect(Collectors.summingLong(RefundInfo::getAmount));
		WalletOrder collect = walletOrderDao.selectByOrderNo(collectOrderNo);
		if (clearedValue.longValue() + refundedValue.longValue()
			+ applyValue.longValue() > collect.getAmount().longValue()) {
			throw new WalletResponseException(EnumWalletResponseCode.REFUND_AMOUNT_OVER_LIMIT);
		}

		// 核对清分记录
//		List<WalletClearInfo> clearInfos = walletClearInfoDao.selectByCollectId(collect.getId());
//		refundList.forEach(r -> {
//			Long clearedValue = histClearings.stream()
//				.filter(
//					c -> ClearingStatus.FAIL.getValue().byteValue() != c.getStatus().byteValue())
//				.filter(c -> c.getWalletId().longValue() == Long.valueOf(r.getWalletId()))
//				.map(c -> c.getAmount())
//				.reduce(0L, (a, b) -> a + b);
//			Long refundedValue = histRefunds.stream()
//				.filter(r -> r.getStatus().byteValue() != RefundStatus.FAIL.getValue().byteValue())
////				.filter(r -> r.getPayerWalletId().getWalletId().longValue() == Long.valueOf(r.getWalletId()))
//				.map(r -> r.getAmount())
//				.reduce(0L, (a, b) -> a + b);
//		});

		// 工单记录
		String orderNo = IdGenerator.createBizId(PREFIX_REFUND, 19, id -> {
			return walletOrderDao.selectCountByOrderNo(id) == 0;
		});
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});

		WalletOrder order = WalletOrder.builder()
			.orderNo(orderNo)
			.batchNo(batchNo)
			.bizNo(bizNo)
			.walletId(collect.getWalletId())
			.type(WalletApplyType.REFUND.getValue())
			.amount(refundList.stream().collect(Collectors.summingLong(RefundInfo::getAmount)))
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(OrderStatus.WAITTING.getValue())
			.tunnelType(TunnelType.YUNST.getValue())
			.build();
		walletOrderDao.insertSelective(order);

		// 记录退款单
		WalletRefund refund = WalletRefund.builder()
			.orderId(order.getId())
			.collectOrderNo(collect.getOrderNo())
			.agentWalletId(agentEntWalletId)
			.collectAmount(collect.getAmount())
			.build();
		walletRefundDao.insertSelective(refund);

		// 记录退款明细
		List<WalletRefundDetail> details = refundList.stream().map(r -> {
			WalletRefundDetail detail = WalletRefundDetail.builder()
				.refundId(refund.getId())
				.payeeWalletId(Long.valueOf(r.getWalletId()))
				.amount(r.getAmount())
				.build();
			walletRefundDetailDao.insertSelective(detail);
			return detail;
		}).collect(Collectors.toList());

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		handler.refund(order, refund, details);
		return order;
	}

	/**
	 * 保存支付方式
	 */
	private void savePayMethod(Long collectId, Byte type, WalletPayMethod payMethod) {
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
		walletCollectMethodDao.insertSelective(builder.build());
	}

	public WalletOrder orderQuery(String orderNo) {
		return walletOrderDao.selectByOrderNo(orderNo);
	}


}
