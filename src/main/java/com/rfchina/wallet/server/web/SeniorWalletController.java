package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.SeniorWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SeniorWalletController {

	public static final ObjectSetter<ObjectMapper> DEF_REQ_OBJ_MAP = objectMapper -> {
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	};

	@Autowired
	private SeniorWalletService seniorWalletService;

	@ApiOperation("高级钱包-充值")
	@PostMapping(UrlConstant.SENIOR_WALLET_RECHARGE)
	public ResponseValue recharge(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "充值内容，参考RechargeReq结构体", required = true) @RequestParam("recharge_req") String rechargeReq
	) {

		RechargeReq req = JsonUtil.toObject(rechargeReq, RechargeReq.class, DEF_REQ_OBJ_MAP);
		seniorWalletService.recharge(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-定时代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_ASYNC)
	public ResponseValue<WalletCollect> collectAsync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		WalletCollect collect = seniorWalletService.preCollect(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, collect);
	}

	@ApiOperation("高级钱包-即刻代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_SYNC)
	public ResponseValue<WalletCollect> collectSync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		WalletCollect walletCollect = seniorWalletService.preCollect(accessToken, req);
		WalletCollect result = seniorWalletService.doCollect(accessToken, walletCollect);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-代收结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_QUERY)
	public ResponseValue<WalletCollect> collectQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单号", required = true) @RequestParam("collect_order_no") String collectOrderNo
	) {
		WalletCollect collect = seniorWalletService.queryCollect(accessToken, collectOrderNo);
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
		seniorWalletService.agentPay(accessToken, collectOrderNo, req.getReceivers());
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-代付结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_AGENT_PAY_QUERY)
	public ResponseValue<WalletClearing> agentPayQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代付单号", required = true) @RequestParam(value = "pay_order_no") String payOrderNo
	) {
		WalletClearing walletClearings = seniorWalletService.agentPayQuery(accessToken, payOrderNo);
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
		WalletRefund refund = seniorWalletService.refund(accessToken, collectOrderNo, rList);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}

	@ApiOperation("高级钱包-退款结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND_QUERY)
	public ResponseValue<WalletRefund> refundQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "退款单号", required = true) @RequestParam("refund_order_no") String refundOrderNo
	) {
		WalletRefund refund = seniorWalletService.refundQuery(accessToken, refundOrderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}


}
