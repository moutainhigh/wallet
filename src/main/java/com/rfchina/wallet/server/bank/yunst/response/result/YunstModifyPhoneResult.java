package com.rfchina.wallet.server.bank.yunst.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YunstModifyPhoneResult {
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)")
	private String bizUserId;
	@ApiModelProperty(value = "旧手机号码")
	private String oldPhone;
	@ApiModelProperty(value = "新手机号码")
	private String newPhone;
}
