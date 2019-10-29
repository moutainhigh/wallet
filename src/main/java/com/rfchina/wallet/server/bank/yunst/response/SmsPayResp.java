package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SmsPayResp {

	@ApiModelProperty(value = "支付状态")
	private String payStatus;

	@ApiModelProperty(value = "支付失败信息")
	private String payFailMessage;

	@ApiModelProperty(value = "商户系统用户标识，")
	private String bizUserId ;

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;


}
