package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayStatusResp {
	@ApiModelProperty(value = "业务凭证号")
	private String bizNo;

	@ApiModelProperty(value = "钱包批次号")
	private String batchNo;

	@ApiModelProperty(name="payee_account", value = "收款方帐号")
	private String payeeAccount;

	@ApiModelProperty(name="payee_name", value = "收款方户名")
	private String payeeName;

	@ApiModelProperty(name="payee_type", value = "收款账户类型，1：对公账户，2：个人账户")
	private Byte payeeType;

	@ApiModelProperty(name="payee_bank_code", value = "收款银行行号")
	private String payeeBankCode;

	@ApiModelProperty(name="note", value = "附言(不超过100)")
	private String note;

	@ApiModelProperty(name="remark", value = "备注")
	private String remark;

	@ApiModelProperty(value = "支付金额")
	private Long amount;

	@ApiModelProperty(value = "交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销，6：待处理")
	private Byte status;

	@ApiModelProperty(value = "错误码")
	private String errCode;

	@ApiModelProperty(value = "用户错误信息")
	private String userErrMsg;

	@ApiModelProperty(value = "系统错误信息")
	private String sysErrMsg;

	@ApiModelProperty(name="create_time", value = "创建日期")
	private Date createTime;

	@ApiModelProperty(name="lanch_time", value = "银行发起时间")
	private Date lanchTime;

	@ApiModelProperty(name="biz_time", value = "银行交易终态日期")
	private Date bizTime;

	@ApiModelProperty(name="end_time", value = "交易结束时间（浦发只有时分秒，查询成功定为交易结束时间）")
	private Date endTime;

	@ApiModelProperty(value = "钱包收单日期")
	private String transDate;
}
