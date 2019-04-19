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

	@ApiModelProperty(value = "受理编号")
	private String acceptNo;

	@ApiModelProperty(value = "柜员流水号")
	private String seqNo;

	@ApiModelProperty(value = "成功笔数")
	private String successCount;

	@ApiModelProperty(value = "失败笔数")
	private String failCount;
}
