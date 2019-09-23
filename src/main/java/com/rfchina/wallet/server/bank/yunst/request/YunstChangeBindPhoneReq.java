package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.5 修改绑定手机")
public class YunstChangeBindPhoneReq implements YunstBaseReq {

	private static final long serialVersionUID = -1678033152764830666L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "旧电话号码", required = true)
	private String oldPhone;
	@ApiModelProperty(value = "新电话号码", required = true)
	private String newPhone;
	@ApiModelProperty(value = "新手机验证码", required = true)
	private String newVerificationCode;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "changeBindPhone";
	}
}
