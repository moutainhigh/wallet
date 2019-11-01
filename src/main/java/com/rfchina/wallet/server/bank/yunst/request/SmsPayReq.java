package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "4.2.8 确认支付（后台+短信验证码确认）")
@Getter
@Builder
public class SmsPayReq implements YunstBaseReq {

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "pay";
	}

	@ApiModelProperty(required = true, value = "商户系统用户标识")
	private String bizUserId;

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(required = false, value = "交易编号")
	private String tradeNo;

	@ApiModelProperty(required = true, value = "短信验证")
	private String verificationCode;

	@ApiModelProperty(required = true, value = "ip 地址")
	private String consumerIp;

}
