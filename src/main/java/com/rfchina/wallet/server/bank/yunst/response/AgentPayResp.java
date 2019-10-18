package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AgentPayResp {

	@ApiModelProperty(value = "托管代付状态")
	private String payStatus;

	@ApiModelProperty(value = "支付失败信息")
	private String payFailMessage;


	@ApiModelProperty(value = "通商云订单号")
	private String orderNo;

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(value = "代付去向 1:到账户余额 2:到银行卡")
	private Long payWhereabouts;

	@ApiModelProperty(value = "最多50个字符,商户拓展参数,用于透传给商户")
	private String extendInfo;
}

