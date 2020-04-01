package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "4.1.24 解绑手机（验证原手机短信验证码）")
public class UnBindPhoneResp {

	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)")
	private String bizUserId;

	@ApiModelProperty(value = "手机号码")
	private String phone;

	@ApiModelProperty(value = "“OK”表示解绑成功； “error”表示解绑失败")
	private String result;

}
