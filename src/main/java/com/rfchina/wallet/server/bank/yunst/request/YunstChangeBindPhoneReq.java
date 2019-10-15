package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.20 个人会员修改绑定手机")
public class YunstChangeBindPhoneReq implements YunstBaseReq {

	private static final long serialVersionUID = -1678033152764830666L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "姓名", required = true)
	private String name;
	@ApiModelProperty(value = "证件类型 目前只支持身份证-1L", required = true )
	private Long identityType = 1L;
	@ApiModelProperty(value = "证件号码 (RSA加密)", required = true )
	private String identityNo;
	@ApiModelProperty(value = "旧电话号码", required = true)
	private String oldPhone;
	@ApiModelProperty(value = "修改手机之后，跳转返回的页面地址", required = false)
	private String jumpUrl;
	@ApiModelProperty(value = "后台通知地址", required = true)
	private String backUrl;

	@Override
	public String getServcieName() {
		return "MemberPwdService";
	}

	@Override
	public String getMethodName() {
		return "updatePhoneByPayPwd";
	}
}
