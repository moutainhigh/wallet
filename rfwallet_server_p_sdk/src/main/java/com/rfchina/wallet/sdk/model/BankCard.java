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
public class BankCard {

	@ApiModelProperty(name = "pay_type", value = "51：银行卡快捷支付")
	private Byte payType;

	@ApiModelProperty(value = "银行卡号")
	private String bankCardNo;

	@ApiModelProperty(value = "渠道出资额(单位分)")
	private Long amount;

	@ApiModelProperty(value = "银行卡类型 1-储蓄卡 2-信用卡")
	private Byte cardType;
}

