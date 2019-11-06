package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "4.2.10 确认支付（前台+密码验证版）")
@Getter
@Builder
public class PwdConfirmReq implements YunstBaseReq {

	@ApiModelProperty(required = true, value = "商户系统用户标识")
	private String bizUserId;

	@ApiModelProperty(required = true, value = "订单申请的商户订单号")
	private String bizOrderNo;

	@ApiModelProperty(required = false, value = "确认支付之后，跳转返回")
	private String jumpUrl;

	@ApiModelProperty(required = true, value = "ip 地址")
	private String consumerIp;


	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "pay";
	}
}
