package com.rfchina.wallet.server.bank.pudong.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriPayQuery54ReqBody {

	@ApiModelProperty(value = "交易客户号", required = true)
	private String transMasterID;

	@ApiModelProperty(value = "项目编号", required = true)
	private String projectNumber;

	@ApiModelProperty(value = "费项编码", required = false)
	private String costItemCode;

	@ApiModelProperty(value = "交易日期",required = true)
	private String transDate;

	@ApiModelProperty(value = "电子凭证号", required = false)
	private String elecChequeNo;

	@ApiModelProperty(value = "受理编号", required = false)
	private String handleSeqNo;


}
