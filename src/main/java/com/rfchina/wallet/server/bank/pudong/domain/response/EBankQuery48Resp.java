package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EBankQuery48Resp {

	@ApiModelProperty(value = "包号")
	private String packageNo;

	@ApiModelProperty(value = "网银受理编号")
	private String entJnlSeqNo;

	@ApiModelProperty(value = "授权流水号")
	private String authJnlSeqNo;

	@ApiModelProperty(value = "核心返回流水号")
	private String hostJnlSeqNo;

	@ApiModelProperty(value = "交易日期")
	private String transDate;

	@ApiModelProperty(value = "交易状态 0－交易成功 1－通讯失败 2－主机拒绝 3－网银拒绝 4－授权拒绝 5－交易录入，待授权 9－待处理 Z－交易提交成功 Y－交易提交不成功")
	private String transStatus;

	@ApiModelProperty(value = "失败原因")
	private String failCode;

	@ApiModelProperty(value = "总金额")
	private String totalAmount;

	@ApiModelProperty(value = "总笔数")
	private String totalNumber;

	@ApiModelProperty(value = "授权拒绝笔数")
	private String rejectNumber;

	@ApiModelProperty(value = "授权拒绝金额")
	private String rejectAmount;
}
