package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreBindCardVo {

	@ApiModelProperty("钱包id")
	private Long walletId;

	@ApiModelProperty("银行卡卡号")
	private String cardNo;

	@ApiModelProperty("银行预留手机号")
	private String phone;

	@ApiModelProperty("验证银行卡申请时间")
	private String transDate;

	@ApiModelProperty("验证银行卡流水号")
	private String transNum;

	@ApiModelProperty("信用卡cvv2码")
	private String cvv2;

	@ApiModelProperty("信用卡到期4位日期")
	private String validate;

	@ApiModelProperty("银行卡类型")
	private Byte cardType;

	@ApiModelProperty("银行类型")
	private String bankCode;
}
