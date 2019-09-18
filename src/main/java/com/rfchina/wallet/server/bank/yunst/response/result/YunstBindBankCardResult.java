package com.rfchina.wallet.server.bank.yunst.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YunstBindBankCardResult {
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:M+mchId)")
	private String bizUserId;
	@ApiModelProperty(value = "流水号")
	private String tranceNum;
	@ApiModelProperty(value = "申请时间 YYYYMMDD")
	private String transDate;
}
