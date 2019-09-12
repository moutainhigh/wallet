package com.rfchina.wallet.server.bank.yunst.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YunstModifyPhoneResult {
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:M+mchId)")
	private String bizUserId;
	@ApiModelProperty(value = "旧手机号码")
	private String oldPhone;
	@ApiModelProperty(value = "新手机号码")
	private String newPhone;
}
