package com.rfchina.wallet.server.bank.yunst.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YunstCreateMemberResult {
	@ApiModelProperty(value = "云商通用户唯一标识")
	private String userId;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:M+mchId)")
	private String bizUserId;

}
