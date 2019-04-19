package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PriPayRespBody {

	@ApiModelProperty(value = "项目编号")
	private String projectNumber;

	@ApiModelProperty(value = "费项编码")
	private String costItemCode;

	@ApiModelProperty(value = "交易类型编码 1:代收 2:代付")
	private String transTypeCode;

	@ApiModelProperty(value = "电子凭证号")
	private String elecChequeNo;

	@ApiModelProperty(value = "受理编号")
	private String handleSeqNo;

	@ApiModelProperty(value = "处理日期")
	private String handleDate;

}
