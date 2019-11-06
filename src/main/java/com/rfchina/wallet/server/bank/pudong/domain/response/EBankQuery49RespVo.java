package com.rfchina.wallet.server.bank.pudong.domain.response;

import com.rfchina.platform.biztool.mapper.string.StringIndex;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EBankQuery49RespVo {

	@StringIndex(1)
	@ApiModelProperty(value = "电子凭证号", required = true)
	private String elecChequeNo;

	@StringIndex(2)
	@ApiModelProperty(value = "申请日期 ", required = true)
	private String promiseDate;

	@StringIndex(3)
	@ApiModelProperty(value = "账号", required = true)
	private String acctNo;

	@StringIndex(4)
	@ApiModelProperty(value = "收款人账号", required = true)
	private String payeeAcctNo;

	@StringIndex(5)
	@ApiModelProperty(value = "收款人名称", required = true)
	private String payeeName;

	@StringIndex(6)
	@ApiModelProperty(value = "收款人账户类型", required = true)
	private String payeeType;

	@StringIndex(7)
	@ApiModelProperty(value = "收款人地址", required = false)
	private String payeeAddress;

	@StringIndex(8)
	@ApiModelProperty(value = "收款行名称", required = false)
	private String payeeBankName;

	@StringIndex(9)
	@ApiModelProperty(value = "支付金额", required = false)
	private String amount;

	@StringIndex(10)
	@ApiModelProperty(value = "本行它行标志", required = false)
	private String sysFlag;

	@StringIndex(11)
	@ApiModelProperty(value = "同城异地标志", required = false)
	private String remitLocation;

	@StringIndex(12)
	@ApiModelProperty(value = "付款用途", required = false)
	private String payPurpose;

	@StringIndex(13)
	@ApiModelProperty(value = "附言", required = false)
	private String note;

	@StringIndex(14)
	@ApiModelProperty(value = "支付号", required = true)
	private String payBankNo;

	@StringIndex(15)
	@ApiModelProperty(value = "授权状态", required = false)
	private String status;
}
