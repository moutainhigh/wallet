package com.rfchina.wallet.server.service.handler.yunst;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletLevel;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.domain.model.WalletClearInfo;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.yunst.request.AgentPayReq;
import com.rfchina.wallet.server.bank.yunst.request.AgentPayReq.CollectPay;
import com.rfchina.wallet.server.bank.yunst.request.BatchAgentPayReq;
import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq;
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
import com.rfchina.wallet.server.bank.yunst.response.BatchAgentPayResp;
import com.rfchina.wallet.server.bank.yunst.response.CollectApplyResp;
import com.rfchina.wallet.server.bank.yunst.response.GetOrderDetailResp;
import com.rfchina.wallet.server.bank.yunst.response.RefundApplyResp;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectStatus;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.RefundType;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.msic.EnumWallet.UniProgress;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyType;
import com.rfchina.wallet.server.msic.EnumWallet.YunstOrderStatus;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.GatewayTransService;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import java.util.ArrayList;
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
	private WalletRechargeExtDao walletRechargeDao;

	@Autowired
	private ConfigService configService;


	public boolean isSupportWalletLevel(Byte walletLevel) {
		return EnumWalletLevel.SENIOR.getValue().byteValue() == walletLevel;
	}


	public GatewayMethod getGatewayMethod() {
		return null;
	}

	public Tuple<GatewayMethod, PayTuple> transfer(Long applyId) {
		return null;
	}

	public PayStatusResp onAskErr(WalletApply walletLog, IGatewayError err) {
		return null;
	}

	/**
	 * 充值
	 */
	public List<WalletRecharge> recharge(Long applyId) {
		WalletApply walletApply = walletApplyDao.selectByPrimaryKey(applyId);
		List<WalletRecharge> recharges = walletRechargeDao.selectByApplyId(applyId);
		List<WalletRecharge> result = new ArrayList<>();

		for (WalletRecharge recharge : recharges) {

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
				.validateType(0L)
				.frontUrl(null)
				.backUrl(configService.getRechargeRecallUrl())
				.orderExpireDatetime(expireTime)
				.payMethod(getMethodMap(methods, true))
				.industryCode("1910")
				.industryName("其他")
				.source(1L)
				.build();

			try {
				CollectApplyResp resp = yunstTpl.execute(req, CollectApplyResp.class);
				if (StringUtils.isNotBlank(resp.getPayStatus())) {
					if ("success".equals(resp.getPayStatus())) {
						recharge.setStatus(CollectStatus.SUCC.getValue());
					} else if ("fail".equals(resp.getPayStatus())) {
						recharge.setStatus(CollectStatus.FAIL.getValue());
					}
					recharge.setTunnelOrderNo(resp.getOrderNo());
					recharge.setStartTime(new Date());
					recharge.setProgress(UniProgress.SENDED.getValue());
					walletRechargeDao.updateByPrimaryKey(recharge);
				}

				result.add(recharge);
			} catch (Exception e) {
				log.error("{}", JSON.toJSONString(e));
			}

		}

		return result;

	}

	/**
	 * 代收
	 */
	public List<WalletCollect> collect(Long applyId) {
		List<WalletCollect> collects = walletCollectDao.selectByApplyId(applyId);
		List<WalletCollect> result = new ArrayList<>();
		// 付款人
		for (WalletCollect collect : collects) {
			List<WalletClearInfo> clears = walletClearInfoDao.selectByCollectId(collect.getId());
			// 收款人
			List<RecieveInfo> receives = clears.stream().map(clear -> {
				WalletChannel receiver = walletChannelDao
					.selectByWalletId(clear.getWalletId(), TunnelType.YUNST.getValue());
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
				.backUrl(configService.getRechargeRecallUrl())
				.ordErexpireDatetime(expireTime)
				.payMethod(getMethodMap(methods, false))
				.tradeCode("3001")
				.industryCode("1910")
				.industryName("其他")
				.source(1L)
				.build();

			try {
				CollectApplyResp resp = yunstTpl.execute(req, CollectApplyResp.class);
				if (StringUtils.isNotBlank(resp.getPayStatus())) {
					if ("success".equals(resp.getPayStatus())) {
						collect.setStatus(CollectStatus.SUCC.getValue());
					} else if ("fail".equals(resp.getPayStatus())) {
						collect.setStatus(CollectStatus.FAIL.getValue());
					}
					collect.setProgress(UniProgress.RECEIVED.getValue());
				} else {
					collect.setProgress(UniProgress.SENDED.getValue());
				}
				collect.setTunnelOrderNo(resp.getOrderNo());
				collect.setStartTime(new Date());
				walletCollectDao.updateByPrimaryKey(collect);
				result.add(collect);

			} catch (Exception e) {
				log.error("{}", JsonUtil.toJSON(e));
			}

		}

		return result;
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
			}
		});
		return payMethod;
	}

	/**
	 * 代付
	 */
	public void agentPay(Long applyId) {

		List<WalletClearing> walletClearings = walletClearingDao.selectByApplyId(applyId);
		// 登记代付数额
		List<AgentPayReq> batchPayList = walletClearings.stream().map(clear -> {
			CollectPay collectPay = CollectPay.builder()
				.bizOrderNo("WP" + clear.getCollectId())
				.amount(clear.getAmount())
				.build();
			WalletChannel tunnel = walletChannelExtDao
				.selectByWalletId(clear.getWalletId(), TunnelType.YUNST.getValue());
			return AgentPayReq.builder()
				.bizOrderNo("WC" + clear.getId())
				.collectPayList(Lists.newArrayList(collectPay))
				.bizUserId(tunnel.getBizUserId())
				.accountSetNo("100001")  // 产品需求代付到余额账户
				.backUrl("")
				.amount(clear.getAmount())
				.fee(0L)
				.extendInfo(null)
				.build();
		}).collect(Collectors.toList());

		WalletApply walletApply = walletApplyDao.selectByPrimaryKey(walletClearings.get(0).getId());
		BatchAgentPayReq req = BatchAgentPayReq.builder()
			.bizBatchNo(walletApply.getBatchNo())
			.batchPayList(batchPayList)
//			.tradeCode(TRADE_CODE_BUY)
			.build();

		try {
			yunstTpl.execute(req, BatchAgentPayResp.class);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException();
		}
	}

	/**
	 * 退款
	 */
	public void refund(Long applyId) {
		List<WalletRefund> refunds = walletRefundDao.selectByApplyId(applyId);
		WalletRefund refund = refunds.get(0);
		WalletChannel toChannel = walletChannelDao
			.selectByWalletId(refund.getToWalletId(), TunnelType.YUNST.getValue());
		List<RefundInfo> refundList = refunds.stream().map(r -> {
			WalletChannel fromChannel = walletChannelDao
				.selectByWalletId(r.getFromWalletId(), TunnelType.YUNST.getValue());
			return RefundInfo.builder()
				.accountSetNo("")
				.bizUserId(fromChannel.getBizUserId())
				.amount(r.getRefundAmount())
				.build();
		}).collect(Collectors.toList());
		RefundApplyReq req = RefundApplyReq.builder()
			.bizOrderNo("WR" + refund.getId())
			.oriBizOrderNo("WP" + refund.getCollectId())
			.bizUserId(toChannel.getBizUserId())
			.refundType(RefundType.D0.getValue())
			.refundList(refundList)
			.amount(
				refunds.stream().map(WalletRefund::getRefundAmount).reduce((x, y) -> x + y).get())
			.build();
		try {
			yunstTpl.execute(req, RefundApplyResp.class);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException();
		}

	}

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

	public void updateRechargeStatus(String orderNo) {
		WalletRecharge walletRecharge = walletRechargeDao.selectByOrderNo(orderNo);

		GetOrderDetailResp channelOrder = queryOrderDetail(orderNo);
		if (!walletRecharge.getTunnelOrderNo().equals(channelOrder.getOrderNo())) {
			log.error("渠道单号不匹配， recharge = {} , channelOrderNo = {}", walletRecharge,
				channelOrder.getBizOrderNo());
			return;
		}
		walletRecharge.setTunnelStatus(String.valueOf(channelOrder.getOrderStatus()));
		walletRecharge.setTunnelSuccTime(
			DateUtil.parse(channelOrder.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN));
		walletRecharge.setErrMsg(channelOrder.getErrorMessage());
		YunstOrderStatus orderStatus = EnumUtil
			.parse(YunstOrderStatus.class, channelOrder.getOrderStatus());
		CollectStatus applyStatus = orderStatus.toUniStatus();
		walletRecharge.setStatus(applyStatus.getValue());
		walletRecharge.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			walletRecharge.setEndTime(new Date());
		}
		walletRechargeDao.updateByPrimaryKey(walletRecharge);
	}

	public void updateCollectStatus(String orderNo) {
		WalletCollect walletCollect = walletCollectDao.selectByOrderNo(orderNo);

		GetOrderDetailResp channelOrder = queryOrderDetail(orderNo);
		if (!walletCollect.getTunnelOrderNo().equals(channelOrder.getOrderNo())) {
			log.error("渠道单号不匹配， collect = {} , channelOrderNo = {}", walletCollect,
				channelOrder.getBizOrderNo());
			return;
		}
		walletCollect.setTunnelStatus(String.valueOf(channelOrder.getOrderStatus()));
		walletCollect.setTunnelSuccTime(
			DateUtil.parse(channelOrder.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN));
		walletCollect.setErrMsg(channelOrder.getErrorMessage());
		YunstOrderStatus orderStatus = EnumUtil
			.parse(YunstOrderStatus.class, channelOrder.getOrderStatus());
		CollectStatus applyStatus = orderStatus.toUniStatus();
		walletCollect.setStatus(applyStatus.getValue());
		walletCollect.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			walletCollect.setEndTime(new Date());
		}
		walletCollectDao.updateByPrimaryKey(walletCollect);
	}

	public void updateAgentPayStatus(String orderNo) {
		WalletClearing walletClearing = walletClearingDao.selectByOrderNo(orderNo);

		GetOrderDetailResp channelOrder = queryOrderDetail(orderNo);
		if (!walletClearing.getTunnelOrderNo().equals(channelOrder.getOrderNo())) {
			log.error("渠道单号不匹配， clear = {} , channelOrderNo = {}", walletClearing,
				channelOrder.getBizOrderNo());
			return;
		}
		walletClearing.setTunnelStatus(String.valueOf(channelOrder.getOrderStatus()));
		walletClearing.setTunnelSuccTime(
			DateUtil.parse(channelOrder.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN));
		walletClearing.setErrMsg(channelOrder.getErrorMessage());
		YunstOrderStatus orderStatus = EnumUtil
			.parse(YunstOrderStatus.class, channelOrder.getOrderStatus());
		CollectStatus applyStatus = orderStatus.toUniStatus();
		walletClearing.setStatus(applyStatus.getValue());
		walletClearing.setProgress(UniProgress.RECEIVED.getValue());
		if (applyStatus.isEndStatus()) {
			walletClearing.setEndTime(new Date());
		}
		walletClearingDao.updateByPrimaryKey(walletClearing);
	}

	private GetOrderDetailResp queryOrderDetail(String orderNo) {

		GetOrderDetailReq req = GetOrderDetailReq.builder().bizOrderNo(orderNo).build();
		try {
			return yunstTpl.execute(req, GetOrderDetailResp.class);
		} catch (Exception e) {
			log.error(" error = {} , req = {} ", req, e);
			throw new RuntimeException();
		}
	}

//	/**
//	 * 查询余额
//	 */
//	public YunstQueryBalanceResult queryBalance(String bizUserId, Integer type, String accountSetNo)
//		throws Exception {
//		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
//		YunstQueryBalanceReq req = YunstQueryBalanceReq.builder$()
//			.bizUserId(bizUserId)
//			.accountSetNo(accountSetNo)
//			.build();
//
//		return yunstTpl.execute(req, YunstQueryBalanceResult.class);
//	}

}
