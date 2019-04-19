package com.rfchina.wallet.server.bank.pudong.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriPayQuery53ReqBody {

	@ApiModelProperty(value = "交易客户号", required = true)
	private String transMasterID;

	@ApiModelProperty(value = "项目编号", required = true)
	private String projectNumber;

	@ApiModelProperty(value = "费项编码", required = false)
	private String costItemCode;

	@ApiModelProperty(value = "电子凭证号", required = false)
	private String elecChequeNo;

	@ApiModelProperty(value = "受理编号", required = false)
	private String handleSeqNo;

	@ApiModelProperty(value = "起始日期", required = true)
	private String beginDate;

	@ApiModelProperty(value = "截止日期", required = true)
	private String endDate;

	@ApiModelProperty(value = "查询笔数,最高20笔", required = true)
	private String queryNumber;

	@ApiModelProperty(value = "起始笔数", required = true)
	private String beginNumber;
}
