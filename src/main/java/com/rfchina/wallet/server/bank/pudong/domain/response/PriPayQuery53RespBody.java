package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PriPayQuery53RespBody {
	@ApiModelProperty(value = "交易日期", required = true)
	private String transDate;

	@ApiModelProperty(value = "项目编号", required = true)
	private String projectNumber;

	@ApiModelProperty(value = "项目名称", required = true)
	private String projectName;

	@ApiModelProperty(value = "单位名称", required = true)
	private String unitName;

	@ApiModelProperty(value = "记录总数", required = true)
	private String recordNumber;

	@ApiModelProperty(value = "循环内容", required = true)
	private Lists lists;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Lists {
		private List<PriPayQuery53RespWrapper> list;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PriPayQuery53RespWrapper{
		@ApiModelProperty(value = "费项编码", required = true)
		private String costItemCode;

		@ApiModelProperty(value = "交易类型编码.1:代收 2:代付", required = true)
		private String transTypeCode;

		@ApiModelProperty(value = "电子凭证号", required = true)
		private String elecChequeNo;

		@ApiModelProperty(value = "受理编号")
		private String handleSeqNo;

		@ApiModelProperty(value = "订单受理日期")
		private String orderHandleDate;

		@ApiModelProperty(value = "批次处理状态 0:成功；1:失败；2:处理中；3:异常（需管理端处理）")
		private String batchHandleStatus;

		@ApiModelProperty(value = "总笔数")
		private  String totalNumber;

		@ApiModelProperty(value = "总金额")
		private String totalAmount;
	}

}
