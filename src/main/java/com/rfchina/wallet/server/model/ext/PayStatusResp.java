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
	@ApiModelProperty(value = "业务凭证号")
	private String bizNo;

	@ApiModelProperty(value = "钱包批次号")
	private String batchNo;

	@ApiModelProperty(value = "交易日期")
	private String transDate;

	@ApiModelProperty(value = "支付金额")
	private Long amount;

	@ApiModelProperty(value = "交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销")
	private Byte status;

	@ApiModelProperty(value = "失败原因")
	private String errMsg;
}
