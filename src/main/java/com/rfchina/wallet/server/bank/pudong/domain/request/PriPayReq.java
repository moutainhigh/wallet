package com.rfchina.wallet.server.bank.pudong.domain.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriPayReq {

	@ApiModelProperty(value = "明细序号", required = true)
	private String detailNo;

	@ApiModelProperty(value = "是否浦发账户 0:是 1:否 ", required = true)
	private String isPuFaAcct;

	@ApiModelProperty(value = "收付款人对公对私标志 0:对公 1:对私", required = true)
	private String payeeType;

	@ApiModelProperty(value = "银行卡卡类型 0：对公帐号 1：卡 2：活期一本通 8：活期存折 9：内部帐/表外帐", required = true)
	private String cardType;

	@ApiModelProperty(value = "对方账号", required = true)
	private String payeeAcctNo;

	@ApiModelProperty(value = "对方账户名", required = true)
	private String payeeName;

	@ApiModelProperty(value = "证件类型.1:身份证 3:军官证 B:台湾同胞来往内地通行证 C:临时身份证 Y:营业执照", required = false)
	private String idType;

	@ApiModelProperty(value = "证件号码", required = false)
	private String idNo;

	@ApiModelProperty(value = "对手行行号", required = false)
	private String payeeBankCode;

	@ApiModelProperty(value = "对手行行名", required = false)
	private String payeeBankName;

	@ApiModelProperty(value = "支付行号", required = false)
	private String payerBankCode;

	@ApiModelProperty(value = "币种", required = false)
	private String moneyType;

	@ApiModelProperty(value = "金额", required = false)
	private String amount;

	@ApiModelProperty(value = "手机号", required = false)
	private String telephone;

	@ApiModelProperty(value = "企业流水号", required = false)
	private String bizLog;

	@ApiModelProperty(value = "备用信息", required = false)
	private String reserve;

	@ApiModelProperty(value = "企业分支机构.集团企业分支编码，非集团企业不用填写", required = false)
	private String bizCode;

	@ApiModelProperty(value = "摘要", required = false)
	private String degist;

	@ApiModelProperty(value = "备注", required = false)
	private String remark;

	@ApiModelProperty(value = "备用1", required = false)
	private String demon1;

	@ApiModelProperty(value = "备用2", required = false)
	private String demon2;

	@ApiModelProperty(value = "备用3", required = false)
	private String demon3;

	private String getValue(String val){
		return !StringUtils.isEmpty(val)?val:"";
	}

	public String toString(){
		StringBuilder buf = new StringBuilder()
			.append(getValue(detailNo)).append("|")
			.append(getValue(isPuFaAcct)).append("|")
			.append(getValue(payeeType)).append("|")
			.append(getValue(cardType)).append("|")
			.append(getValue(payeeAcctNo)).append("|")
			.append(getValue(payeeName)).append("|")
			.append(getValue(idType)).append("|")
			.append(getValue(idNo)).append("|")
			.append(getValue(payeeBankCode)).append("|")
			.append(getValue(payeeBankName)).append("|")
			.append(getValue(payerBankCode)).append("|")
			.append(getValue(moneyType)).append("|")
			.append(getValue(amount)).append("|")
			.append(getValue(telephone)).append("|")
			.append(getValue(bizLog)).append("|")
			.append(getValue(reserve)).append("|")
			.append(getValue(bizCode)).append("|")
			.append(getValue(degist)).append("|")
			.append(getValue(remark)).append("|")
			.append(getValue(demon1)).append("|")
			.append(getValue(demon2)).append("|")
			.append(getValue(demon3));
		return buf.toString();

	}

}
