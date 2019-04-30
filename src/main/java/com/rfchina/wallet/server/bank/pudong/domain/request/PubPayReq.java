package com.rfchina.wallet.server.bank.pudong.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubPayReq {

	@ApiModelProperty(value = "电子凭证号")
	private String elecChequeNo;

	@ApiModelProperty(value = "付款账号", required = true)
	private String acctNo;

	@ApiModelProperty(value = "付款人账户名称", required = true)
	private String acctName;

	@ApiModelProperty(value = "预约日期  格式：20041008")
	private String bespeakDate;

	@ApiModelProperty(value = "收款人账号", required = true)
	private String payeeAcctNo;

	@ApiModelProperty(value = "收款人名称", required = true)
	private String payeeName;

	@ApiModelProperty(value = "收款人账户类型 0-对公账号 1-卡")
	private String payeeType;

	@ApiModelProperty(value = "收款行名称")
	private String payeeBankName;

	@ApiModelProperty(value = "收款人地址")
	private String payeeAddress;

	@ApiModelProperty(value = "支付金额", required = true)
	private String amount;

	@ApiModelProperty(value = "本行/他行标志 0：表示本行 1：表示他行", required = true)
	private String sysFlag;

	@ApiModelProperty(value = "同城异地标志 0：同城 1：异地", required = true)
	private String remitLocation;

	@ApiModelProperty(value = "附言")
	private String note;

	@ApiModelProperty(value = "收款行速选标志")
	private String payeeBankSelectFlag;

	@ApiModelProperty(value = "支付号")
	private String payeeBankNo;

	@ApiModelProperty(value = "支付用途 收款人为个人客户时必须输入")
	private String payPurpose;
}
