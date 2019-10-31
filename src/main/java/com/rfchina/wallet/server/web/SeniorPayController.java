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
import com.rfchina.wallet.domain.model.WalletOrder;
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
import com.rfchina.wallet.server.model.ext.WithdrawResp;
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
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "银行卡id", required = true) @RequestParam("card_id") Long cardId,
		@ApiParam(value = "金额", required = true) @RequestParam("amount") Long amount
	) {

		RechargeResp result = seniorPayApi.recharge(accessToken, walletId, cardId, amount);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-提现")
	@PostMapping(UrlConstant.SENIOR_WALLET_WITHDRAW)
	public ResponseValue<WithdrawResp> withdraw(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "银行卡id", required = true) @RequestParam("card_id") Long cardId,
		@ApiParam(value = "金额", required = true) @RequestParam("amount") Long amount) {

		WithdrawResp result = seniorPayApi.withdraw(accessToken, walletId, cardId, amount);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-即刻代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_SYNC)
	public ResponseValue<WalletCollectResp> collectSync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		WalletCollectResp result = seniorPayApi.collect(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-代付")
	@PostMapping(UrlConstant.SENIOR_WALLET_AGENT_PAY)
	public ResponseValue<SettleResp> agentPay(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务方单号", required = true) @RequestParam(value = "biz_no") String bizNo,
		@ApiParam(value = "原代收单号", required = true) @RequestParam(value = "collect_order_no") String collectOrderNo,
		@ApiParam(value = "代付列表（与代收的分账规则对应），参考AgentPayReq结构体", required = true) @RequestParam("agent_pay_req") String agentPayReq
	) {
		AgentPayReq req = JsonUtil.toObject(agentPayReq, AgentPayReq.class, DEF_REQ_OBJ_MAP);
		seniorPayApi.agentPay(accessToken, bizNo, collectOrderNo, req.getReceivers());
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-退款")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND)
	public ResponseValue<WalletOrder> refund(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务方单号", required = true) @RequestParam(value = "biz_no") String bizNo,
		@ApiParam(value = "代收单号", required = true) @RequestParam("collect_order_no") String collectOrderNo,
		@ApiParam(value = "退款清单，参考List<RefundInfo>结构体", required = true) @RequestParam("refund_list") String refundList
	) {
		List<RefundInfo> rList = JsonUtil.toArray(refundList, RefundInfo.class, DEF_REQ_OBJ_MAP);
		WalletOrder refund = seniorPayApi.refund(accessToken, bizNo, collectOrderNo, rList);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}

	@ApiOperation("高级钱包-订单结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_ORDER_QUERY)
	public ResponseValue<WalletOrder> orderQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "统一订单号", required = true) @RequestParam("order_no") String orderNo
	) {
		WalletOrder order = seniorPayApi.orderQuery(accessToken, orderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, order);
	}

	@ApiOperation("高级钱包-短信确认")
	@PostMapping(UrlConstant.SENIOR_WALLET_SMS_CONFIRM)
	public ResponseValue smsConfirm(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务令牌", required = true) @RequestParam("ticket") String ticket,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode,
		@ApiParam(value = "客户ip", required = true) @RequestParam("customer_ip") String customerIp
	) {

		seniorPayApi.smsConfirm(accessToken, ticket, verifyCode, customerIp);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-重发短信")
	@PostMapping(UrlConstant.SENIOR_WALLET_SMS_RETRY)
	public ResponseValue smsRetry(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务令牌", required = true) @RequestParam("ticket") String ticket
	) {

		seniorPayApi.smsRetry(accessToken, ticket);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

}
