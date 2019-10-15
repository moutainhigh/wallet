package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SettleQueryResp {

	@ApiModelProperty(value = "工单ID")
	private Long applyId;

	@ApiModelProperty(value = "业务凭证号")
	private String bizNo;

	@ApiModelProperty(value = "钱包ID")
	private Long walletId;

	@ApiModelProperty(value = "交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败")
	private Byte status;

	@ApiModelProperty(value = "用户错误信息")
	private String userErrMsg;

	@ApiModelProperty(value = "系统错误信息")
	private String sysErrMsg;
}
