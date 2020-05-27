package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pos {

	@ApiModelProperty(name = "pay_type", value = "51：银行卡快捷支付")
	private Byte payType;

	@ApiModelProperty(value = "集团模式：收银宝子商户号", required = true)
	private String vspCusid;

	@ApiModelProperty(required = true, value = "支付金额")
	private Long amount;

}
