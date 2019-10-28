package com.rfchina.wallet.server.service.handler.yunst;

import com.alibaba.fastjson.JSON;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.google.common.collect.Lists;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletLevel;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.domain.model.WalletClearInfo;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.yunst.exception.UnknownException;
import com.rfchina.wallet.server.bank.yunst.request.AgentPayReq;
import com.rfchina.wallet.server.bank.yunst.request.AgentPayReq.CollectPay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Alipay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Alipay.AlipayService;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Alipay.ScanAlipay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Balance;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.CodePay;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.CodePay.CodePayVsp;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.AppOpen;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.H5Open;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.MiniProgram;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.ScanWeixin;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod.Wechat.WechatPublic;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.RecieveInfo;
import com.rfchina.wallet.server.bank.yunst.request.DepositApplyReq;
import com.rfchina.wallet.server.bank.yunst.request.GetOrderDetailReq;
import com.rfchina.wallet.server.bank.yunst.request.RefundApplyReq;
import com.rfchina.wallet.server.bank.yunst.request.RefundApplyReq.RefundInfo;
import com.rfchina.wallet.server.bank.yunst.response.AgentPayResp;
import com.rfchina.wallet.server.bank.yunst.response.CollectApplyResp;
import com.rfchina.wallet.server.bank.yunst.response.GetOrderDetailResp;
import com.rfchina.wallet.server.bank.yunst.response.RefundApplyResp;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.BankCard;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectStatus;
import com.rfchina.wallet.server.msic.EnumWallet.EnumYunstDeviceType;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.RefundType;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.msic.EnumWallet.UniProgress;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyType;
import com.rfchina.wallet.server.msic.EnumWallet.YunstOrderStatus;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.GatewayTransService;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private GatewayTransService gatewayTransService;

	@Autowired
	private WalletChannelExtDao walletChannelDao;

	@Autowired
	private WalletCollectExtDao walletCollectDao;

	@Autowired
	private WalletClearInfoExtDao walletClearInfoDao;

	@Autowired
	private WalletCollectMethodExtDao walletCollectMethodDao;

	@Autowired
	private WalletChannelExtDao walletChannelExtDao;

	@Autowired
	private WalletClearingExtDao walletClearingDao;

	@Autowired
	private WalletRefundExtDao walletRefundDao;

	@Autowired
	private WalletRefundDetailExtDao walletRefundDetailDao;

	@Autowired
	private WalletRechargeExtDao walletRechargeDao;

	@Autowired
	private ConfigService configService;


	public boolean isSupportWalletLevel(Byte walletLevel) {
		return EnumWalletLevel.SENIOR.getValue().byteValue() == walletLevel;
	}


	public GatewayMethod getGatewayMethod() {
		return null;
	}


	public PayStatusResp onAskErr(WalletApply walletLog, IGatewayError err) {
		return null;
	}

	/**
	 * 转账
	 */
	public Tuple<GatewayMethod, PayTuple> transfer(Long applyId) {
		return null;
	}

	/**
	 * 充值
	 */
	public List<WalletRecharge> recharge(Long applyId) {
		WalletApply walletApply = walletApplyDao.selectByPrimaryKey(applyId);
		List<WalletRecharge> recharges = walletRechargeDao.selectByApplyId(applyId);

		return recharges.stream().map(recharge -> {

			List<WalletCollectMethod> methods = walletCollectMethodDao
				.selectByCollectId(recharge.getId(), WalletApplyType.RECHARGE.getValue());

			WalletChannel payer = walletChannelDao
				.selectByWalletId(recharge.getPayerWalletId(), TunnelType.YUNST.getValue());
			String expireTime = recharge.getExpireTime() != null ? DateUtil
				.formatDate(recharge.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
			DepositApplyReq req = DepositApplyReq.builder()
				.bizOrderNo(recharge.getOrderNo())
				.bizUserId(payer.getBizUserId())
				.accountSetNo(configService.getUserAccSet())
				.amount(walletApply.getAmount())
				.fee(0L)
				.validateType(Long.valueOf(recharge.getValidateType()))
				.frontUrl(null)
				.backUrl(configService.getYunstRechargeRecallUrl())
				.orderExpireDatetime(expireTime)
				.payMethod(getMethodMap(methods, true))
				.industryCode("1910")
				.industryName("其他")
				.source(EnumYunstDeviceType.MOBILE.getValue())
				.build();

			recharge.setStartTime(new Date());
			recharge.setProgress(UniProgress.SENDED.getValue());
			try {
				CollectApplyResp resp = yunstTpl.execute(req, CollectApplyResp.class);
				recharge.setTunnelOrderNo(resp.getOrderNo());
				if (StringUtils.isNotBlank(resp.getPayStatus())
					&& "fail".equals(resp.getPayStatus())) {
					recharge.setStatus(CollectStatus.FAIL.getValue());
					recharge.setErrCode(resp.getPayCode());
					recharge.setErrMsg(resp.getPayFailMessage());
				}
				walletRechargeDao.updateByPrimaryKey(recharge);
				return recharge;
			} catch (CommonGatewayException e) {
				log.error("{}", JsonUtil.toJSON(e));
				recharge.setErrCode(e.getBankErrCode());
				recharge.setErrMsg(e.getBankErrMsg());
				walletRechargeDao.updateByPrimaryKey(recharge);
				throw e;
			} catch (Exception e) {
				log.error("{}", JSON.toJSONString(e));
				throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
			}
		}).filter(item -> item != null)
			.collect(Collectors.toList());
	}

	/**
	 * 代收
	 */
	public List<WalletCollectResp> collect(Long applyId) {
		List<WalletCollect> collects = walletCollectDao.selectByApplyId(applyId);
		// 付款人
		return collects.stream().map(collect -> {
			List<WalletClearInfo> clears = walletClearInfoDao.selectByCollectId(collect.getId());
			// 收款人
			List<RecieveInfo> receives = clears.stream().map(clear -> {
				WalletChannel receiver = walletChannelDao
					.selectByWalletId(clear.getPayeeWalletId(), collect.getTunnelType());
				return RecieveInfo.builder()
					.bizUserId(receiver.getBizUserId())
					.amount(clear.getBudgetAmount())
					.build();
			}).collect(Collectors.toList());

			List<WalletCollectMethod> methods = walletCollectMethodDao
				.selectByCollectId(collect.getId(), WalletApplyType.COLLECT.getValue());

			WalletChannel payer = walletChannelDao
				.selectByWalletId(collect.getPayerWalletId(), TunnelType.YUNST.getValue());
			String expireTime = collect.getExpireTime() != null ? DateUtil
				.formatDate(collect.getExpireTime(), DateUtil.STANDARD_DTAETIME_PATTERN) : null;
			CollectApplyReq req = CollectApplyReq.builder()
				.bizOrderNo(collect.getOrderNo())
				.payerId(payer.getBizUserId())
				.recieverList(receives)
				.amount(collect.getAmount())
				.fee(0L)
				.validateType(0L)
				.frontUrl(null)
				.backUrl(configService.getYunstCollectRecallUrl())
				.ordErexpireDatetime(expireTime)
				.payMethod(getMethodMap(methods, false))
				.tradeCode(TRADE_CODESTRING_COLLECT)
				.industryCode("1910")
				.industryName("其他")
				.source(1L)
				.build();

			collect.setProgress(UniProgress.SENDED.getValue());
			collect.setStartTime(new Date());
			CollectApplyResp resp = null;
			try {
				resp = yunstTpl.execute(req, CollectApplyResp.class);
				collect.setTunnelOrderNo(resp.getOrderNo());
				if (StringUtils.isNotBlank(resp.getPayStatus())
					&& "fail".equals(resp.getPayStatus())) {
					collect.setStatus(CollectStatus.FAIL.getValue());
					collect.setErrCode(resp.getPayCode());
					collect.setErrMsg(resp.getPayFailMessage());
				}
			} catch (CommonGatewayException e) {
				log.error("{}", JsonUtil.toJSON(e));
				collect.setErrCode(e.getBankErrCode());
				collect.setErrMsg(e.getBankErrMsg());
			} catch (Exception e) {
				log.error("{}", JsonUtil.toJSON(e));
			}
			walletCollectDao.updateByPrimaryKey(collect);
			WalletCollectResp result = BeanUtil.newInstance(collect, WalletCollectResp.class);
			if (resp != null) {
				result.setPayInfo(resp.getPayInfo());
				result.setWeChatAPPInfo(resp.getWeChatAPPInfo());
			}
			return result;
		}).filter(item -> item != null)
			.collect(Collectors.toList());
	}


	/**
	 * 代付
	 */
	public List<WalletClearing> agentPay(Long applyId) {

		List<WalletClearing> walletClearings = walletClearingDao.selectByApplyId(applyId);
		// 登记代付数额
		return walletClearings.stream().map(clearing -> {
			WalletCollect collect = walletCollectDao.selectByPrimaryKey(clearing.getCollectId());
			CollectPay collectPay = CollectPay.builder()
				.bizOrderNo(collect.getOrderNo())
				.amount(clearing.getAmount())
				.build();
			WalletChannel tunnel = walletChannelExtDao
				.selectByWalletId(clearing.getPayeeWalletId(), TunnelType.YUNST.getValue());
			AgentPayReq req = AgentPayReq.builder()
				.bizOrderNo(clearing.getOrderNo())
				.collectPayList(Lists.newArrayList(collectPay))
				.bizUserId(tunnel.getBizUserId())
				.accountSetNo(configService.getUserAccSet())  // 产品需求代付到余额账户
				.backUrl(configService.getYunstAgentPayRecallUrl())
				.amount(clearing.getAmount())
				.fee(0L)
				.splitRuleList(null)
				.tradeCode(TRADE_CODESTRING_AGENTPAY)
				.summary(null)
//				.extendInfo(clearing.getOrderNo())
				.build();

			clearing.setProgress(UniProgress.SENDED.getValue());
			clearing.setStartTime(new Date());
			try {
				AgentPayResp resp = yunstTpl.execute(req, AgentPayResp.class);
				if (StringUtils.isNotBlank(resp.getPayStatus())
					&& "fail".equals(resp.getPayStatus())) {
					clearing.setStatus(CollectStatus.FAIL.getValue());
					clearing.setErrMsg(resp.getPayFailMessage());
				}
				clearing.setTunnelOrderNo(resp.getOrderNo());
			} catch (CommonGatewayException e) {
				log.error("{}", JsonUtil.toJSON(e));
				clearing.setErrCode(e.getBankErrCode());
				clearing.setErrMsg(e.getBankErrMsg());
			} catch (Exception e) {
				log.error("", e);
			}
			walletClearingDao.updateByPrimaryKey(clearing);

			return clearing;
		}).filter(item -> item != null)
			.collect(Collectors.toList());
	}

	/**
	 * 退款
	 */
	public List<WalletRefund> refund(Long applyId) {
		List<WalletRefund> refunds = walletRefundDao.selectByApplyId(applyId);

		return refunds.stream().map(refund -> {
			List<WalletRefundDetail> details = walletRefundDetailDao
				.selectByRefundId(refund.getId());
			List<RefundInfo> refundList = details.stream().map(detail -> {
				WalletChannel payeeChannel = walletChannelDao
					.selectByWalletId(detail.getPayeeWalletId(), refund.getTunnelType());
				return RefundInfo.builder()
					.accountSetNo(null)
					.bizUserId(payeeChannel.getBizUserId())
					.amount(detail.getAmount())
					.build();
			}).collect(Collectors.toList());

			WalletCollect collect = walletCollectDao.selectByPrimaryKey(refund.getCollectId());
			WalletChannel payerChannel = walletChannelDao
				.selectByWalletId(refund.getPayerWalletId(), refund.getTunnelType());
			RefundApplyReq req = RefundApplyReq.builder()
				.bizOrderNo(refund.getOrderNo())
				.oriBizOrderNo(collect.getOrderNo())
				.bizUserId(payerChannel.getBizUserId())
				.refundType(RefundType.D0.getValue())
				.refundList(refundList)
				.backUrl(configService.getYunstRefundRecallUrl())
				.amount(details.stream()
					.collect(Collectors.summingLong(WalletRefundDetail::getAmount)))
				.couponAmount(0L)
				.feeAmount(0L)
				.build();

			refund.setProgress(UniProgress.SENDED.getValue());
			refund.setStartTime(new Date());
			try {
				RefundApplyResp resp = yunstTpl.execute(req, RefundApplyResp.class);
				if (StringUtils.isNotBlank(resp.getPayStatus())
					&& "fail".equals(resp.getPayStatus())) {
					refund.setStatus(CollectStatus.FAIL.getValue());
					refund.setErrMsg(resp.getPayFailMessage());
				}
				refund.setTunnelOrderNo(resp.getOrderNo());
			} catch (CommonGatewayException e) {
				log.error("{}", JsonUtil.toJSON(e));
				refund.setErrCode(e.getBankErrCode());
				refund.setErrMsg(e.getBankErrMsg());
			} catch (Exception e) {
				log.error("", e);
			}
			walletRefundDao.updateByPrimaryKey(refund);
			return refund;
		}).collect(Collectors.toList());


	}


	/**
	 * 更新转账状态
	 */
	public Tuple<WalletApply, GatewayTrans> updatePayStatus(
		Tuple<WalletApply, GatewayTrans> applyTuple) {

//		applyTuples.forEach(tuple -> {
//			WalletApply walletApply = tuple.left;
//			GatewayTrans trans = tuple.right;
//			// 查询订单状态
//			GetOrderDetailReq req = GetOrderDetailReq.builder().bizOrderNo(trans.getAcceptNo())
//				.build();
//			trans.setStage(req.getServcieName() + "." + req.getMethodName());
//			try {
//				GetOrderDetailResp resp = yunstTpl.execute(req, GetOrderDetailResp.class);
//				YunstOrderStatus yunstStatus = EnumUtil
//					.parse(YunstOrderStatus.class, resp.getOrderStatus());
//				WalletApplyStatus status = yunstStatus.toApplyStatus();
//				walletApply.setStatus(status.getValue());
//				Date bizTime = DateUtil
//					.parse(resp.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN);
//				walletApply.setBizTime(bizTime);
//				walletApplyDao.updateByPrimaryKey(walletApply);
//
//				trans.setBizTime(bizTime);
//				trans.setEndTime(new Date());
//				gatewayTransService.updateTrans(trans);
//			} catch (CommonGatewayException e) {
//				walletApply.setStatus(WalletApplyStatus.WAIT_DEAL.getValue());
//				walletApplyDao.updateByPrimaryKey(walletApply);
//
//				trans.setErrCode(e.getBankErrCode());
//				trans.setSysErrMsg(e.getBankErrMsg());
//				gatewayTransService.updateTrans(trans);
//
//				log.info("", e);
//			} catch (Exception e) {
//				log.error("", e);
//			}
//
//		});
		return null;
	}

	/**
	 * 更新充值状态
	 */
	public void updateRechargeStatus(String orderNo) {
		WalletRecharge recharge = walletRechargeDao.selectByOrderNo(orderNo);

		GetOrderDetailResp tunnelOrder = queryOrderDetail(orderNo);
		if (!recharge.getTunnelOrderNo().equals(tunnelOrder.getOrderNo())) {
			log.error("渠道单号不匹配， recharge = {} , channelOrderNo = {}", recharge,
				tunnelOrder.getBizOrderNo());
			return;
		}
		recharge.setTunnelStatus(String.valueOf(tunnelOrder.getOrderStatus()));
		recharge.setTunnelSuccTime(
			DateUtil.parse(tunnelOrder.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN));
		recharge.setErrMsg(tunnelOrder.getErrorMessage());
		YunstOrderStatus orderStatus = EnumUtil
			.parse(YunstOrderStatus.class, tunnelOrder.getOrderStatus());
		CollectStatus applyStatus = orderStatus.toUniStatus();
		recharge.setStatus(applyStatus.getValue());
		recharge.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			recharge.setEndTime(new Date());
		}
		walletRechargeDao.updateByPrimaryKey(recharge);
	}

	/**
	 * 更新代收状态
	 */
	public void updateCollectStatus(String orderNo) {
		WalletCollect collect = walletCollectDao.selectByOrderNo(orderNo);

		GetOrderDetailResp channelOrder = queryOrderDetail(orderNo);
		if (!collect.getTunnelOrderNo().equals(channelOrder.getOrderNo())) {
			log.error("渠道单号不匹配， collect = {} , channelOrderNo = {}", collect,
				channelOrder.getBizOrderNo());
			return;
		}
		collect.setTunnelStatus(String.valueOf(channelOrder.getOrderStatus()));
		collect.setTunnelSuccTime(
			DateUtil.parse(channelOrder.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN));
		collect.setErrMsg(channelOrder.getErrorMessage());
		YunstOrderStatus orderStatus = EnumUtil
			.parse(YunstOrderStatus.class, channelOrder.getOrderStatus());
		CollectStatus applyStatus = orderStatus.toUniStatus();
		collect.setStatus(applyStatus.getValue());
		collect.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			collect.setEndTime(new Date());
		}
		walletCollectDao.updateByPrimaryKey(collect);
	}

	/**
	 * 更新代付状态
	 */
	public void updateAgentPayStatus(String orderNo) {
		WalletClearing clearing = walletClearingDao.selectByOrderNo(orderNo);

		GetOrderDetailResp channelOrder = queryOrderDetail(orderNo);
		if (!clearing.getTunnelOrderNo().equals(channelOrder.getOrderNo())) {
			log.error("渠道单号不匹配， clear = {} , channelOrderNo = {}", clearing,
				channelOrder.getBizOrderNo());
			return;
		}
		clearing.setTunnelStatus(String.valueOf(channelOrder.getOrderStatus()));
		clearing.setTunnelSuccTime(
			DateUtil.parse(channelOrder.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN));
		clearing.setErrMsg(channelOrder.getErrorMessage());
		YunstOrderStatus orderStatus = EnumUtil
			.parse(YunstOrderStatus.class, channelOrder.getOrderStatus());
		CollectStatus applyStatus = orderStatus.toUniStatus();
		clearing.setStatus(applyStatus.getValue());
		clearing.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			clearing.setEndTime(new Date());
		}
		walletClearingDao.updateByPrimaryKey(clearing);
	}

	/**
	 * 更新退款状态
	 */
	public void updateRefundStatus(String orderNo) {
		WalletRefund refund = walletRefundDao.selectByOrderNo(orderNo);

		GetOrderDetailResp channelOrder = queryOrderDetail(orderNo);
		if (!refund.getTunnelOrderNo().equals(channelOrder.getOrderNo())) {
			log.error("渠道单号不匹配， refund = {} , channelOrderNo = {}", refund,
				channelOrder.getBizOrderNo());
			return;
		}
		refund.setTunnelStatus(String.valueOf(channelOrder.getOrderStatus()));
		refund.setTunnelSuccTime(
			DateUtil.parse(channelOrder.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN));
		refund.setErrMsg(channelOrder.getErrorMessage());
		YunstOrderStatus orderStatus = EnumUtil
			.parse(YunstOrderStatus.class, channelOrder.getOrderStatus());
		CollectStatus applyStatus = orderStatus.toUniStatus();
		refund.setStatus(applyStatus.getValue());
		refund.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			refund.setEndTime(new Date());
		}
		walletRefundDao.updateByPrimaryKey(refund);
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
				BankCard bankCard = BankCard.builder()
					.bankCardNo(bankCardNo)
					.amount(m.getAmount())
					.build();
				payMethod.put(CollectPayMethod.BankCard.KEY_CodePayVsp, bankCard);
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
			log.error(" error = {} , req = {} ", req, e);
			throw new RuntimeException();
		}
	}

}
