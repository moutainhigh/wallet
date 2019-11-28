package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.rfchina.platform.biztools.fileserver.HttpFile;
import com.rfchina.platform.common.misc.Triple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.MoneyLogMapper;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.MoneyLog;
import com.rfchina.wallet.domain.model.MoneyLog.MoneyLogBuilder;
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
import com.rfchina.wallet.server.mapper.ext.WalletConsumeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletWithdrawExtDao;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.DebitType;
import com.rfchina.wallet.server.msic.EnumWallet.EnumBizTag;
import com.rfchina.wallet.server.msic.EnumWallet.EnumYunstDeviceType;
import com.rfchina.wallet.server.msic.EnumWallet.EnumYunstWithdrawType;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.msic.EnumWallet.RefundType;
import com.rfchina.wallet.server.msic.EnumWallet.UniProgress;
import com.rfchina.wallet.server.msic.EnumWallet.YunstFileType;
import com.rfchina.wallet.server.msic.EnumWallet.YunstOrderStatus;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.GatewayTransService;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
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
	private WalletConsumeExtDao walletConsumeDao;

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
				WalletOrder walletOrder = updateOrderStatus(order);
				return new Triple<WalletOrder, WalletFinance, GatewayTrans>(walletOrder, null,
					null);
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
			dealUndefinedError(e);
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
			.summary(order.getNote())
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
			dealUndefinedError(e);
		}
		return null;
	}

	/**
	 * 代收
	 */
	public WalletCollectResp collect(WalletOrder order, WalletCollect collect,
		List<WalletCollectInfo> clearInfos, WalletTunnel payer) {
		// 收款人
		List<RecieveInfo> receives = clearInfos.stream().map(info -> {
			WalletTunnel receiver = walletChannelDao
				.selectByWalletId(info.getPayeeWalletId(), order.getTunnelType());
			return RecieveInfo.builder()
				.bizUserId(receiver.getBizUserId())
				.amount(info.getBudgetAmount())
				.build();
		}).collect(Collectors.toList());

		List<WalletCollectMethod> methods = walletCollectMethodDao
			.selectByCollectId(collect.getId(), OrderType.COLLECT.getValue());

		String expireTime = order.getExpireTime() != null ? DateUtil
			.formatDate(order.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
		CollectApplyReq req = CollectApplyReq.builder()
			.bizOrderNo(order.getOrderNo())
			.payerId(payer.getBizUserId())
			.recieverList(receives)
			.amount(order.getAmount())
			.fee(0L)
			.validateType(collect.getValidateType().longValue())
			.frontUrl(null)
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.ordErexpireDatetime(expireTime)
			.payMethod(getMethodMap(methods, false))
			.tradeCode(TRADE_CODESTRING_COLLECT)
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
			dealUndefinedError(e);
		}
		return null;
	}


	/**
	 * 代付
	 */
	public void agentPay(WalletOrder order, WalletClearing clearing) {

		// 登记代付数额
		CollectPay collectPay = CollectPay.builder()
			.bizOrderNo(clearing.getCollectOrderNo())
			.amount(clearing.getAmount())
			.build();
		WalletTunnel tunnel = walletTunnelExtDao
			.selectByWalletId(order.getWalletId(), order.getTunnelType());
		AgentPayReq req = AgentPayReq.builder()
			.bizOrderNo(order.getOrderNo())
			.collectPayList(Lists.newArrayList(collectPay))
			.bizUserId(tunnel.getBizUserId())
			.accountSetNo(configService.getUserAccSet())  // 产品需求代付到余额账户
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.amount(clearing.getAmount())
			.fee(0L)
			.splitRuleList(null)
			.tradeCode(TRADE_CODESTRING_AGENTPAY)
			.summary(order.getNote())
			.build();

		order.setProgress(UniProgress.SENDED.getValue());
		order.setStartTime(new Date());
		try {
			AgentPayResp resp = yunstTpl.execute(req, AgentPayResp.class);
			order.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				order.setStatus(OrderStatus.FAIL.getValue());
				order.setTunnelErrMsg(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(order);
		} catch (CommonGatewayException e) {
			dealGatewayError(order, e);
		} catch (Exception e) {
			dealUndefinedError(e);
			return;
		}
	}

	private void dealUndefinedError(Exception e) {
		log.error("未定义异常", e);
		throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
	}

	/**
	 * 退款
	 */
	public void refund(WalletOrder order, WalletRefund refund, List<WalletRefundDetail> details) {

		List<RefundInfo> refundList = details.stream().map(detail -> {
			WalletTunnel payeeChannel = walletChannelDao
				.selectByWalletId(detail.getPayeeWalletId(), order.getTunnelType());
			return RefundInfo.builder()
				.accountSetNo(null)   //不送：默认从平台中间账户集退款
				.bizUserId(payeeChannel.getBizUserId())
				.amount(detail.getAmount())
				.build();
		}).collect(Collectors.toList());

		WalletTunnel payerChannel = walletChannelDao
			.selectByWalletId(order.getWalletId(), order.getTunnelType());
		RefundApplyReq req = RefundApplyReq.builder()
			.bizOrderNo(order.getOrderNo())
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

		order.setProgress(UniProgress.SENDED.getValue());
		order.setStartTime(new Date());
		try {
			RefundApplyResp resp = yunstTpl.execute(req, RefundApplyResp.class);
			order.setTunnelOrderNo(resp.getOrderNo());
			if (StringUtils.isNotBlank(resp.getPayStatus())
				&& "fail".equals(resp.getPayStatus())) {
				order.setStatus(OrderStatus.FAIL.getValue());
				order.setTunnelErrCode(resp.getPayFailMessage());
			}
			walletOrderDao.updateByPrimaryKeySelective(order);
		} catch (CommonGatewayException e) {
			dealGatewayError(order, e);
			return;
		} catch (Exception e) {
			dealUndefinedError(e);
		}

	}

	/**
	 * 退款
	 */
	public WalletCollectResp consume(WalletOrder order, WalletConsume consume, WalletTunnel payer,
		WalletTunnel payee, List<WalletCollectMethod> methods) {

		String expireTime = order.getExpireTime() != null ? DateUtil
			.formatDate(order.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
		ConsumeApplyReq req = ConsumeApplyReq.builder()
			.payerId(payer.getBizUserId())
			.recieverId(payee.getBizUserId())
			.bizOrderNo(order.getOrderNo())
			.amount(order.getAmount())
			.fee(0L)
			.validateType(consume.getValidateType().longValue())
			.frontUrl(null)
			.backUrl(configService.getYunstRecallPrefix() + UrlConstant.YUNST_ORDER_RECALL)
			.orderExpireDatetime(expireTime)
			.payMethod(getMethodMap(methods, false))
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
			dealUndefinedError(e);
		}
		return null;
	}


	private void dealGatewayError(WalletOrder order, CommonGatewayException e) {
		log.error("网关错误", e);
		order.setTunnelErrCode(e.getBankErrCode());
		order.setTunnelErrMsg(e.getBankErrMsg());
		walletOrderDao.updateByPrimaryKeySelective(order);
		throw e;
	}


	/**
	 * 更新状态
	 */
	public WalletOrder updateOrderStatus(WalletOrder order) {

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
		OrderStatus applyStatus = orderStatus.toUniStatus();
		order.setStatus(applyStatus.getValue());
		order.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			order.setEndTime(new Date());
		}
		order.setBizTag(
			EnumBizTag.RECORD.and(Optional.ofNullable(order.getBizTag()).orElse((byte) 0)));
		walletOrderDao.updateByPrimaryKeySelective(order);

		// 记录到流水
		if (OrderStatus.SUCC.getValue().byteValue() == order.getStatus()
			&& (order.getBizTag() == null || !EnumBizTag.RECORD.contains(order.getBizTag()))) {
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
				if (order.getType().byteValue() == OrderType.REFUND.getValue()) {
					WalletRefund refund = walletRefundDao.selectByOrderId(order.getId());
					List<WalletRefundDetail> details = walletRefundDetailDao
						.selectByRefundId(refund.getId());
					details.forEach(detail -> {
						walletCollectInfoDao
							.accuRefundAmount(detail.getCollectInfoId(), detail.getAmount());
					});

				}
				moneyLogDao.insertSelective(builder.build());
			}


		}

		return order;
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
					.subAppId(m.getAppId())
					.amount(m.getAmount())
					.acct(m.getOpenId())
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
					.subAppId(m.getAppId())
					.amount(m.getAmount())
					.acct(m.getOpenId())
					.build();
				payMethod.put(Wechat.KEY_WechatPublic, wechatPublic);
			} else if (CollectPayType.WECHAT_ScanWeixin.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				ScanWeixin scanWeixin = ScanWeixin.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.amount(m.getAmount())
					.build();
				payMethod.put(Wechat.KEY_ScanWeixin, scanWeixin);
			} else if (CollectPayType.ALIPAY_ScanAlipay.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				ScanAlipay scanAlipay = ScanAlipay.builder()
					.limitPay(isRecharge ? "no_credit" : "")
					.amount(m.getAmount())
					.build();
				payMethod.put(Alipay.KEY_ScanAlipay, scanAlipay);
			} else if (CollectPayType.ALIPAY_Service.getValue().byteValue() ==
				m.getPayType().byteValue()) {
				AlipayService alipayService = AlipayService.builder()
					.amount(m.getAmount())
					.acct(m.getOpenId())
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
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	};

	private Function<String, String> downloadFile = (resourceUrl) -> {
		// 下载文件
		try {
			URL url = new URL(resourceUrl);
			String uri = url.getFile();
			String fileUrl =
				configService.getStorageDir() + "/yunst/" + uri.substring(uri.lastIndexOf("/"));
			HttpFile.download(url, fileUrl);
			return fileUrl;
		} catch (Exception e) {
			log.error("文件下载错误", e);
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
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}
}
