package com.rfchina.wallet.server.bank.yunst.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YunstPersonSetRealNameResult {
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:M+mchId)")
	private String bizUserId;
	@ApiModelProperty(value = "姓名")
	private String name;
	@ApiModelProperty(value = "证件类型 目前只支持身份证-1L")
	private Long identityType = 1L;
	@ApiModelProperty(value = "证件号码 (RSA加密)")
	private String identityNo;
}
