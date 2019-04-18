package com.rfchina.wallet.server.bank.pudong.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "AQ52 批量代收付交易")
public class PriPayReqBody {

	@ApiModelProperty(value = "交易客户号", required = true)
	private String transMasterID;

	@ApiModelProperty(value = "项目编号", required = true)
	private String projectNumber;

	@ApiModelProperty(value = "项目名称", required = true)
	private String projectName;

	@ApiModelProperty(value = "费项编码", required = true)
	private String costItemCode;

	@ApiModelProperty(value = "交易类型.1:代收 2:代付", required = true)
	private String transType;

	@ApiModelProperty(value = "电子凭证号", required = true)
	private String elecChequeNo;

	@ApiModelProperty(value = "是否指定唯一渠道.0:不指定渠道 1:指定渠道", required = false)
	private String onlyChannelFlag;

	@ApiModelProperty(value = "第三渠道ID", required = false)
	private String thirdChannelID;

	@ApiModelProperty(value = "总笔数", required = true)
	private String totalNumber;

	@ApiModelProperty(value = "总金额", required = true)
	private String totalAmount;

	@ApiModelProperty(value = "摘要", required = false)
	private String note;

	@ApiModelProperty(value = "用途或附言", required = false)
	private String purpose;

	@ApiModelProperty(value = "批次号", required = true)
	private String batchNo;

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
	public static class PriPayReqWrapper{

		@ApiModelProperty(value = "明细内容。属性值关联至PriPayReq.toString",required = true)
		private String detailedContent;
	}

}
