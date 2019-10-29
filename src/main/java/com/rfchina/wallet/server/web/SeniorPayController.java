package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SeniorPayController {

	public static final ObjectSetter<ObjectMapper> DEF_REQ_OBJ_MAP = objectMapper -> {
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.STANDARD_DTAETIME_PATTERN));
	};

	@Autowired
	private SeniorPayApi seniorPayApi;

	@ApiOperation("高级钱包-充值")
	@PostMapping(UrlConstant.SENIOR_WALLET_RECHARGE)
	public ResponseValue<RechargeResp> recharge(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "充值内容，参考RechargeReq结构体", required = true) @RequestParam("recharge_req") String rechargeReq
	) {

		RechargeReq req = JsonUtil.toObject(rechargeReq, RechargeReq.class, DEF_REQ_OBJ_MAP);
		seniorPayApi.recharge(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-充值确认")
	@PostMapping(UrlConstant.SENIOR_WALLET_RECHARGE_CONFIRM)
	public ResponseValue<RechargeResp> rechargeConfirm(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "充值业务令牌", required = true) @RequestParam("recharge_ticket") String rechargeTicket,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode,
		@ApiParam(value = "客户ip", required = true) @RequestParam("customer_ip") String customerIp
	) {

		seniorPayApi.rechargeConfirm(accessToken, rechargeTicket, verifyCode, customerIp);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}


	@ApiOperation("高级钱包-提现")
	@PostMapping(UrlConstant.SENIOR_WALLET_WITHDRAW)
	public ResponseValue withdraw(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "充值内容，参考WithdrawReq结构体", required = true) @RequestParam("withdraw_req") String withdrawReq
	) {

		WithdrawReq req = JsonUtil.toObject(withdrawReq, WithdrawReq.class, DEF_REQ_OBJ_MAP);
		seniorPayApi.withdraw(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-定时代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_ASYNC)
	public ResponseValue<WalletCollect> collectAsync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		WalletCollect collect = seniorPayApi.preCollect(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, collect);
	}

	@ApiOperation("高级钱包-即刻代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_SYNC)
	public ResponseValue<WalletCollectResp> collectSync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		WalletCollect walletCollect = seniorPayApi.preCollect(accessToken, req);
		WalletCollectResp result = seniorPayApi.doCollect(accessToken, walletCollect);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-代收结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_QUERY)
	public ResponseValue<WalletCollect> collectQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单号", required = true) @RequestParam("collect_order_no") String collectOrderNo
	) {
		WalletCollect collect = seniorPayApi.queryCollect(accessToken, collectOrderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, collect);
	}

	@ApiOperation("高级钱包-代付")
	@PostMapping(UrlConstant.SENIOR_WALLET_AGENT_PAY)
	public ResponseValue<SettleResp> agentPay(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单号", required = true) @RequestParam(value = "collect_order_no") String collectOrderNo,
		@ApiParam(value = "代付列表（与代收的分账规则对应），参考AgentPayReq结构体", required = true) @RequestParam("agent_pay_req") String agentPayReq
	) {
		AgentPayReq req = JsonUtil.toObject(agentPayReq, AgentPayReq.class, DEF_REQ_OBJ_MAP);
		seniorPayApi.agentPay(accessToken, collectOrderNo, req.getReceivers());
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-代付结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_AGENT_PAY_QUERY)
	public ResponseValue<WalletClearing> agentPayQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代付单号", required = true) @RequestParam(value = "pay_order_no") String payOrderNo
	) {
		WalletClearing walletClearings = seniorPayApi.agentPayQuery(accessToken, payOrderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletClearings);
	}

	@ApiOperation("高级钱包-退款")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND)
	public ResponseValue<WalletRefund> refund(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单号", required = true) @RequestParam("collect_order_no") String collectOrderNo,
		@ApiParam(value = "退款清单，参考List<RefundInfo>结构体", required = true) @RequestParam("refund_list") String refundList
	) {
		List<RefundInfo> rList = JsonUtil.toArray(refundList, RefundInfo.class, DEF_REQ_OBJ_MAP);
		WalletRefund refund = seniorPayApi.refund(accessToken, collectOrderNo, rList);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}

	@ApiOperation("高级钱包-退款结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND_QUERY)
	public ResponseValue<WalletRefund> refundQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "退款单号", required = true) @RequestParam("refund_order_no") String refundOrderNo
	) {
		WalletRefund refund = seniorPayApi.refundQuery(accessToken, refundOrderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}


}
