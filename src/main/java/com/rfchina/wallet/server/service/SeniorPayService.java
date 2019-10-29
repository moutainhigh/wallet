package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef.BizValidateType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletLevel;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletClearInfo;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletCollectMethod.WalletCollectMethodBuilder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletWithdrawExtDao;
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
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import com.rfchina.wallet.server.msic.EnumWallet.ChannelType;
import com.rfchina.wallet.server.msic.EnumWallet.ClearInfoStatus;
import com.rfchina.wallet.server.msic.EnumWallet.ClearingStatus;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectStatus;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumWallet.RefundStatus;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletType;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

	private Long anonyPayerWalletId = 10001L;
	private Long agentEntWalletId = 10000L;

	/**
	 * 充值
	 */
	public RechargeResp recharge(RechargeReq req) {
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.bizNo(req.getBizNo())
			.type(WalletApplyType.RECHARGE.getValue())
			.amount(req.getAmount())
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		Long payerWalletId = (req.getPayerWalletId() != null) ? req.getPayerWalletId()
			: anonyPayerWalletId;
		WalletPayMethod payMethod = req.getWalletPayMethod();
		Wallet payerWallet = walletDao.selectByPrimaryKey(payerWalletId);
		if (payerWallet == null || payerWallet.getStatus() != WalletStatus.ACTIVE.getValue()
			.byteValue()) {
			throw new RuntimeException();
		}
		// 充值记录
		String orderNo = IdGenerator.createBizId(PREFIX_RECHARGE, 19, id -> {
			return walletRechargeDao.selectCountByOrderNo(id) == 0;
		});
		WalletRecharge recharge = WalletRecharge.builder()
			.applyId(walletApply.getId())
			.orderNo(orderNo)
			.payerWalletId(payerWallet.getId())
			.payeeWalletId(payerWallet.getId())
			.validateType(BizValidateType.SMS.getValue())
			.amount(req.getAmount())
			.tunnelType(TunnelType.YUNST.getValue())
			.payMethod(payMethod.getMethods())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(CollectStatus.WAIT_PAY.getValue())
			.expireTime(req.getExpireTime())
			.build();
		walletRechargeDao.insertSelective(recharge);
		savePayMethod(recharge.getId(), WalletApplyType.RECHARGE.getValue(), payMethod);

		// 充值
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		List<RechargeResp> results = handler.recharge(walletApply.getId());
		return results.stream()
			.filter(r -> r.getId() == recharge.getId())
			.findFirst()
			.orElse(null);
	}

	/**
	 * 充值确认
	 */
	public void rechargeConfirm(Long applyId, String tradeNo, String verifyCode, String ip) {
		WalletApply walletApply = walletApplyDao.selectByPrimaryKey(applyId);
		// 充值
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		if (handler instanceof YunstBizHandler) {
			((YunstBizHandler) handler).smsConfirm(walletApply, tradeNo, verifyCode, ip);
		}
	}

	/**
	 * 提现
	 */
	public WalletWithdraw withdraw(WithdrawReq req) {
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.bizNo(req.getBizNo())
			.type(WalletApplyType.WITHDRAWAL.getValue())
			.amount(req.getAmount())
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		// 检查钱包状态
		Wallet payerWallet = walletDao.selectByPrimaryKey(req.getPayerWalletId());
		Optional.ofNullable(payerWallet)
			.filter(wallet -> wallet.getStatus() == WalletStatus.ACTIVE.getValue())
			.orElseThrow(() -> new RuntimeException());

		// 检查银行卡状态
		WalletCard walletCard = walletCardDao.selectByPrimaryKey(req.getCardId());
		Optional.ofNullable(walletCard)
			.filter(card -> card.getWalletId().longValue() == payerWallet.getId().longValue()
				&& card.getStatus().byteValue() == 1)
			.orElseThrow(() -> new RuntimeException());

		// 充值记录
		String orderNo = IdGenerator.createBizId(PREFIX_WITHDRAW, 19, id -> {
			return walletRechargeDao.selectCountByOrderNo(id) == 0;
		});
		WalletWithdraw withdraw = WalletWithdraw.builder()
			.applyId(walletApply.getId())
			.orderNo(orderNo)
			.payerWalletId(payerWallet.getId())
			.cardId(walletCard.getId())
			.bankAccount(walletCard.getBankAccount())
			.validateType(BizValidateType.SMS.getValue())
			.amount(req.getAmount())
			.tunnelType(TunnelType.YUNST.getValue())
			.payMethod(ChannelType.BANKCARD.getValue())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(CollectStatus.WAIT_PAY.getValue())
			.expireTime(req.getExpireTime())
			.build();
		walletWithdrawDao.insertSelective(withdraw);

		// 充值
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		List<WalletWithdraw> results = handler.withdraw(walletApply.getId());
		return results.stream()
			.filter(r -> r.getId() == withdraw.getId())
			.findFirst()
			.orElse(null);
	}

	/**
	 * 预代收
	 */
	public WalletCollect preCollect(CollectReq collectReq) {

		// 定义付款人
		Long payerWalletId = (collectReq.getPayerWalletId() != null) ? collectReq.getPayerWalletId()
			: anonyPayerWalletId;
		Wallet payerWallet = walletDao.selectByPrimaryKey(payerWalletId);
		if (payerWallet == null || payerWallet.getStatus() != WalletStatus.ACTIVE.getValue()
			.byteValue()) {
			throw new RuntimeException();
		}

		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.bizNo(collectReq.getBizNo())
			.type(WalletApplyType.COLLECT.getValue())
			.amount(collectReq.getAmount())
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(payerWallet.getLevel())
			.walletType(payerWallet.getType())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		// 生成代收单
		WalletPayMethod payMethod = collectReq.getWalletPayMethod();
		if (payMethod.getMethods() == 0) {
			throw new RuntimeException();
		}
		String orderNo = IdGenerator.createBizId(PREFIX_COLLECT, 19, id -> {
			return walletRechargeDao.selectCountByOrderNo(id) == 0;
		});
		WalletCollect collect = WalletCollect.builder()
			.orderNo(orderNo)
			.applyId(walletApply.getId())
			.payerWalletId(payerWallet.getId())
			.agentWalletId(agentEntWalletId)
			.amount(collectReq.getAmount())
			.refundLimit(collectReq.getAmount())
			.tunnelType(TunnelType.YUNST.getValue())
			.payMethod(payMethod.getMethods())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(CollectStatus.WAIT_PAY.getValue())
			.expireTime(collectReq.getExpireTime())
			.build();
		walletCollectDao.insertSelective(collect);
		savePayMethod(collect.getId(), WalletApplyType.COLLECT.getValue(),
			collectReq.getWalletPayMethod());

		// 生成清分记录
		collectReq.getRecievers().forEach(reciever -> {
			WalletClearInfo clearInfo = WalletClearInfo.builder()
				.collectId(collect.getId())
				.payeeWalletId(reciever.getWalletId())
				.budgetAmount(reciever.getAmount())
				.clearAmount(0L)
				.refundAmount(0L)
				.status(ClearInfoStatus.WAITING.getValue())
				.build();
			walletClearInfoDao.insertSelective(clearInfo);
		});

		return collect;
	}

	/**
	 * 发起代收
	 */
	public WalletCollectResp doCollect(WalletCollect collect) {
		WalletApply walletApply = walletApplyDao.selectByPrimaryKey(collect.getApplyId());

		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		List<WalletCollectResp> result = handler.collect(walletApply.getId());

		return result.stream()
			.filter(c -> c.getId().longValue() == collect.getId().longValue())
			.findFirst()
			.orElse(null);
	}

	/**
	 * 查询代收结果
	 */
	public WalletCollect queryCollect(String orderNo) {
		return walletCollectDao.selectByOrderNo(orderNo);
	}


	/**
	 * 发起代付（加锁）
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public SettleResp agentPay(String collectOrderNo,
		List<Reciever> receivers) {
		WalletCollect collect = walletCollectDao.selectByOrderNo(collectOrderNo);
		if (collect == null) {
			throw new RuntimeException();
		}
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.type(WalletApplyType.AGENT_PAY.getValue())
			.amount(receivers.stream().map(req -> req.getAmount()).reduce(0L, (x, y) -> x + y))
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		List<WalletClearInfo> clearInfos = walletClearInfoDao.selectByCollectId(collect.getId());
		// 判断收款人记录不重复
		// 匹配原始分账记录
		Map<Long, Long> infoMap = receivers.stream()
			.collect(Collectors.toMap(receiver -> receiver.getWalletId(), receiver -> {
				Optional<WalletClearInfo> opt = clearInfos.stream().filter(
					clear -> clear.getPayeeWalletId().longValue() == receiver.getWalletId()
						.longValue())
					.findFirst();
				WalletClearInfo clearInfo = opt.orElseThrow(() -> new RuntimeException());
				// 代付金额不超过剩余代付
				if (clearInfo.getBudgetAmount() - clearInfo.getClearAmount()
					- clearInfo.getRefundAmount() < receiver.getAmount()) {
					throw new RuntimeException();
				}
				return clearInfo.getId();
			}));
		receivers.forEach(receiver -> {
			String orderNo = IdGenerator.createBizId(PREFIX_AGENT_PAY, 19, id -> {
				WalletClearing walletClearing = walletClearingDao.selectByOrderNo(id);
				return walletClearing == null;
			});
			WalletClearing clearing = WalletClearing.builder()
				.orderNo(orderNo)
				.applyId(walletApply.getId())
				.collectId(collect.getId())
				.collectInfoId(infoMap.get(receiver.getWalletId()))
				.payeeWalletId(receiver.getWalletId())
				.tunnelType(TunnelType.YUNST.getValue())
				.amount(receiver.getAmount())
				.status(ClearingStatus.WAITING.getValue())
				.build();
			walletClearingDao.insertSelective(clearing);
		});

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		List<WalletClearing> walletClearings = handler.agentPay(walletApply.getId());

		return SettleResp.builder()
			.collect(collect)
			.clearings(walletClearings)
			.build();

	}

	/**
	 * 查询代付结果
	 */
	public WalletClearing agentPayQuery(String agentPayOrderNo) {
		return walletClearingDao.selectByOrderNo(agentPayOrderNo);
	}


	/**
	 * 退款
	 */
	public WalletRefund refund(String collectOrderNo,
		List<RefundInfo> refundList) {
		// 退款申请不重复
		Set<String> walletIdSet = refundList.stream().map(r -> r.getWalletId().toString())
			.collect(Collectors.toSet());
		if (walletIdSet.size() != refundList.size()) {
			throw new RuntimeException();
		}

		// 在途和新的退款金额总额不超过代收单金额
		WalletCollect collect = walletCollectDao.selectByOrderNo(collectOrderNo);
		List<WalletClearing> histClearings = walletClearingDao.selectByCollectId(collect.getId());
		List<WalletRefund> histRefunds = walletRefundDao.selectByCollectId(collect.getId());
		Long clearedValue = histClearings.stream()
			.filter(
				c -> ClearingStatus.FAIL.getValue().byteValue() != c.getStatus().byteValue())
			.map(c -> c.getAmount())
			.reduce(0L, (a, b) -> a + b);
		Long histRefund = histRefunds.stream()
			.filter(r -> r.getStatus().byteValue() != RefundStatus.FAIL.getValue().byteValue())
			.map(r -> r.getAmount())
			.reduce(0L, (a, b) -> a + b);

		Long applyValue = refundList.stream()
			.map(r -> r.getAmount())
			.reduce(0L, (a, b) -> a + b);
		if (collect.getAmount().longValue() < clearedValue.longValue() + histRefund.longValue()
			+ applyValue.longValue()) {
			throw new RuntimeException();
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
//			Long histRefund = histRefunds.stream()
//				.filter(r -> r.getStatus().byteValue() != RefundStatus.FAIL.getValue().byteValue())
////				.filter(r -> r.getPayerWalletId().getWalletId().longValue() == Long.valueOf(r.getWalletId()))
//				.map(r -> r.getAmount())
//				.reduce(0L, (a, b) -> a + b);
//		});

		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.type(WalletApplyType.REFUND.getValue())
			.amount(
				refundList.stream().map(refund -> refund.getAmount()).reduce((x, y) -> x + y).get())
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		// 记录退款单
		String orderNo = IdGenerator.createBizId(PREFIX_REFUND, 19, id -> {
			return walletRefundDao.selectByOrderNo(id) == null;
		});
		WalletRefund walletRefund = WalletRefund.builder()
			.orderNo(orderNo)
			.applyId(walletApply.getId())
			.collectId(collect.getId())
			.payerWalletId(collect.getPayerWalletId())
			.agentWalletId(agentEntWalletId)
			.amount(applyValue)
			.collectAmount(collect.getAmount())
			.tunnelType(collect.getTunnelType())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(RefundStatus.WAITING.getValue())
			.build();
		walletRefundDao.insertSelective(walletRefund);

		// 记录退款明细
		refundList.forEach(refund -> {
			WalletRefundDetail detail = WalletRefundDetail.builder()
				.refundId(walletRefund.getId())
				.payeeWalletId(Long.valueOf(refund.getWalletId()))
				.amount(refund.getAmount())
				.build();
			walletRefundDetailDao.insertSelective(detail);
		});

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		List<WalletRefund> results = handler.refund(walletRefund.getApplyId());

		return results.stream()
			.filter(r -> r.getId().longValue() == walletRefund.getId().longValue())
			.findFirst()
			.orElse(null);
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

	public WalletRefund refundQuery(String refundOrderNo) {
		return walletRefundDao.selectByOrderNo(refundOrderNo);
	}
}
