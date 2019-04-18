package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayStatusResp {
	@ApiModelProperty(value = "电子凭证号")
	private String elecChequeNo;

	@ApiModelProperty(value = "受理编号")
	private String acceptNo;

	@ApiModelProperty(value = "交易日期")
	private String transDate;

	@ApiModelProperty(value = "支付金额")
	private Long amount;

	@ApiModelProperty(value = "支付状态。1：受理中，2：交易成功。3：交易失败")
	private Byte status;

	@ApiModelProperty(value = "失败原因")
	private String errMsg;
}
