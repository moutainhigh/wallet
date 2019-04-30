package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayInResp {

	@ApiModelProperty(value = "钱包批次号")
	private String batchNo;

	@ApiModelProperty(value = "受理编号", hidden = true)
	private String acceptNo;

}
