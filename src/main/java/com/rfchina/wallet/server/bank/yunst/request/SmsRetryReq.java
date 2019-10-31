package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "4.2.25 重发支付短信验证码")
@Getter
@Builder
public class SmsRetryReq implements YunstBaseReq {

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "resendPaySMS";
	}
}
