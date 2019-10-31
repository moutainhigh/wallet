package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SmsRetryResp {

	@ApiModelProperty(value = "手机号码")
	private String phone;

	@ApiModelProperty(value = "通商云订单号")
	private String orderNo;

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;


}
