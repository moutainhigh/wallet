package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class PubPayQueryRespBody {

	@ApiModelProperty(value = "交易笔数")
	private String totalCount;

	private Lists lists;

	@Data
	public static class Lists {

		private List<PayResult> list;
	}

	@Data
	public static class PayResult {

		@ApiModelProperty(value = "电子凭证号")
		private String elecChequeNo;

		@ApiModelProperty(value = "受理编号")
		private String acceptNo;

		@ApiModelProperty(value = "序号")
		private String serialNo;

		@ApiModelProperty(value = "交易日期")
		private String transDate;

		@ApiModelProperty(value = "预约日期")
		private String bespeakDate;

		@ApiModelProperty(value = "申请日期")
		private String PromiseDate;

		@ApiModelProperty(value = "账号")
		private String acctNo;

		@ApiModelProperty(value = "付款人账户名称")
		private String acctName;

		@ApiModelProperty(value = "收款人账号")
		private String payeeAcctNo;

		@ApiModelProperty(value = "收款人名称")
		private String payeeName;

		@ApiModelProperty(value = "收款人账户类型")
		private String payeeType;

		@ApiModelProperty(value = "收款行名称")
		private String payeeBankName;

		@ApiModelProperty(value = "收款人地址")
		private String payeeAddress;

		@ApiModelProperty(value = "支付金额")
		private String amount;

		@ApiModelProperty(value = "本行他行标志")
		private String sysFlag;

		@ApiModelProperty(value = "同城/异地标志")
		private String remitLocation;

		@ApiModelProperty(value = "附言")
		private String note;

		@ApiModelProperty(value = "交易状态")
		private String transStatus;

		@ApiModelProperty(value = "交易流水号")
		private String seqNo;
	}
}
