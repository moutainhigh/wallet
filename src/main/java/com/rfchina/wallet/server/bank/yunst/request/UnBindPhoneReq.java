package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.24 解绑手机（验证原手机短信验证码）")
public class UnBindPhoneReq implements YunstBaseReq {

	private static final long serialVersionUID = -7879283375342118045L;

	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;

	@ApiModelProperty(value = "原手机号", required = true)
	private String phone;

	@ApiModelProperty(value = "验证码", required = true)
	private String verificationCode;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "unbindPhone";
	}
}
