package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.rfchina.platform.biztools.fileserver.HttpFile;
import com.rfchina.platform.common.misc.Triple;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.MoneyLogMapper;
import com.rfchina.wallet.domain.mapper.ext.WalletBalanceDetailDao;
import com.rfchina.wallet.domain.misc.EnumDef.BalanceDetailStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderSubStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.MoneyLog;
import com.rfchina.wallet.domain.model.MoneyLog.MoneyLogBuilder;
import com.rfchina.wallet.domain.model.WalletBalanceDetail;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectInfo;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletConsume;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.exception.UnknownException;
import com.rfchina.wallet.server.bank.yunst.request.AgentPayReq;
import com.rfchina.wallet.server.bank.yunst.request.AgentPayReq.CollectPay;
import com.rfchina.wallet.server.bank.yunst.request.AgentPayReq.SplitRule;
import com.rfchina.wallet.server.bank.yunst.request.CardBinReq;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Alipay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Alipay.AlipayService;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Alipay.ScanAlipay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Balance;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.BankCard.CardQuickPay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.CodePay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.CodePay.CodePayVsp;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Pos;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.AppOpen;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.H5Open;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.MiniProgram;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.ScanWeixin;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.WechatPublic;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.RecieveInfo;
import com.rfchina.wallet.server.bank.yunst.request.ConsumeApplyReq;
import com.rfchina.wallet.server.bank.yunst.request.DepositApplyReq;
import com.rfchina.wallet.server.bank.yunst.request.GetCheckAccountFileReq;
import com.rfchina.wallet.server.bank.yunst.request.GetOrderDetailReq;
import com.rfchina.wallet.server.bank.yunst.request.PwdConfirmReq;
import com.rfchina.wallet.server.bank.yunst.request.RefundApplyReq;
import com.rfchina.wallet.server.bank.yunst.request.RefundApplyReq.RefundInfo;
import com.rfchina.wallet.server.bank.yunst.request.SmsGwPayReq;
import com.rfchina.wallet.server.bank.yunst.request.SmsPayReq;
import com.rfchina.wallet.server.bank.yunst.request.SmsRetryReq;
import com.rfchina.wallet.server.bank.yunst.request.WithdrawApplyReq;
import com.rfchina.wallet.server.bank.yunst.response.AgentPayResp;
import com.rfchina.wallet.server.bank.yunst.response.CardBinResp;
import com.rfchina.wallet.server.bank.yunst.response.CollectApplyResp;
import com.rfchina.wallet.server.bank.yunst.response.GetCheckAccountFileResp;
import com.rfchina.wallet.server.bank.yunst.response.GetOrderDetailResp;
import com.rfchina.wallet.server.bank.yunst.response.RefundApplyResp;
import com.rfchina.wallet.server.bank.yunst.response.SmsPayResp;
import com.rfchina.wallet.server.bank.yunst.response.SmsRetryResp;
import com.rfchina.wallet.server.bank.yunst.response.WithdrawApplyResp;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletWithdrawExtDao;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceFreezeMode;
import com.rfchina.wallet.server.msic.EnumWallet.BudgetMode;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectRoleType;
import com.rfchina.wallet.server.msic.EnumWallet.DebitType;
import com.rfchina.wallet.server.msic.EnumWallet.EnumBizTag;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumWallet.RefundType;
import com.rfchina.wallet.server.msic.EnumWallet.UniProgress;
import com.rfchina.wallet.server.msic.EnumYunst.EnumYunstDeviceType;
import com.rfchina.wallet.server.msic.EnumYunst.EnumYunstWithdrawType;
import com.rfchina.wallet.server.msic.EnumYunst.YunstFileType;
import com.rfchina.wallet.server.msic.EnumYunst.YunstOrderStatus;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.GatewayTransService;
import com.rfchina.wallet.server.service.WalletBalanceDetailService;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class YunstBizHandler extends EBankHandler {

	public static final String TRADE_CODESTRING_COLLECT = "3001";
	public static final String TRADE_CODESTRING_AGENTPAY = "4001";


	@Autowired
	private YunstTpl yunstTpl;

	@Autowired
	private GatewayTransService gatewayTransService;

	@Autowired
	private WalletTunnelExtDao walletChannelDao;

	@Autowired
	private WalletCollectExtDao walletCollectDao;

	@Autowired
	private WalletCollectInfoExtDao walletCollectInfoDao;

	@Autowired
	private WalletCollectMethodExtDao walletCollectMethodDao;

	@Autowired
	private WalletTunnelExtDao walletTunnelExtDao;

	@Autowired
	private WalletClearingExtDao walletClearingDao;

	@Autowired
	private WalletRefundExtDao walletRefundDao;

	@Autowired
	private WalletRefundDetailExtDao walletRefundDetailDao;

	@Autowired
	private WalletRechargeExtDao walletRechargeDao;

	@Autowired
	private WalletWithdrawExtDao walletWithdrawDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private MoneyLogMapper moneyLogDao;

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private WalletBalanceDetailDao walletBalanceDetailDao;

	@Autowired
	private WalletBalanceDetailService walletBalanceDetailService;


	public boolean isSupportTunnelType(Byte tunnelType) {
		return TunnelType.YUNST.getValue().byteValue() == tunnelType.byteValue();
	}


	public GatewayMethod getGatewayMethod() {
		return null;
	}

	@Override
	public List<Triple<WalletOrder, WalletFinance, GatewayTrans>> updateOrderStatus(
		List<WalletOrder> orders) {

		return orders.stream()
			.map(order -> {
				try {
					WalletOrder walletOrder = updateOrderStatus(order);
					return new Triple<WalletOrder, WalletFinance, GatewayTrans>(walletOrder, null,
						null);
				} catch (Exception e) {
					dealUndefinedError(order, e);
					throw e;
				}
			})
			.collect(Collectors.toList());
	}


	/**
	 * 充值
	 */
	public RechargeResp recharge(WalletOrder order, WalletRecharge recharge,
		WalletTunnel payer) {

		List<WalletCollectMethod> methods = walletCollectMethodDao
			.selectByCollectId(recharge.getId(), OrderType.RECHARGE.getValue());

		// 超时时间
		String expireTime = order.getExpireTime() != null ? DateUtil
			.formatDate(order.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
		DepositApplyReq req = DepositApplyReq.builder()
			.bizOrderNo(order.getOrderNo())
			.bizUserId(payer.getBizUserId())
			.accountSetNo(configService.getUserAccSet())
			.amount(order.getAmount())
			.fee(0L)
			.validateType(Long.valueOf(recharge.getValidateType()))
			.frontUrl(null)
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.orderExpireDatetime(expireTime)
			.payMethod(getMethodMap(methods, true))
			.industryCode(order.getIndustryCode())
			.industryName(order.getIndustryName())
			.goodsName(order.getGoodName())
			.summary(order.getNote())
			.source(EnumYunstDeviceType.MOBILE.getValue())
			.build();
		// 发起时间
		order.setStartTime(new Date());
		order.setProgress(UniProgress.SENDED.getValue());
		try {
			CollectApplyResp resp = yunstTpl.execute(req, CollectApplyResp.class);
			// 订单号
			order.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				order.setStatus(OrderStatus.FAIL.getValue());
				order.setTunnelErrCode(resp.getPayCode());
				order.setTunnelErrMsg(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(order);
			RechargeResp rechargeResp = BeanUtil.newInstance(order, RechargeResp.class);
			rechargeResp.setPayInfo(resp.getPayInfo());
			rechargeResp.setWeChatAPPInfo(resp.getWeChatAPPInfo());
			rechargeResp.setExtendInfo(resp.getExtendInfo());
			rechargeResp.setTradeNo(resp.getTradeNo());
			rechargeResp.setSmsConfirm(
				(resp.getValidateType() != null && resp.getValidateType() == 1) ? true : false);
			rechargeResp.setPasswordConfirm(false); // 收银宝不支持密码验证
			return rechargeResp;
		} catch (CommonGatewayException e) {
			dealGatewayError(order, e);
		} catch (Exception e) {
			dealUndefinedError(order, e);
		}
		return null;
	}


	/**
	 * 提现
	 */
	public WithdrawResp withdraw(WalletOrder order, WalletWithdraw withdraw, WalletTunnel payer) {

		String expireTime = order.getExpireTime() != null ? DateUtil
			.formatDate(order.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
		String bankAccount = null;
		try {
			bankAccount = RSAUtil.encrypt(withdraw.getBankAccount());
		} catch (Exception e) {
			log.error("银行卡加密失败", e);
		}
		WithdrawApplyReq req = WithdrawApplyReq.builder()
			.bizOrderNo(order.getOrderNo())
			.bizUserId(payer.getBizUserId())
			.accountSetNo(configService.getUserAccSet())
			.amount(order.getAmount())
			.fee(0L)
			.validateType(Long.valueOf(withdraw.getValidateType()))
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.orderExpireDatetime(expireTime)
			.payMethod(null)
			.bankCardNo(bankAccount)
			.bankCardPro(withdraw.getCardPro().longValue())
			.withdrawType(EnumYunstWithdrawType.D0.getValue())
			.industryCode(order.getIndustryCode())
			.industryName(order.getIndustryName())
			.summary(order.getOrderNo())    // 海荣需求，提现传单号到银行流水
			.source(EnumYunstDeviceType.MOBILE.getValue())
			.build();

		order.setStartTime(new Date());
		order.setProgress(UniProgress.SENDED.getValue());
		try {
			WithdrawApplyResp resp = yunstTpl.execute(req, WithdrawApplyResp.class);
			order.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				order.setStatus(OrderStatus.FAIL.getValue());
				order.setTunnelErrMsg(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(order);

			WithdrawResp result = BeanUtil.newInstance(order, WithdrawResp.class);
			result.setBizUserId(payer.getBizUserId());
			return result;
		} catch (CommonGatewayException e) {
			dealGatewayError(order, e);
		} catch (Exception e) {
			dealUndefinedError(order, e);
		}
		return null;
	}


	private List<RecieveInfo> getRecievers(Byte budgetMode, Long amount, Byte tunnelType,
		List<WalletCollectInfo> clearInfos) {
		if (budgetMode == null || budgetMode.byteValue() == BudgetMode.ON_COLLECT.getValue()) {

			log.info("代收分佣 {}", clearInfos);
			return clearInfos.stream().map(info -> {
				WalletTunnel receiver = walletChannelDao
					.selectByWalletId(info.getPayeeWalletId(), tunnelType);
				return RecieveInfo.builder()
					.bizUserId(receiver.getBizUserId())
					.amount(info.getBudgetAmount())
					.build();
			}).collect(Collectors.toList());
		} else if (budgetMode.byteValue() == BudgetMode.ON_AGENTPAY.getValue()) {

			Optional<WalletCollectInfo> opts = clearInfos.stream().filter(
				info -> CollectRoleType.PROJECTOR.getValue().byteValue() == info.getRoleType())
				.findFirst();
			opts.orElseThrow(() -> new WalletResponseException(
				EnumWalletResponseCode.WALLET_PROJECTOR_IS_EMPTY));

			log.info("代付抽佣 主收款人[{}]", opts.get().getPayeeWalletId());
			WalletTunnel receiver = walletChannelDao
				.selectByWalletId(opts.get().getPayeeWalletId(), tunnelType);
			return Arrays.asList(RecieveInfo.builder()
				.bizUserId(receiver.getBizUserId())
				.amount(amount)
				.build());
		} else if (budgetMode.byteValue() == BudgetMode.SILI_PROXY.getValue()) {

			log.info("思力代分 主收款人[{}]", configService.getAgentPosWalletId());
			WalletTunnel receiver = walletChannelDao
				.selectByWalletId(configService.getAgentPosWalletId(), tunnelType);
			return Arrays.asList(RecieveInfo.builder()
				.bizUserId(receiver.getBizUserId())
				.amount(amount)
				.build());
		} else {
			return null;
		}

	}

	/**
	 * 代收
	 */
	public WalletCollectResp collect(WalletOrder order, WalletCollect collect,
		List<WalletCollectInfo> clearInfos, WalletTunnel payer) {

		// 收款人
		List<RecieveInfo> recievers = getRecievers(collect.getBudgetMode(), order.getAmount(),
			order.getTunnelType(), clearInfos);

		// 收款方式
		List<WalletCollectMethod> methods = walletCollectMethodDao
			.selectByCollectId(collect.getId(), OrderType.COLLECT.getValue());

		String expireTime = order.getExpireTime() != null ? DateUtil
			.formatDate(order.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
		CollectApplyReq req = CollectApplyReq.builder()
			.bizOrderNo(order.getOrderNo())
			.payerId(payer.getBizUserId())
			.recieverList(recievers)
			.amount(order.getAmount())
			.fee(0L)
			.validateType(collect.getValidateType().longValue())
			.frontUrl(null)
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.orderExpireDatetime(expireTime)
			.payMethod(getMethodMap(methods, false))
			.tradeCode(TRADE_CODESTRING_COLLECT)
			.industryCode(order.getIndustryCode())
			.industryName(order.getIndustryName())
			.summary(order.getNote())
			.goodsName(order.getGoodName())
			.goodsDesc(order.getGoodDesc())
			.extendInfo("")
			.source(1L)
			.build();

		order.setProgress(UniProgress.SENDED.getValue());
		order.setStartTime(new Date());
		CollectApplyResp resp = null;
		try {
			resp = yunstTpl.execute(req, CollectApplyResp.class);
			order.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				order.setStatus(OrderStatus.FAIL.getValue());
				order.setTunnelErrCode(resp.getPayCode());
				order.setTunnelErrMsg(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(order);
			WalletCollectResp result = BeanUtil.newInstance(order, WalletCollectResp.class);
			if (resp != null) {
				result.setPayInfo(resp.getPayInfo());
				result.setWeChatAPPInfo(resp.getWeChatAPPInfo());
				result.setTradeNo(resp.getTradeNo());
			}

			return result;
		} catch (CommonGatewayException e) {
			dealGatewayError(order, e);
		} catch (Exception e) {
			dealUndefinedError(order, e);
		}
		return null;
	}

	private Tuple<WalletTunnel, List<SplitRule>> getSplitRule(Byte budgetMode,
		WalletOrder clearOrder, WalletClearing clearing, List<WalletCollectInfo> collectInfos) {

		if (budgetMode == null || budgetMode.byteValue() == BudgetMode.ON_COLLECT.getValue()) {

			WalletTunnel clearTunnel = walletTunnelExtDao
				.selectByWalletId(clearOrder.getWalletId(), clearOrder.getTunnelType());
			return new Tuple(clearTunnel, null);
		} else if (budgetMode.byteValue() == BudgetMode.ON_AGENTPAY.getValue()) {

			Optional<Long> opts = collectInfos.stream()
				.filter(
					info -> info.getRoleType().byteValue() == CollectRoleType.PROJECTOR.getValue())
				.map(WalletCollectInfo::getPayeeWalletId)
				.findFirst();
			opts.orElseThrow(() -> new WalletResponseException(
				EnumWalletResponseCode.WALLET_PROJECTOR_IS_EMPTY));

			WalletTunnel projectTunnel = walletTunnelExtDao
				.selectByWalletId(opts.orElse(0L), clearOrder.getTunnelType());
			WalletTunnel clearTunnel = walletTunnelExtDao
				.selectByWalletId(clearOrder.getWalletId(), clearOrder.getTunnelType());
			if (clearOrder.getWalletId().longValue() != projectTunnel.getWalletId()) {
				List<SplitRule> splitRules = Arrays.asList(SplitRule.builder()
					.bizUserId(clearTunnel.getBizUserId())
					.amount(clearing.getAmount())
					.fee(0L)
					.build()
				);
				return new Tuple<>(projectTunnel, splitRules);
			} else {
				return new Tuple<>(projectTunnel, null);
			}

		} else if (budgetMode.byteValue() == BudgetMode.SILI_PROXY.getValue()) {

			WalletTunnel projectTunnel = walletTunnelExtDao
				.selectByWalletId(configService.getAgentPosWalletId(), clearOrder.getTunnelType());
			WalletTunnel clearTunnel = walletTunnelExtDao
				.selectByWalletId(clearOrder.getWalletId(), clearOrder.getTunnelType());
			List<SplitRule> splitRules = Arrays.asList(SplitRule.builder()
				.bizUserId(clearTunnel.getBizUserId())
				.amount(clearing.getAmount())
				.fee(0L)
				.build()
			);
			return new Tuple<>(projectTunnel, splitRules);
		}
		return null;
	}

	/**
	 * 代付
	 */
	public void agentPay(WalletOrder clearOrder, WalletClearing clearing,
		WalletCollect walletCollect, List<WalletCollectInfo> collectInfos) {

		// 读取分帐规则
		Tuple<WalletTunnel, List<SplitRule>> splitRule = getSplitRule(walletCollect.getBudgetMode(),
			clearOrder, clearing, collectInfos);

		// 登记代付数额
		CollectPay collectPay = CollectPay.builder()
			.bizOrderNo(clearing.getCollectOrderNo())
			.amount(clearing.getAmount())
			.build();
		AgentPayReq req = AgentPayReq.builder()
			.bizOrderNo(clearOrder.getOrderNo())
			.collectPayList(Lists.newArrayList(collectPay))
			.bizUserId(splitRule.left.getBizUserId())
			.accountSetNo(configService.getUserAccSet())  // 产品需求代付到余额账户
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.amount(clearing.getAmount())
			.fee(0L)
			.splitRuleList(splitRule.right)
			.tradeCode(TRADE_CODESTRING_AGENTPAY)
			.summary(clearOrder.getNote())
			.build();

		clearOrder.setProgress(UniProgress.SENDED.getValue());
		clearOrder.setStartTime(new Date());
		try {
			AgentPayResp resp = yunstTpl.execute(req, AgentPayResp.class);
			clearOrder.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				clearOrder.setStatus(OrderStatus.FAIL.getValue());
				clearOrder.setTunnelErrMsg(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(clearOrder);
		} catch (CommonGatewayException e) {
			dealGatewayError(clearOrder, e);
		} catch (Exception e) {
			dealUndefinedError(clearOrder, e);
			return;
		}
	}

	private List<RefundInfo> getRefundList(Byte budgetMode, Byte tunnelType,
		List<WalletRefundDetail> details, List<WalletCollectInfo> collectInfos) {

		if (budgetMode == null || budgetMode.byteValue() == BudgetMode.ON_COLLECT.getValue()) {
			return details.stream().map(detail -> {
				WalletTunnel payeeChannel = walletChannelDao
					.selectByWalletId(detail.getPayeeWalletId(), tunnelType);
				return RefundInfo.builder()
					.accountSetNo(null)   //不送：默认从平台中间账户集退款
					.bizUserId(payeeChannel.getBizUserId())
					.amount(detail.getAmount())
					.build();
			}).collect(Collectors.toList());
		} else if (budgetMode.byteValue() == BudgetMode.ON_AGENTPAY.getValue()) {

			Long projector = findProjector(collectInfos);
			Optional<Long> total = details.stream()
				.map(WalletRefundDetail::getAmount)
				.reduce((x, y) -> x + y);
			return Arrays.asList(RefundInfo.builder()
				.accountSetNo(null)   //不送：默认从平台中间账户集退款
				.bizUserId(String.valueOf(projector))
				.amount(total.orElse(0L))
				.build());
		} else if (budgetMode.byteValue() == BudgetMode.SILI_PROXY.getValue()) {

			Long projector = configService.getAgentPosWalletId();
			Optional<Long> total = details.stream()
				.map(WalletRefundDetail::getAmount)
				.reduce((x, y) -> x + y);
			return Arrays.asList(RefundInfo.builder()
				.accountSetNo(null)   //不送：默认从平台中间账户集退款
				.bizUserId(String.valueOf(projector))
				.amount(total.orElse(0L))
				.build());
		}

		return null;

	}

	private Long findProjector(List<WalletCollectInfo> collectInfos) {
		Optional<Long> opts = collectInfos.stream()
			.filter(
				info -> info.getRoleType().byteValue() == CollectRoleType.PROJECTOR.getValue())
			.map(WalletCollectInfo::getPayeeWalletId)
			.findFirst();
		opts.orElseThrow(() -> new WalletResponseException(
			EnumWalletResponseCode.WALLET_PROJECTOR_IS_EMPTY));

		return opts.get();
	}


	/**
	 * 退款
	 */
	public void refund(WalletOrder refundOrder, WalletRefund refund,
		List<WalletRefundDetail> details, List<WalletCollectInfo> collectInfos) {

		List<RefundInfo> refundList = getRefundList(refund.getBudgetMode(),
			refundOrder.getTunnelType(), details, collectInfos);

		WalletTunnel payerChannel = walletChannelDao
			.selectByWalletId(refundOrder.getWalletId(), refundOrder.getTunnelType());
		RefundApplyReq req = RefundApplyReq.builder()
			.bizOrderNo(refundOrder.getOrderNo())
			.oriBizOrderNo(refund.getCollectOrderNo())
			.bizUserId(payerChannel.getBizUserId())
			.refundType(RefundType.D0.getValue())
			.refundList(refundList)
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.amount(details.stream()
				.collect(Collectors.summingLong(WalletRefundDetail::getAmount)))
			.couponAmount(0L)
			.feeAmount(0L)
			.build();

		refundOrder.setProgress(UniProgress.SENDED.getValue());
		refundOrder.setStartTime(new Date());
		try {
			RefundApplyResp resp = yunstTpl.execute(req, RefundApplyResp.class);
			refundOrder.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				refundOrder.setStatus(OrderStatus.FAIL.getValue());
				refundOrder.setTunnelErrCode(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(refundOrder);
		} catch (CommonGatewayException e) {
			dealGatewayError(refundOrder, e);
			return;
		} catch (Exception e) {
			dealUndefinedError(refundOrder, e);
		}

	}

	/**
	 * 协议消费
	 */
	public WalletCollectResp consume(WalletOrder order, WalletConsume consume, WalletTunnel payer,
		WalletTunnel payee, List<WalletCollectMethod> methods) {

		String expireTime = order.getExpireTime() != null ? DateUtil
			.formatDate(order.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
		ConsumeApplyReq req = ConsumeApplyReq.builder()
			.payerId(payer.getBizUserId())
			.protocolNo(payer.getBalanceProtocolNo())
			.receiverId(payee.getBizUserId())
			.bizOrderNo(order.getOrderNo())
			.payMethod(getMethodMap(methods, false))
			.amount(order.getAmount())
			.fee(0L)
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.orderExpireDatetime(expireTime)
			.industryCode(order.getIndustryCode())
			.industryName(order.getIndustryName())
			.summary(order.getNote())
			.source(1L)
			.build();

		order.setProgress(UniProgress.SENDED.getValue());
		order.setStartTime(new Date());
		CollectApplyResp resp = null;
		try {
			resp = yunstTpl.execute(req, CollectApplyResp.class);
			order.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				order.setStatus(OrderStatus.FAIL.getValue());
				order.setTunnelErrCode(resp.getPayCode());
				order.setTunnelErrMsg(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(order);
			WalletCollectResp result = BeanUtil.newInstance(order, WalletCollectResp.class);
			if (resp != null) {
				result.setPayInfo(resp.getPayInfo());
				result.setWeChatAPPInfo(resp.getWeChatAPPInfo());
				result.setTradeNo(resp.getTradeNo());
			}

			return result;
		} catch (CommonGatewayException e) {
			dealGatewayError(order, e);
		} catch (Exception e) {
			dealUndefinedError(order, e);
		}
		return null;
	}


	private void dealGatewayError(WalletOrder walletOrder, CommonGatewayException err) {
		log.error("网关错误", err);
		walletOrder.setTunnelErrCode(err.getBankErrCode());
		walletOrder.setTunnelErrMsg(err.getBankErrMsg());
		walletOrder.setUserErrMsg("交易异常");
		walletOrder.setStatus(OrderStatus.FAIL.getValue());
		walletOrder.setProgress(GwProgress.HAS_RESP.getValue());
		walletOrder.setSubStatus(OrderSubStatus.WAIT_DEAL.getValue());
		walletOrderDao.updateByPrimaryKeySelective(walletOrder);
		throw err;
	}


	private void dealUndefinedError(WalletOrder walletOrder, Exception e) {
		log.error("未定义异常", e);
		walletOrder.setUserErrMsg("交易异常");
		walletOrder.setProgress(GwProgress.HAS_RESP.getValue());
		walletOrder.setSubStatus(OrderSubStatus.WAIT_DEAL.getValue());
		walletOrderDao.updateByPrimaryKeySelective(walletOrder);
		throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
	}


	/**
	 * 更新状态
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletOrder updateOrderStatus(WalletOrder order) {
		try {
			GetOrderDetailResp tunnelOrder = queryOrderDetail(order.getOrderNo());
			if (!order.getOrderNo().equals(tunnelOrder.getBizOrderNo())) {
				log.error("订单号不匹配， order = {} , channelOrderNo = {}", order,
					tunnelOrder.getBizOrderNo());
				throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_STATUS_QUERY_ERROR);
			}
			// 通道状态与时间
			order.setTunnelStatus(String.valueOf(tunnelOrder.getOrderStatus()));
			Optional.ofNullable(tunnelOrder.getPayDatetime()).ifPresent(paytime -> {
				order.setTunnelSuccTime(
					DateUtil.parse(paytime, DateUtil.STANDARD_DTAETIME_PATTERN));
			});
			order.setTunnelErrMsg(tunnelOrder.getErrorMessage());
			YunstOrderStatus orderStatus = EnumUtil
				.parse(YunstOrderStatus.class, tunnelOrder.getOrderStatus());
			OrderStatus walletOrderStatus = orderStatus.toUniStatus();
			order.setStatus(walletOrderStatus.getValue());
			if (walletOrderStatus.isEndStatus()) {
				order.setProgress(UniProgress.RECEIVED.getValue());
			}
			if (walletOrderStatus.isEndStatus()) {
				order.setEndTime(new Date());
			}
			// 更新订单状态
			walletOrderDao.updateByPrimaryKeySelective(order);

			// 更新余额明细
			if (walletOrderStatus.isEndStatus()
				&& OrderType.COLLECT.getValue() != order.getType()
				&& OrderType.REFUND.getValue() != order.getType()
				&& !EnumBizTag.RECORD.contains(order.getBizTag())) {

				Byte walletDetailStatus =
					(OrderStatus.SUCC.getValue().byteValue() == walletOrderStatus.getValue())
						? BalanceDetailStatus.SUCC.getValue() : BalanceDetailStatus.FAIL.getValue();
				List<WalletBalanceDetail> details = walletBalanceDetailDao
					.selectByOrderId(order.getId());
				details.forEach(detail -> {
					detail.setStatus(walletDetailStatus);
					walletBalanceDetailDao.updateByPrimaryKeySelective(detail);
					log.info("[自动提现] 更新余额明细单状态 单号 {} , 状态 {}", order.getOrderNo(),
						walletDetailStatus);

					if (detail.getAmount() < 0 && detail.getRefOrderId() != null) {
						long unFreezen = 0;
						long balance = 0;
						if (BalanceFreezeMode.NO_FREEZE.getValue().byteValue() == detail
							.getRefFreezeMode()) {
							unFreezen = 0;
							balance = BalanceDetailStatus.SUCC.getValue().byteValue()
								== walletDetailStatus ? detail.getAmount() : 0;
						} else if (BalanceFreezeMode.FREEZEN.getValue().byteValue() == detail
							.getRefFreezeMode()) {
							unFreezen = detail.getAmount();
							balance = BalanceDetailStatus.SUCC.getValue().byteValue()
								== walletDetailStatus ? 0 : -detail.getAmount();
						}
						walletBalanceDetailDao.updateDetailFreezen(detail.getRefOrderId(),
							detail.getRefOrderDetailId(), unFreezen, balance);
						log.info("[自动提现] 更新入金余额 入金单号 {} , 状态 {}", detail.getRefOrderNo(),
							walletDetailStatus);
					}
				});
				if (details.isEmpty() && OrderType.WITHDRAWAL.getValue() == order.getType()
					&& BalanceDetailStatus.SUCC.getValue().byteValue() == walletDetailStatus) {
					WalletWithdraw withdraw = walletWithdrawDao.selectByOrderId(order.getId());
					List<Tuple<WalletBalanceDetail, Long>> payDetails = walletBalanceDetailService
						.selectDetailToPay(order.getWalletId(), order.getAmount(), false);
					// 锁定余额明细
					Optional<String> orderNos = payDetails.stream().map(tuple -> {
						WalletBalanceDetail payDetail = tuple.left;
						walletBalanceDetailDao.updateDetailFreezen(payDetail.getOrderId(),
							payDetail.getOrderDetailId(), 0L, -tuple.right);

						WalletBalanceDetail withdrawDetail = WalletBalanceDetail.builder()
							.walletId(order.getWalletId())
							.orderId(order.getId())
							.orderNo(order.getOrderNo())
							.orderDetailId(withdraw.getId())
							.refOrderId(payDetail.getOrderId())
							.refOrderNo(payDetail.getOrderNo())
							.refOrderDetailId(payDetail.getOrderDetailId())
							.type(order.getType())
							.status(BalanceDetailStatus.SUCC.getValue())
							.amount(-tuple.right)
							.balance(-tuple.right)
							.freezen(0L)
							.refFreezeMode(BalanceFreezeMode.NO_FREEZE.getValue())
							.createTime(new Date())
							.build();
						walletBalanceDetailDao.insertSelective(withdrawDetail);

						return withdrawDetail.getOrderNo();

					}).reduce((x, y) -> x + "," + y);

					log.info("发起余额提现出金  出金单号 {} , 入金单号 {}", order.getOrderNo(),
						orderNos.orElse(""));
				}
			}

			// 记录到流水
			if (OrderStatus.SUCC.getValue().byteValue() == order.getStatus()
				&& (order.getBizTag() == null || !EnumBizTag.RECORD.contains(order.getBizTag()))) {

				// 累计
				order.setBizTag(
					EnumBizTag.RECORD.and(Optional.ofNullable(order.getBizTag()).orElse((byte) 0)));
				if (order.getType().byteValue() == OrderType.RECHARGE.getValue().byteValue()) {
					walletDao.accRecharge(order.getWalletId(), order.getAmount());
				} else if (order.getType().byteValue() == OrderType.COLLECT.getValue().byteValue()
					|| order.getType().byteValue() == OrderType.CONSUME.getValue().byteValue()) {
					walletDao.accPay(order.getWalletId(), order.getAmount());
				}
				walletOrderDao.updateByPrimaryKeySelective(order);

				// 代付
				if (order.getType().byteValue() == OrderType.AGENT_PAY.getValue()) {
					MoneyLog moneyLog = MoneyLog.builder()
						.walletId(order.getWalletId())
						.orderId(order.getId())
						.orderNo(order.getOrderNo())
						.orderType(order.getType())
						.type(DebitType.DEBIT.getValue())
						.debitAmount(order.getAmount())
						.creditAmount(0L)
						.createTime(new Date())
						.build();
					moneyLogDao.insertSelective(moneyLog);
					WalletClearing clearing = walletClearingDao.selectByOrderId(order.getId());
					walletCollectInfoDao
						.accuClearAmount(clearing.getCollectInfoId(), clearing.getAmount());
				} else {
					MoneyLogBuilder builder = MoneyLog.builder()
						.walletId(order.getWalletId())
						.orderId(order.getId())
						.orderNo(order.getOrderNo())
						.orderType(order.getType())
						.createTime(new Date());
					if (order.getType().byteValue() == OrderType.RECHARGE.getValue()
						|| order.getType().byteValue() == OrderType.REFUND.getValue()) {
						// 充值、退款
						builder.type(DebitType.DEBIT.getValue());
						builder.debitAmount(order.getAmount());
						builder.creditAmount(0L);
					} else if (order.getType().byteValue() == OrderType.WITHDRAWAL.getValue()
						|| order.getType().byteValue() == OrderType.COLLECT.getValue()
						|| order.getType().byteValue() == OrderType.CONSUME.getValue()
						|| order.getType().byteValue() == OrderType.DEDUCTION.getValue()) {
						// 提现、代收、消费、代扣
						builder.type(DebitType.CREDIT.getValue());
						builder.debitAmount(0L);
						builder.creditAmount(order.getAmount());
					}
					moneyLogDao.insertSelective(builder.build());

					if (order.getType().byteValue() == OrderType.REFUND.getValue()) {
						WalletRefund refund = walletRefundDao.selectByOrderId(order.getId());
						List<WalletRefundDetail> details = walletRefundDetailDao
							.selectByRefundId(refund.getId());
						details.forEach(detail -> {
							walletCollectInfoDao
								.accuRefundAmount(detail.getCollectInfoId(), detail.getAmount());
						});
					}
				}


			}
			// 处理失败的退款
			else if ((OrderStatus.FAIL.getValue().byteValue() == order.getStatus()
				|| OrderStatus.CLOSED.getValue().byteValue() == order.getStatus())
				&& OrderType.REFUND.getValue().byteValue() == order.getType().byteValue()
				&& (order.getBizTag() == null || !EnumBizTag.RECORD.contains(order.getBizTag()))) {
				order.setBizTag(
					EnumBizTag.RECORD.and(Optional.ofNullable(order.getBizTag()).orElse((byte) 0)));
				walletOrderDao.updateByPrimaryKeySelective(order);

				WalletRefund walletRefund = walletRefundDao.selectByOrderId(order.getId());
				WalletCollect walletCollect = walletCollectDao
					.selectByOrderId(walletRefund.getCollectOrderId());
				walletCollect.setRefundLimit(walletCollect.getRefundLimit() + order.getAmount());
				walletCollect
					.setRemainTunnelFee(walletCollect.getRemainTunnelFee() - order.getTunnelFee());
				walletCollectDao.updateByPrimaryKeySelective(walletCollect);
			}
			return order;
		} catch (Exception e) {
			dealUndefinedError(order, e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}


	/**
	 * 支付方式
	 */
	private Map<String, Object> getMethodMap(List<WalletCollectMethod> methods,
		boolean isRecharge) {
		Map<String, Object> payMethod = new HashMap<>();
		methods.forEach(m -> {
			if (CollectPayType.BALANCE.getValue().byteValue() == m.getPayType().byteValue()) {
				Balance balance = Balance.builder()
					.accountSetNo(configService.getUserAccSet())
					.amount(m.getAmount())
					.build();
				payMethod.put(Balance.KEY_Balance, Arrays.asList(balance));
			} else if (CollectPayType.WECHAT_MINIPROGROGRAM.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				MiniProgram miniProgram = MiniProgram.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.subAppid(m.getAppId())
					.amount(m.getAmount())
					.acct(m.getOpenId())
					.vspCusid(m.getSellerId())
					.build();
				payMethod.put(Wechat.KEY_MiniProgram, miniProgram);
			} else if (CollectPayType.WECHAT_APPOPEN.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				AppOpen appOpen = AppOpen.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.subAppId(m.getAppId())
					.amount(m.getAmount())
					.build();
				payMethod.put(Wechat.KEY_AppOpen, appOpen);
			} else if (CollectPayType.WECHAT_H5OPEN.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				H5Open h5Open = H5Open.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.subAppId(m.getAppId())
					.amount(m.getAmount())
					.acct(m.getOpenId())
					.cusip(m.getCusIp())
					.sceneInfo(m.getSceneInfo())
					.build();
				payMethod.put(Wechat.KEY_H5Open, h5Open);
			} else if (CollectPayType.WECHAT_WechatPublic.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				WechatPublic wechatPublic = WechatPublic.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.subAppid(m.getAppId())
					.amount(m.getAmount())
					.acct(m.getOpenId())
					.vspCusid(m.getSellerId())
					.build();
				payMethod.put(Wechat.KEY_WechatPublic, wechatPublic);
			} else if (CollectPayType.WECHAT_ScanWeixin.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				ScanWeixin scanWeixin = ScanWeixin.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.amount(m.getAmount())
					.vspCusid(m.getSellerId())
					.build();
				payMethod.put(Wechat.KEY_ScanWeixin, scanWeixin);
			} else if (CollectPayType.ALIPAY_ScanAlipay.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				ScanAlipay scanAlipay = ScanAlipay.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.amount(m.getAmount())
					.vspCusid(m.getSellerId())
					.build();
				payMethod.put(Alipay.KEY_ScanAlipay, scanAlipay);
			} else if (CollectPayType.ALIPAY_Service.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				AlipayService alipayService = AlipayService.builder()
					.amount(m.getAmount())
					.acct(m.getOpenId())
					.vspCusid(m.getSellerId())
					.build();
				payMethod.put(Alipay.KEY_AlipayService, alipayService);
			} else if (CollectPayType.ALIPAY_AppOpen.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				AppOpen appOpen = AppOpen.builder()
					.amount(m.getAmount())
					.build();
				payMethod.put(Alipay.KEY_AppOpen, appOpen);
			} else if (CollectPayType.CODEPAY.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				CodePayVsp codePayVsp = CodePayVsp.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.amount(m.getAmount())
					.authcode(m.getSceneInfo())
					.vspCusid(m.getSellerId())
					.build();
				payMethod.put(CodePay.KEY_CodePayVsp, codePayVsp);
			} else if (CollectPayType.BANKCARD.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				String bankCardNo = null;
				try {
					bankCardNo = RSAUtil.encrypt(m.getOpenId());
				} catch (Exception e) {
					log.error("银行卡加密错误", e);
				}
				CardQuickPay bankCard = CardQuickPay.builder()
					.bankCardNo(bankCardNo)
					.amount(m.getAmount())
					.build();
				payMethod.put(CollectPayMethod.BankCard.KEY_QuickPayVsp, bankCard);
			} else if (CollectPayType.POS.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				Pos pos = Pos.builder()
					.vspCusid(m.getSellerId())
					.amount(m.getAmount())
					.build();
				payMethod.put(CollectPayMethod.Pos.KEY_POS, pos);
			}
		});
		return payMethod;
	}

	/**
	 * 查询订单状态
	 */
	private GetOrderDetailResp queryOrderDetail(String orderNo) {

		GetOrderDetailReq req = GetOrderDetailReq.builder().bizOrderNo(orderNo).build();
		try {
			return yunstTpl.execute(req, GetOrderDetailResp.class);
		} catch (Exception e) {
			log.error("通联-接口异常", e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}


	/**
	 * 短信确认支付
	 */
	public SmsPayResp smsConfirm(WalletOrder order, String tradeNo, String verifyCode, String ip) {

		WalletTunnel payer = walletChannelDao
			.selectByWalletId(order.getWalletId(), order.getTunnelType());
		SmsPayReq req = SmsPayReq.builder()
			.bizUserId(payer.getBizUserId())
			.bizOrderNo(order.getOrderNo())
			.tradeNo(tradeNo)
			.verificationCode(verifyCode)
			.consumerIp(ip)
			.build();
		try {
			return yunstTpl.execute(req, SmsPayResp.class);
		} catch (CommonGatewayException e) {
			throw e;
		} catch (Exception e) {
			log.error("通联-接口异常", e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}

	/**
	 * 重发短信
	 */
	public SmsRetryResp smsRetry(WalletOrder order) {

		SmsRetryReq req = SmsRetryReq.builder()
			.bizOrderNo(order.getOrderNo())
			.build();

		try {
			return yunstTpl.execute(req, SmsRetryResp.class);
		} catch (CommonGatewayException e) {
			throw e;
		} catch (Exception e) {
			log.error("通联-接口异常", e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}


	/**
	 * 短信确认支付
	 */
	public String smsGwConfirm(WalletOrder order, WalletTunnel channel, String consumerIp) {

		SmsGwPayReq req = SmsGwPayReq.builder()
			.bizOrderNo(order.getOrderNo())
			.bizUserId(channel.getBizUserId())
			.consumerIp(consumerIp)
			.build();
		return yunstTpl.signRequest(req);
	}

	/**
	 * 密码确认支付
	 */
	public String pwdGwConfirm(WalletOrder order, WalletTunnel channel, String jumpUrl,
		String consumerIp) {

		PwdConfirmReq req = PwdConfirmReq.builder()
			.bizOrderNo(order.getOrderNo())
			.bizUserId(channel.getBizUserId())
			.jumpUrl(jumpUrl)
			.consumerIp(consumerIp)
			.build();
		return yunstTpl.signRequest(req);
	}

	/**
	 * 对账
	 */
	public String downloadBalanceFile(Date date) {
		String resourceUrl = queryBalanceFile.apply(date);
		return downloadFile.apply(resourceUrl);
	}

	private Function<Date, String> queryBalanceFile = (date) -> {
		// 获取文件地址
		GetCheckAccountFileReq req = GetCheckAccountFileReq.builder()
			.date(DateUtil.formatDate(date, DateUtil.SHORT_DTAE_PATTERN))
			.fileType(YunstFileType.DETAIL.getValue())
			.build();
		try {
			GetCheckAccountFileResp resp = yunstTpl.execute(req, GetCheckAccountFileResp.class);
			return resp.getUrl();
		} catch (Exception e) {
			log.error("通联-接口异常", e);
			if (e instanceof CommonGatewayException) {
				throw (CommonGatewayException) e;
			}
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	};

	private Function<String, String> downloadFile = (resourceUrl) -> {
		// 下载文件
		try {
			URL url = new URL(resourceUrl);
			String uri = url.getFile();
			String dir = configService.getStorageDir() + "/yunst/";
			String fileUrl = dir + uri.substring(uri.lastIndexOf("/"));
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdirs();
			}
			HttpFile.download(url, fileUrl);
			return fileUrl;
		} catch (Exception e) {
			log.error("文件下载错误", e);
			if (e instanceof CommonGatewayException) {
				throw (CommonGatewayException) e;
			}
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	};

	public String cardBin(String cardNo) {
		try {
			cardNo = RSAUtil.encrypt(cardNo);
		} catch (Exception e) {
			log.error("银行卡加密失败", e);
		}

		CardBinReq req = CardBinReq.builder()
			.cardNo(cardNo)
			.build();

		try {
			CardBinResp resp = yunstTpl.execute(req, CardBinResp.class);
			return resp.getCardBinInfo();
		} catch (Exception e) {
			log.error("通联-接口异常", e);
			if (e instanceof CommonGatewayException) {
				throw (CommonGatewayException) e;
			}
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}

	private void noteSuffer(String note) {
		String message = Optional.ofNullable(note).orElse("");

		String summary = (message.length() > 20) ? message.substring(0, 19) : message;
	}

}
