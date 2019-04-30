package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriPayQuery54RespBody {

	@ApiModelProperty(value = "项目编号")
	private String projectNumber;

	@ApiModelProperty(value = "项目名称")
	private String projectName;

	@ApiModelProperty(value = "费项编码")
	private String costItemCode;

	@ApiModelProperty(value = "费项名称")
	private String costItemName;

	@ApiModelProperty(value = "企业名称")
	private String entName;

	@ApiModelProperty(value = "电子凭证号")
	private String electronNumber;

	@ApiModelProperty(value = "受理编号")
	private String handleSeqNo;

	@ApiModelProperty(value = "结算账号")
	private String acctNo;

	@ApiModelProperty(value = "结算账号名称")
	private String acctName;

	@ApiModelProperty(value = "交易日期")
	private String transDate;

	@ApiModelProperty(value = "批次号")
	private String batchNo;

	@ApiModelProperty(value = "交易类型")
	private String transType;

	@ApiModelProperty(value = "总笔数")
	private String totalNumber;

	@ApiModelProperty(value = "总金额")
	private String totalAmount;

	@ApiModelProperty(value = "成功笔数")
	private String successNum;

	@ApiModelProperty(value = "成功金额")
	private String successAmount;

	@ApiModelProperty(value = "失败笔数")
	private String failureNum;

	@ApiModelProperty(value = "失败金额")
	private String failureAmount;

	@ApiModelProperty(value = "循环内容")
	private Lists lists;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Lists {

		private List<PriPayReqWrapper> list;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PriPayReqWrapper {

		@ApiModelProperty(value = "明细内容。属性值关联至PriPayReq.toString", required = true)
		private String detailedContent;
	}

}
