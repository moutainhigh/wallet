package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "4.2.7 批量托管代付(标准版)")
public class BatchAgentPayResp {

	@ApiModelProperty(required = true, value = "商户批次号")
	private String bizBatchNo;
}
