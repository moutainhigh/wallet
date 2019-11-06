package com.rfchina.wallet.server.bank.pudong.domain.request;


import com.rfchina.platform.biztool.mapper.string.StringIndex;
import com.rfchina.platform.biztool.mapper.string.StringObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @author nzm
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriPayReq {

	@StringIndex(1)
	@ApiModelProperty(value = "明细序号", required = true)
	private String detailNo;

	@StringIndex(2)
	@ApiModelProperty(value = "是否浦发账户 0:是 1:否 ", required = true)
	private String isPuFaAcct;

	@StringIndex(3)
	@ApiModelProperty(value = "收付款人对公对私标志 0:对公 1:对私", required = true)
	private String payeeType;

	@StringIndex(4)
	@ApiModelProperty(value = "银行卡卡类型 0：对公帐号 1：卡 2：活期一本通 8：活期存折 9：内部帐/表外帐", required = true)
	private String cardType;

	@StringIndex(5)
	@ApiModelProperty(value = "对方账号", required = true)
	private String payeeAcctNo;

	@StringIndex(6)
	@ApiModelProperty(value = "对方账户名", required = true)
	private String payeeName;

	@StringIndex(7)
	@ApiModelProperty(value = "证件类型.1:身份证 3:军官证 B:台湾同胞来往内地通行证 C:临时身份证 Y:营业执照", required = false)
	private String idType;

	@StringIndex(8)
	@ApiModelProperty(value = "证件号码", required = false)
	private String idNo;

	@StringIndex(9)
	@ApiModelProperty(value = "对手行行号", required = false)
	private String payeeBankCode;

	@StringIndex(10)
	@ApiModelProperty(value = "对手行行名", required = false)
	private String payeeBankName;

	@StringIndex(11)
	@ApiModelProperty(value = "支付行号", required = false)
	private String payerBankCode;

	@StringIndex(12)
	@ApiModelProperty(value = "币种", required = false)
	private String moneyType;

	@StringIndex(13)
	@ApiModelProperty(value = "金额", required = true)
	private String amount;

	@StringIndex(14)
	@ApiModelProperty(value = "手机号", required = false)
	private String telephone;

	@StringIndex(15)
	@ApiModelProperty(value = "企业流水号", required = false)
	private String bizLog;

	@StringIndex(16)
	@ApiModelProperty(value = "备用信息", required = false)
	private String reserve;

	@StringIndex(17)
	@ApiModelProperty(value = "企业分支机构.集团企业分支编码，非集团企业不用填写", required = false)
	private String bizCode;

	@StringIndex(18)
	@ApiModelProperty(value = "摘要", required = false)
	private String degist;

	@StringIndex(19)
	@ApiModelProperty(value = "备注", required = false)
	private String remark;

	@StringIndex(20)
	@ApiModelProperty(value = "备用1", required = false)
	private String demon1;

	@StringIndex(21)
	@ApiModelProperty(value = "备用2", required = false)
	private String demon2;

	@StringIndex(22)
	@ApiModelProperty(value = "备用3", required = false)
	private String demon3;

	private String getValue(String val) {
		return !StringUtils.isEmpty(val) ? val : "";
	}

	public String toString() {
//		StringBuilder buf = new StringBuilder()
//			.append(getValue(detailNo)).append("|")
//			.append(getValue(isPuFaAcct)).append("|")
//			.append(getValue(payeeType)).append("|")
//			.append(getValue(cardType)).append("|")
//			.append(getValue(payeeAcctNo)).append("|")
//			.append(getValue(payeeName)).append("|")
//			.append(getValue(idType)).append("|")
//			.append(getValue(idNo)).append("|")
//			.append(getValue(payeeBankCode)).append("|")
//			.append(getValue(payeeBankName)).append("|")
//			.append(getValue(payerBankCode)).append("|")
//			.append(getValue(moneyType)).append("|")
//			.append(getValue(amount)).append("|")
//			.append(getValue(telephone)).append("|")
//			.append(getValue(bizLog)).append("|")
//			.append(getValue(reserve)).append("|")
//			.append(getValue(bizCode)).append("|")
//			.append(getValue(degist)).append("|")
//			.append(getValue(remark)).append("|")
//			.append(getValue(demon1)).append("|")
//			.append(getValue(demon2)).append("|")
//			.append(getValue(demon3));
//		return buf.toString();

		return StringObject.toObjectString(this,PriPayReq.class,"|");
	}

}
