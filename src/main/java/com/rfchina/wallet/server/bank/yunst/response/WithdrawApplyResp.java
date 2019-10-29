package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class WithdrawApplyResp {

	@ApiModelProperty(value = "支付状态")
	private String payStatus;

	@ApiModelProperty(value = "支付失败信息")
	private String payFailMessage;

	@ApiModelProperty(value = "商户系统用户标识 仅交易验证方式为“0”时返回")
	private String bizUserId;

	@ApiModelProperty(value = "通商云订单号")
	private String orderNo;

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(value = "扩展参数")
	private String extendInfo;

}
