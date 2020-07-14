package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.mch.sdk.model.ChargingConfig;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.biztools.CacheHashMap;
import com.rfchina.platform.common.misc.Triple;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletBalanceDetailDao;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletConsumeDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletTerminalDao;
import com.rfchina.wallet.domain.misc.EnumDef.BalanceDetailStatus;
import com.rfchina.wallet.domain.misc.EnumDef.BizValidateType;
import com.rfchina.wallet.domain.misc.EnumDef.DirtyType;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletBalanceDetail;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectInfo;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletCollectMethod.WalletCollectMethodBuilder;
import com.rfchina.wallet.domain.model.WalletConsume;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import com.rfchina.wallet.domain.model.WalletTerminal;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.bank.yunst.response.SmsPayResp;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstQueryBalanceResult;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
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
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Pos;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.WalletPayMethodBuilder;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Wechat;
import com.rfchina.wallet.server.model.ext.DeductionReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceFreezeMode;
import com.rfchina.wallet.server.msic.EnumWallet.BudgetMode;
import com.rfchina.wallet.server.msic.EnumWallet.CardPro;
import com.rfchina.wallet.server.msic.EnumWallet.ChannelType;
import com.rfchina.wallet.server.msic.EnumWallet.ChargingType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectRoleType;
import com.rfchina.wallet.server.msic.EnumWallet.FeeConfigKey;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumYunst.EnumTerminalStatus;
import com.rfchina.wallet.server.msic.LockConstant;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeniorPayService {

	public static final String PREFIX_REFUND = "WB";
	public static final String PREFIX_AGENT_PAY = "WO";
	public static final String PREFIX_COLLECT = "WC";
	public static final String PREFIX_RECHARGE = "WR";
	public static final String PREFIX_WITHDRAW = "WW";
	public static final String PREFIX_DEDUCTION = "WD";

	public static final String INDUSTRY_CODE = "1910";
	public static final String INDUSTRY_NAME = "其他";

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private WalletDao walletDao;

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
	private WalletConsumeDao walletConsumeDao;

	@Autowired
	private SimpleExclusiveLock lock;

	@Autowired
	private SessionThreadLocal sessionThreadLocal;

	@Autowired
	private WalletBalanceDetailDao walletBalanceDetailDao;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private WalletBalanceDetailService walletBalanceDetailService;

	@Autowired
	@Qualifier(value = "feeMap")
	private CacheHashMap<String, ChargingConfig> feeMap;

	@Autowired
	private WalletTerminalDao walletTerminalDao;

	/**
	 * 充值
	 */
	public RechargeResp recharge(Long walletId, WalletCard walletCard, Long amount) {

		// 检查钱包
		Wallet payerWallet = verifyService.checkSeniorWallet(walletId);
		// 检查银行卡状态
		verifyService.checkCard(walletCard, payerWallet);

		// 工单记录
		String batchNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + IdGenerator.PREFIX_WALLET, 20, id -> {
				return walletOrderDao.selectCountByBatchNo(id) == 0;
			});
		String orderNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + PREFIX_RECHARGE, 19, id -> {
				return walletOrderDao.selectCountByOrderNo(id) == 0;
			});

		// 支付方式
		BankCard bankCard = new BankCard();
		bankCard.setBankCardNo(walletCard.getBankAccount());
		bankCard.setAmount(amount);
		bankCard.setPayType(CollectPayType.BANKCARD.getValue());
		WalletPayMethod payMethod = new WalletPayMethod();
		payMethod.setBankCard(bankCard);

		try {
			lock.acquireLock(LockConstant.LOCK_PAY_ORDER + orderNo, 5, 0, 1000);

			BigDecimal rate = payMethod.getRate(configService);
			BigDecimal tunnelFee = new BigDecimal(amount)
				.multiply(rate)
				.setScale(0, EBankHandler.getRoundingMode());
			WalletOrder rechargeOrder = WalletOrder.builder()
				.orderNo(orderNo)
				.batchNo(batchNo)
				.bizNo(IdGenerator.createBizId(PREFIX_RECHARGE, 19, (id) -> true))
				.walletId(walletId)
				.type(OrderType.RECHARGE.getValue())
				.payMethod(payMethod.getMethods())
				.amount(amount)
				.expireTime(getDefExpireTime(null))
				.progress(GwProgress.WAIT_SEND.getValue())
				.status(OrderStatus.WAITTING.getValue())
				.tunnelType(TunnelType.YUNST.getValue())
				.tunnelFee(tunnelFee.longValue())
				.chargingType(ChargingType.RATE.getValue())
				.chargingValue(rate)
				.note(null)
				.sourceAppId(sessionThreadLocal.getApp().getId())
				.industryCode(INDUSTRY_CODE)
				.industryName(INDUSTRY_NAME)
				.goodName("钱包充值")
				.goodDesc(null)
				.createTime(new Date())
				.build();
			walletOrderDao.insertSelective(rechargeOrder);

			// 充值记录
			WalletRecharge recharge = WalletRecharge.builder()
				.orderId(rechargeOrder.getId())
				.validateType(BizValidateType.SMS.getValue())
				.createTime(new Date())
				.build();
			walletRechargeDao.insertSelective(recharge);
			savePayMethod(recharge.getOrderId(), recharge.getId(), OrderType.RECHARGE.getValue(),
				payMethod, null);

			// 余额明细
			WalletBalanceDetail withdrawDetail = WalletBalanceDetail.builder()
				.walletId(rechargeOrder.getWalletId())
				.orderId(rechargeOrder.getId())
				.orderNo(rechargeOrder.getOrderNo())
				.orderDetailId(recharge.getId())
				.type(OrderType.RECHARGE.getValue())
				.status(BalanceDetailStatus.WAITTING.getValue())
				.amount(rechargeOrder.getAmount())
				.balance(rechargeOrder.getAmount())
				.freezen(0L)
				.createTime(new Date())
				.build();
			walletBalanceDetailDao.insertSelective(withdrawDetail);

			// 支付人
			WalletTunnel payer = walletTunnelDao
				.selectByWalletId(walletId, rechargeOrder.getTunnelType());

			// 充值
			EBankHandler handler = handlerHelper.selectByTunnelType(rechargeOrder.getTunnelType());
			return handler.recharge(rechargeOrder, recharge, payer);
		} finally {
			lock.unLock(LockConstant.LOCK_PAY_ORDER + orderNo);
		}
	}

	public WithdrawResp balanceWithdraw(Long walletId, WalletCard walletCard, Long amount,
		Byte validateType, String jumpUrl, String customerIp, BalanceFreezeMode mode,
		boolean isException) {

		List<Tuple<WalletBalanceDetail, Long>> payDetails = walletBalanceDetailService
			.selectDetailToPay(walletId, amount, isException);
		// 提现
		WithdrawResp order = doWithdraw(walletId, walletCard, amount, validateType, jumpUrl,
			customerIp);
		if (BalanceFreezeMode.FREEZEN.getValue().byteValue() == mode.getValue()) {
			// 锁定余额明细
			Optional<String> orderNos = payDetails.stream().map(payDetail -> {

				WalletBalanceDetail withdrawDetail = walletBalanceDetailService
					.consumePayDetail(order,
						order.getWithdrawId(), payDetail.left, payDetail.right, mode);
				return withdrawDetail.getOrderNo();

			}).reduce((x, y) -> x + "," + y);

			log.info("发起余额出金  出金单号 {} , 入金单号 {}", order.getOrderNo(),
				orderNos.orElse(""));
		}

		return order;
	}


	/**
	 * 提现
	 */
	public WithdrawResp doWithdraw(Long walletId, WalletCard walletCard, Long amount,
		Byte validateType, String jumpUrl, String customerIp) {

		// 检查钱包
		Wallet payerWallet = verifyService.checkSeniorWallet(walletId);
		if (payerWallet.getWalletBalance().longValue() < amount) {
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_AMOUNT_NOT_ENOUGH);
		}
		// 检查银行卡状态
		verifyService.checkCard(walletCard, payerWallet);

		// 工单记录
		String batchNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + IdGenerator.PREFIX_WALLET, 20, id -> {
				return walletOrderDao.selectCountByBatchNo(id) == 0;
			});
		String orderNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + PREFIX_WITHDRAW, 19, id -> {
				return walletOrderDao.selectCountByOrderNo(id) == 0;
			});

		try {
			lock.acquireLock(LockConstant.LOCK_PAY_ORDER + orderNo, 5, 0, 1000);

			ChargingConfig config = feeMap.get(FeeConfigKey.YUNST_WITHDRAW.getValue());
			WalletOrder withdrawOrder = WalletOrder.builder()
				.orderNo(orderNo)
				.batchNo(batchNo)
				.bizNo(IdGenerator.createBizId(PREFIX_WITHDRAW, 19, (id) -> true))
				.walletId(walletId)
				.type(OrderType.WITHDRAWAL.getValue())
				.payMethod(ChannelType.BANKCARD.getValue())
				.amount(amount)
				.progress(GwProgress.WAIT_SEND.getValue())
				.status(OrderStatus.WAITTING.getValue())
				.tunnelType(TunnelType.YUNST.getValue())
				.chargingType(config != null ? config.getType().byteValue() : null)
				.chargingValue(config != null ? config.getChargingValue() : null)
				.tunnelFee(config != null ? config.getChargingValue().longValue() : null)
				.note("钱包提现")
				.expireTime(getDefExpireTime(null))
				.sourceAppId(sessionThreadLocal.getApp().getId())
				.industryCode(INDUSTRY_CODE)
				.industryName(INDUSTRY_NAME)
				.createTime(new Date())
				.build();
			walletOrderDao.insertSelective(withdrawOrder);

			// 提现记录
			WalletWithdraw withdraw = WalletWithdraw.builder()
				.orderId(withdrawOrder.getId())
				.cardId(walletCard.getId())
				.cardPro(walletCard.getIsPublic().intValue() == 1 ? CardPro.COMPANY.getValue()
					: CardPro.PERSON.getValue())
				.bankAccount(walletCard.getBankAccount())
				.validateType(validateType)
				.createTime(new Date())
				.build();
			walletWithdrawDao.insertSelective(withdraw);

			// 提现人
			WalletTunnel payer = walletTunnelDao
				.selectByWalletId(withdrawOrder.getWalletId(), withdrawOrder.getTunnelType());
			// 提现
			EBankHandler handler = handlerHelper.selectByTunnelType(withdrawOrder.getTunnelType());
			WithdrawResp result = handler.withdraw(withdrawOrder, withdraw, payer);

			// 签名密码验证参数
			if (validateType.byteValue() == BizValidateType.PASSWORD.getValue().byteValue()) {
				String signedParams = ((YunstBizHandler) handler)
					.pwdGwConfirm(withdrawOrder, payer, jumpUrl, customerIp);
				signedParams = configService.getYunstPwdConfirmUrl() + "?" + signedParams;
				result.setSignedParams(signedParams);
			} else if (validateType.byteValue() == BizValidateType.SMS.getValue().byteValue()) {
				String signedParams = ((YunstBizHandler) handler)
					.smsGwConfirm(withdrawOrder, payer, customerIp);
				signedParams = configService.getYunstSmsConfirmUrl() + "?" + signedParams;
				result.setSignedParams(signedParams);
			}

			result.setWithdrawId(withdraw.getId());
			return result;
		} finally {
			lock.unLock(LockConstant.LOCK_PAY_ORDER + orderNo);
		}
	}

	/**
	 * 预代收
	 */
	public WalletCollectResp collect(CollectReq req, String jumpUrl, String customerIp) {

		// 定义付款人
		Balance balancePay = req.getWalletPayMethod().getBalance();
		Long payerWalletId = (balancePay != null) ? balancePay.getPayerWalletId()
			: configService.getAnonyPayerWalletId();
		Wallet payerWallet = verifyService.checkSeniorWallet(payerWalletId);
		// 检查钱包余额
		if (balancePay != null) {
			if (payerWallet.getWalletBalance().longValue() < balancePay.getAmount()) {
				throw new WalletResponseException(EnumWalletResponseCode.WALLET_AMOUNT_NOT_ENOUGH);
			}
		}

		// 工单记录
		String orderNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + PREFIX_COLLECT, 19, id -> {
				return walletOrderDao.selectCountByOrderNo(id) == 0;
			});
		String batchNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + IdGenerator.PREFIX_WALLET, 20, id -> {
				return walletOrderDao.selectCountByBatchNo(id) == 0;
			});

		try {
			lock.acquireLock(LockConstant.LOCK_PAY_ORDER + orderNo, 5, 0, 1000);
			BigDecimal rate = req.getWalletPayMethod().getRate(configService);
			BigDecimal tunnelFee = new BigDecimal(req.getAmount())
				.multiply(rate)
				.setScale(0, EBankHandler.getRoundingMode());

			WalletOrder collectOrder = WalletOrder.builder()
				.orderNo(orderNo)
				.batchNo(batchNo)
				.bizNo(req.getBizNo())
				.walletId(payerWalletId)
				.type(OrderType.COLLECT.getValue())
				.payMethod(req.getWalletPayMethod().getMethods())
				.amount(req.getAmount())
				.expireTime(getDefExpireTime(req.getExpireTime()))
				.progress(GwProgress.WAIT_SEND.getValue())
				.status(OrderStatus.WAITTING.getValue())
				.tunnelType(TunnelType.YUNST.getValue())
				.tunnelFee(tunnelFee.longValue())
				.chargingType(ChargingType.RATE.getValue())
				.chargingValue(rate)
				.note(req.getNote())
				.sourceAppId(sessionThreadLocal.getApp().getId())
				.industryCode(req.getIndustryCode())
				.industryName(req.getIndustryName())
				.goodName(req.getGoodName())
				.goodDesc(req.getGoodDesc())
				.createTime(new Date())
				.build();
			walletOrderDao.insertSelective(collectOrder);

			// 生成代收单
			Byte validateType =
				req.getValidateType() != null ? req.getValidateType()
					: BizValidateType.SMS.getValue();
			WalletCollect collect = WalletCollect.builder()
				.orderId(collectOrder.getId())
				.agentWalletId(configService.getAgentEntWalletId())
				.refundLimit(req.getAmount())
				.remainTunnelFee(tunnelFee.longValue())
				.validateType(validateType)
				.budgetMode(req.getWalletPayMethod().hasMethod(ChannelType.POS)
					? BudgetMode.ON_AGENTPAY.getValue() : BudgetMode.ON_COLLECT.getValue())
				.createTime(new Date())
				.build();
			walletCollectDao.insertSelective(collect);
			Optional<Long> opt = req.getRecievers().stream()
				.filter(r -> r.getRoleType().byteValue() == CollectRoleType.PROJECTOR.getValue())
				.map(r -> r.getWalletId())
				.findAny();
			savePayMethod(collect.getOrderId(), collect.getId(), OrderType.COLLECT.getValue(),
				req.getWalletPayMethod(), opt.orElse(null));

			// 生成清分记录
			List<WalletCollectInfo> collectInfos = req.getRecievers().stream().map(reciever -> {
				WalletCollectInfo clearInfo = WalletCollectInfo.builder()
					.collectId(collect.getId())
					.roleType(reciever.getRoleType())
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
			// 签名密码验证参数
			if (collect.getValidateType().byteValue() == BizValidateType.PASSWORD.getValue()) {
				String signedParams = ((YunstBizHandler) handler)
					.pwdGwConfirm(collectOrder, payer, jumpUrl, customerIp);
				signedParams = configService.getYunstPwdConfirmUrl() + "?" + signedParams;
				result.setSignedParams(signedParams);
			} else if (collect.getValidateType().byteValue() == BizValidateType.SMS.getValue()) {
				String signedParams = ((YunstBizHandler) handler)
					.smsGwConfirm(collectOrder, payer, customerIp);
				signedParams = configService.getYunstSmsConfirmUrl() + "?" + signedParams;
				result.setSignedParams(signedParams);
			}

			return result;
		} finally {
			lock.unLock(LockConstant.LOCK_PAY_ORDER + orderNo);
		}
	}

	/**
	 * 发起代付（加锁）
	 */
	public SettleResp agentPay(WalletOrder collectOrder, String bizNo, Reciever receiver,
		String note) {

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
		String orderNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + PREFIX_AGENT_PAY, 19, id -> {
				return walletOrderDao.selectCountByOrderNo(id) == 0;
			});
		String batchNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + IdGenerator.PREFIX_WALLET, 20, id -> {
				return walletOrderDao.selectCountByBatchNo(id) == 0;
			});

		try {
			lock.acquireLock(LockConstant.LOCK_PAY_ORDER + orderNo, 5, 0, 1000);

			WalletOrder clearOrder = WalletOrder.builder()
				.orderNo(orderNo)
				.batchNo(batchNo)
				.bizNo(bizNo)
				.walletId(receiver.getWalletId())
				.type(OrderType.AGENT_PAY.getValue())
				.amount(receiver.getAmount())
				.progress(GwProgress.WAIT_SEND.getValue())
				.status(OrderStatus.WAITTING.getValue())
				.tunnelType(TunnelType.YUNST.getValue())
				.note(note)
				.sourceAppId(sessionThreadLocal.getApp().getId())
				.createTime(new Date())
				.build();
			walletOrderDao.insertSelective(clearOrder);

			// 代付明细
			WalletClearing clearing = WalletClearing.builder()
				.orderId(clearOrder.getId())
				.collectOrderNo(collectOrder.getOrderNo())
				.collectInfoId(collectInfo.getId())
				.agentWalletId(configService.getAgentEntWalletId())
				.amount(receiver.getAmount())
				.createTime(new Date())
				.build();
			walletClearingDao.insertSelective(clearing);

			// 余额明细
			WalletBalanceDetail withdrawDetail = WalletBalanceDetail.builder()
				.walletId(clearOrder.getWalletId())
				.orderId(clearOrder.getId())
				.orderNo(clearOrder.getOrderNo())
				.orderDetailId(clearing.getId())
				.type(OrderType.AGENT_PAY.getValue())
				.status(BalanceDetailStatus.WAITTING.getValue())
				.amount(clearOrder.getAmount())
				.balance(clearOrder.getAmount())
				.freezen(0L)
				.createTime(new Date())
				.build();
			walletBalanceDetailDao.insertSelective(withdrawDetail);

			// 代付给每个收款人
			EBankHandler handler = handlerHelper.selectByTunnelType(clearOrder.getTunnelType());
			handler.agentPay(clearOrder, clearing, walletCollect, collectInfos);

			return SettleResp.builder()
				.order(clearOrder)
				.clearing(clearing)
				.build();
		} finally {
			lock.unLock(LockConstant.LOCK_PAY_ORDER + orderNo);
		}

	}

	/**
	 * 退款
	 */
	public WalletOrder refund(WalletOrder collectOrder, String bizNo, List<RefundInfo> refundList,
		String note) {

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

				// 分帐金额不能小于退款金额+已退金额+已清金额
				if (collectInfo.getBudgetAmount() < collectInfo.getRefundAmount()
					+ collectInfo.getClearAmount() + r.getAmount()) {
					throw new WalletResponseException(
						EnumWalletResponseCode.REFUND_AMOUNT_OVER_LIMIT);
				}
				return collectInfo;
			}).collect(Collectors.toMap(c -> c.getPayeeWalletId().toString(), c -> c));
		// 手续费退还
		Long refundAmount = refundList.stream()
			.collect(Collectors.summingLong(RefundInfo::getAmount));
		BigDecimal tunnelFee = BigDecimal.ZERO;
		BigDecimal rate = BigDecimal.ZERO;
		try {
			List<WalletCollectMethod> methods = walletCollectMethodDao
				.selectByCollectId(walletCollect.getId(), OrderType.COLLECT.getValue());
			WalletPayMethod payMethod = getPayMethod(methods.get(0));
			rate = payMethod.getRate(configService);
			tunnelFee = new BigDecimal(walletCollect.getRefundLimit() - refundAmount)
				.multiply(rate)
				.setScale(0, EBankHandler.getRoundingMode());

		} catch (Exception e) {
			log.error("手续费错误", e);
		}

		// 工单记录
		String orderNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + PREFIX_REFUND, 19, id -> {
				return walletOrderDao.selectCountByOrderNo(id) == 0;
			});
		String batchNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + IdGenerator.PREFIX_WALLET, 20, id -> {
				return walletOrderDao.selectCountByBatchNo(id) == 0;
			});

		try {
			lock.acquireLock(LockConstant.LOCK_PAY_ORDER + orderNo, 5, 0, 1000);

			WalletOrder refundOrder = WalletOrder.builder()
				.orderNo(orderNo)
				.batchNo(batchNo)
				.bizNo(bizNo)
				.walletId(collectOrder.getWalletId())
				.type(OrderType.REFUND.getValue())
				.amount(refundAmount)
				.progress(GwProgress.WAIT_SEND.getValue())
				.status(OrderStatus.WAITTING.getValue())
				.tunnelType(TunnelType.YUNST.getValue())
				.tunnelFee(tunnelFee.longValue() - walletCollect.getRemainTunnelFee())
				.sourceAppId(sessionThreadLocal.getApp().getId())
				.chargingType(ChargingType.RATE.getValue())
				.chargingValue(rate)
				.note(note)
				.createTime(new Date())
				.build();
			walletOrderDao.insertSelective(refundOrder);

			walletCollect.setRefundLimit(walletCollect.getRefundLimit() - refundAmount);
			walletCollect.setRemainTunnelFee(tunnelFee.longValue());
			walletCollectDao.updateByPrimaryKeySelective(walletCollect);

			// 记录退款单
			WalletRefund refund = WalletRefund.builder()
				.orderId(refundOrder.getId())
				.collectOrderId(collectOrder.getId())
				.collectOrderNo(collectOrder.getOrderNo())
				.agentWalletId(configService.getAgentEntWalletId())
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
		} finally {
			lock.unLock(LockConstant.LOCK_PAY_ORDER + orderNo);
		}
	}


	/**
	 * 预代收
	 */
	public WalletCollectResp deduction(DeductionReq req) {

		// 定义付款人
		Long payerWalletId = req.getWalletPayMethod().getBalance().getPayerWalletId();
		verifyService.checkSeniorWallet(payerWalletId);

		// 工单记录
		String orderNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + PREFIX_DEDUCTION, 19, id -> {
				return walletOrderDao.selectCountByOrderNo(id) == 0;
			});
		String batchNo = IdGenerator
			.createBizId(configService.getOrderNoPrefix() + IdGenerator.PREFIX_WALLET, 20, id -> {
				return walletOrderDao.selectCountByBatchNo(id) == 0;
			});

		try {
			lock.acquireLock(LockConstant.LOCK_PAY_ORDER + orderNo, 5, 0, 1000);

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
				.tunnelFee(0L)
				.industryCode(req.getIndustryCode())
				.industryName(req.getIndustryName())
				.note(req.getNote())
				.expireTime(getDefExpireTime(null))
				.sourceAppId(sessionThreadLocal.getApp().getId())
				.createTime(new Date())
				.build();
			walletOrderDao.insertSelective(consumeOrder);

			// 生成代收单
			WalletConsume consume = WalletConsume.builder()
				.orderId(consumeOrder.getId())
				.payeeWalletId(configService.getAgentEntWalletId())
				.validateType(BizValidateType.NONE.getValue())
				.createTime(new Date())
				.build();
			walletConsumeDao.insertSelective(consume);

			WalletCollectMethod method = savePayMethod(consume.getOrderId(), consume.getId(),
				OrderType.DEDUCTION.getValue(), req.getWalletPayMethod(), null);

			WalletTunnel payer = walletTunnelDao
				.selectByWalletId(consumeOrder.getWalletId(), consumeOrder.getTunnelType());
			WalletTunnel payee = walletTunnelDao
				.selectByWalletId(consume.getPayeeWalletId(), consumeOrder.getTunnelType());

			EBankHandler handler = handlerHelper.selectByTunnelType(consumeOrder.getTunnelType());
			WalletCollectResp result = handler.consume(consumeOrder, consume, payer, payee,
				Arrays.asList(method));

			// 余额明细
			List<Tuple<WalletBalanceDetail, Long>> details = walletBalanceDetailService.
				selectDetailToPay(consumeOrder.getWalletId(), consumeOrder.getAmount(), false);
			details.forEach(payDetail -> {
				WalletBalanceDetail payerDetail = walletBalanceDetailService.consumePayDetail(
					consumeOrder, consume.getId(), payDetail.left, payDetail.right,
					BalanceFreezeMode.FREEZEN);
				WalletBalanceDetail payeeDetail = BeanUtil
					.newInstance(payerDetail, WalletBalanceDetail.class);
				payeeDetail.setId(null);
				payeeDetail.setWalletId(consume.getPayeeWalletId());
				payeeDetail.setAmount(payDetail.right);
				payeeDetail.setBalance(payDetail.right);
				walletBalanceDetailDao.insertSelective(payerDetail);
			});

			return result;
		} finally {
			lock.unLock(LockConstant.LOCK_PAY_ORDER + orderNo);
		}
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
	private WalletCollectMethod savePayMethod(Long orderId, Long collectId, Byte type,
		WalletPayMethod payMethod, Long walletId) {
		// 支付方式
		WalletCollectMethodBuilder builder = WalletCollectMethod.builder()
			.refId(collectId)
			.orderId(orderId)
			.type(type);
		if (payMethod.getBalance() != null) {
			Balance balance = payMethod.getBalance();
			builder.channelType(ChannelType.BALANCE.getValue())
				.payType(CollectPayType.BALANCE.getValue())
				.amount(balance.getAmount());
		} else if (payMethod.getWechat() != null) {
			Wechat wechat = payMethod.getWechat();
			builder.channelType(ChannelType.WECHAT.getValue())
				.payType(wechat.getPayType())
				.amount(wechat.getAmount())
				.openId(wechat.getOpenId())
				.cusIp(wechat.getCusip())
				.appId(wechat.getSubAppId())
				.sceneInfo(wechat.getSceneInfo())
				.sellerId(wechat.getVspCusid());
		} else if (payMethod.getAlipay() != null) {
			Alipay alipay = payMethod.getAlipay();
			builder.channelType(ChannelType.ALIPAY.getValue())
				.payType(alipay.getPayType())
				.amount(alipay.getAmount())
				.openId(alipay.getUserId())
				.sellerId(alipay.getVspCusid());
		} else if (payMethod.getCodePay() != null) {
			CodePay codePay = payMethod.getCodePay();
			builder.channelType(ChannelType.CODEPAY.getValue())
				.payType(codePay.getPayType())
				.amount(codePay.getAmount())
				.sceneInfo(codePay.getAuthcode())
				.sellerId(codePay.getVspCusid());
		} else if (payMethod.getBankCard() != null) {
			BankCard bankCard = payMethod.getBankCard();
			builder.channelType(ChannelType.BANKCARD.getValue())
				.payType(CollectPayType.BANKCARD.getValue())
				.amount(bankCard.getAmount())
				.openId(bankCard.getBankCardNo())
				.cardType(bankCard.getCardType());
		} else if (payMethod.getPos() != null) {
			Pos pos = payMethod.getPos();
			builder.channelType(ChannelType.POS.getValue())
				.payType(CollectPayType.POS.getValue())
				.amount(pos.getAmount());
			// 珏衡需求： sellerId本地表查询
			WalletTerminal walletTerminal = walletTerminalDao
				.selectByWalletId(walletId, EnumTerminalStatus.BIND.getValue());
			Optional.ofNullable(walletTerminal)
				.ifPresent(t -> builder.sellerId(t.getVspCusid()));
		}
		WalletCollectMethod method = builder.build();
		walletCollectMethodDao.insertSelective(method);
		return method;
	}

	/**
	 * 获取支付方式
	 */
	private WalletPayMethod getPayMethod(WalletCollectMethod collectMethod) {
		WalletPayMethodBuilder builder = WalletPayMethod.builder();
		// 支付方式
		if (ChannelType.BALANCE.getValue().equals(collectMethod.getChannelType())) {
			Balance balance = Balance.builder()
				.amount(collectMethod.getAmount())
				.build();
			builder.balance(balance);
		} else if (ChannelType.WECHAT.getValue().equals(collectMethod.getChannelType())) {
			Wechat wechat = Wechat.builder()
				.payType(collectMethod.getPayType())
				.amount(collectMethod.getAmount())
				.openId(collectMethod.getOpenId())
				.cusip(collectMethod.getCusIp())
				.subAppId(collectMethod.getAppId())
				.sceneInfo(collectMethod.getSceneInfo())
				.vspCusid(collectMethod.getSellerId())
				.build();
			builder.wechat(wechat);
		} else if (ChannelType.ALIPAY.getValue().equals(collectMethod.getChannelType())) {
			Alipay alipay = Alipay.builder()
				.payType(collectMethod.getPayType())
				.amount(collectMethod.getAmount())
				.userId(collectMethod.getOpenId())
				.vspCusid(collectMethod.getSellerId())
				.build();
			builder.alipay(alipay);
		} else if (ChannelType.CODEPAY.getValue().equals(collectMethod.getChannelType())) {
			CodePay codePay = CodePay.builder()
				.payType(collectMethod.getPayType())
				.amount(collectMethod.getAmount())
				.authcode(collectMethod.getSceneInfo())
				.vspCusid(collectMethod.getSellerId())
				.build();
			builder.codePay(codePay);
		} else if (ChannelType.BANKCARD.getValue().equals(collectMethod.getChannelType())) {
			BankCard bankCard = BankCard.builder()
				.payType(collectMethod.getPayType())
				.amount(collectMethod.getAmount())
				.bankCardNo(collectMethod.getOpenId())
				.cardType(collectMethod.getCardType())
				.build();
			builder.bankCard(bankCard);
		}

		return builder.build();
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
			SmsPayResp resp = ((YunstBizHandler) handler)
				.smsConfirm(order, tradeNo, verifyCode, ip);
			if ("fail".equalsIgnoreCase(resp.getPayStatus())) {
				throw new WalletResponseException(
					EnumWalletResponseCode.WALLET_SMS_ERROR.getValue(), resp.getPayFailMessage());
			}
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
	public WalletOrder updateOrderStatusWithMq(String orderNo, boolean incQuery) {
		WalletOrder walletOrder = updateOrderStatus(orderNo, incQuery);
		return Optional.ofNullable(walletOrder)
			.filter(order -> OrderStatus.WAITTING.getValue().byteValue() != order.getStatus()
				.byteValue())
			.orElse(null);
	}


	public WalletOrder updateOrderStatus(String orderNo, boolean incQuery) {
		WalletOrder order = verifyService.checkOrder(orderNo, OrderStatus.WAITTING.getValue());
		EBankHandler handler = handlerHelper.selectByTunnelType(order.getTunnelType());
		try {
			lock.acquireLock(LockConstant.LOCK_PAY_ORDER + order.getOrderNo(), 5, 0, 1000);
			if (incQuery) {
				order.setCurrTryTimes(order.getCurrTryTimes() + 1);
			}
			List<Triple<WalletOrder, WalletFinance, GatewayTrans>> triples = handler
				.updateOrderStatus(Arrays.asList(order));

			WalletOrder rs = triples.get(0).x;
			if (OrderStatus.SUCC.getValue().byteValue() == rs.getStatus().byteValue()) {
				WalletTunnel walletTunnel = walletTunnelDao
					.selectByWalletId(order.getWalletId(), order.getTunnelType());
				syncTunnelAmount(walletTunnel);
				if (OrderType.DEDUCTION.getValue().byteValue() == order.getType().byteValue()) {
					WalletConsume walletConsume = walletConsumeDao.selectByOrderId(order.getId());
					WalletTunnel payeeTunnel = walletTunnelDao
						.selectByWalletId(walletConsume.getPayeeWalletId(), order.getTunnelType());
					Optional.ofNullable(payeeTunnel).ifPresent(t -> syncTunnelAmount(t));
				}
			}
			return rs;
		} finally {
			lock.unLock(LockConstant.LOCK_PAY_ORDER + order.getOrderNo());
		}
	}

	public Wallet syncTunnelAmount(WalletTunnel walletTunnel) {
		log.info("[余额同步] 通道记录[{}]更新余额", walletTunnel.getId());
		// 通联查余额
		YunstQueryBalanceResult result = yunstUserHandler
			.queryBalance(walletTunnel.getBizUserId());
		// 更新通道余额
		walletTunnel.setBalance(result.getAllAmount());
		walletTunnel.setFreezenAmount(result.getFreezenAmount());
		walletTunnel.setIsDirty(DirtyType.NORMAL.getValue());
		walletTunnelDao.updateByPrimaryKeySelective(walletTunnel);
		log.info("[余额同步] 通道[{}]更新余额", walletTunnel.getId());

		// 更新钱包余额
		Wallet wallet = walletDao.selectByPrimaryKey(walletTunnel.getWalletId());
		wallet.setWalletBalance(walletTunnel.getBalance());
		wallet.setFreezeAmount(walletTunnel.getFreezenAmount());
		wallet.setBalanceUpdTime(new Date());
		walletDao.updateByPrimaryKeySelective(wallet);
		log.info("[余额同步] 钱包[{}]更新余额", wallet.getId());
		return wallet;
	}


	/**
	 * 计算超时时间
	 */
	private Date getDefExpireTime(Date expire) {

		return Optional.ofNullable(expire)
			.orElseGet(() -> {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.MINUTE, 60);
				return calendar.getTime();
			});
	}
}
