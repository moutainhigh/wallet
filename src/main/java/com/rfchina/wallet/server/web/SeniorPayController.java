package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rfchina.biztools.limiter.annotation.EnumFixedRate;
import com.rfchina.biztools.limiter.setting.RateSetting;
import com.rfchina.biztools.limiter.setting.RateSettingUtil;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.model.BalanceJob;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.DeductionReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

	// 快捷参数配置工具，也可以自行构建
	public static RateSetting settingTemplate = RateSetting.builder()
		.limitWord("")
		.fixedLength(5)
		.fixedUnit(EnumFixedRate.SECONDS)
		.max(1)
		.overflow((setting) -> {
			throw new WalletResponseException(600001, "超过" + setting.getMax() + "次数限制");
		})
		.build();


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

		RechargeResp result = seniorPayApi
			.recharge(accessToken, walletId, cardId, amount);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-提现")
	@PostMapping(UrlConstant.SENIOR_WALLET_WITHDRAW)
	public ResponseValue<WithdrawResp> withdraw(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "银行卡id", required = true) @RequestParam("card_id") Long cardId,
		@ApiParam(value = "金额", required = true) @RequestParam("amount") Long amount,
		@ApiParam(value = "交易验证方式 0：无验证 1：短信 2：密码") @RequestParam("validate_type") Integer validateType,
		@ApiParam(value = "跳转地址", required = false) @RequestParam(value = "jump_url", required = false) String jumpUrl,
		@ApiParam(value = "客户Ip", required = true) @RequestParam(value = "customer_ip") String customerIp
	) {

		WithdrawResp result = seniorPayApi
			.withdraw(accessToken, walletId, cardId, amount, validateType, jumpUrl, customerIp);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-即刻代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_SYNC)
	public ResponseValue<WalletCollectResp> collectSync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq,
		@ApiParam(value = "跳转地址", required = false) @RequestParam(value = "jump_url", required = false) String jumpUrl,
		@ApiParam(value = "客户Ip", required = false) @RequestParam(value = "customer_ip", required = false) String customerIp
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		RateSetting rateSetting = RateSettingUtil.clone(req.getBizNo(), settingTemplate);
		WalletCollectResp result = seniorPayApi
			.collect(accessToken, req, jumpUrl, customerIp, rateSetting);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-单笔代付")
	@PostMapping(UrlConstant.SENIOR_WALLET_AGENT_PAY)
	public ResponseValue<SettleResp> agentPay(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务方单号", required = true) @RequestParam(value = "biz_no") String bizNo,
		@ApiParam(value = "原代收单号", required = true) @RequestParam(value = "collect_order_no") String collectOrderNo,
		@ApiParam(value = "代付列表（与代收的分账规则对应），参考AgentPayReq.Reciever结构体", required = true) @RequestParam("agent_pay_req") String agentPayReq,
		@ApiParam(value = "备注") @RequestParam(value = "note", required = false) String note
	) {
		AgentPayReq.Reciever reciever = JsonUtil
			.toObject(agentPayReq, AgentPayReq.Reciever.class, DEF_REQ_OBJ_MAP);
		RateSetting rateSetting = RateSettingUtil.clone(bizNo, settingTemplate);
		SettleResp settleResp = seniorPayApi
			.agentPay(accessToken, bizNo, collectOrderNo, reciever, note, rateSetting);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, settleResp);
	}

	@ApiOperation("高级钱包-退款")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND)
	public ResponseValue<WalletOrder> refund(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务方单号", required = true) @RequestParam(value = "biz_no") String bizNo,
		@ApiParam(value = "代收单号", required = true) @RequestParam("collect_order_no") String collectOrderNo,
		@ApiParam(value = "退款清单，参考List<RefundInfo>结构体", required = true) @RequestParam("refund_list") String refundList,
		@ApiParam(value = "备注") @RequestParam(value = "note", required = false) String note
	) {
		List<RefundInfo> rList = JsonUtil.toArray(refundList, RefundInfo.class, DEF_REQ_OBJ_MAP);
		RateSetting rateSetting = RateSettingUtil.clone(bizNo, settingTemplate);
		WalletOrder refund = seniorPayApi
			.refund(accessToken, bizNo, collectOrderNo, rList, note, rateSetting);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}

	@ApiOperation("高级钱包-代扣")
	@PostMapping(UrlConstant.SENIOR_WALLET_DEDUCTION)
	public ResponseValue<WalletCollectResp> deduction(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "消费内容，参考DeductionReq结构体", required = true) @RequestParam("deduction_req") String deductionReq
	) {

		DeductionReq req = JsonUtil.toObject(deductionReq, DeductionReq.class, DEF_REQ_OBJ_MAP);
		RateSetting rateSetting = RateSettingUtil.clone(req.getBizNo(), settingTemplate);
		WalletCollectResp result = seniorPayApi.deduction(accessToken, req, rateSetting);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
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


	@ApiOperation("高级钱包-对账文件")
	@PostMapping(UrlConstant.SENIOR_WALLET_BALANCE_FILE)
	public ResponseValue<BalanceJob> balanceFile(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "对账日期 yyyy-MM-dd", required = true) @RequestParam("balance_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date balanceDate
	) {

		BalanceJob job = seniorPayApi.balanceFile(accessToken, balanceDate);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, job);
	}
}
