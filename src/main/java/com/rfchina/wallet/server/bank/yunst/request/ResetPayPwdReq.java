package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.19 重置支付密码【密码验证版】")
public class ResetPayPwdReq implements YunstBaseReq {

	private static final long serialVersionUID = 118332437377003408L;

	@Override
	public String getServcieName() {
		return "MemberPwdService";
	}

	@Override
	public String getMethodName() {
		return "resetPayPwd";
	}


	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;

	@ApiModelProperty(value = "用户姓名", required = true)
	private String name;

	@ApiModelProperty(value = "用户绑定手机号码", required = true)
	private String phone;

	@ApiModelProperty(value = "证件类型 目前只支持身份证-1L", required = true)
	private Long identityType;

	@ApiModelProperty(value = "证件号码 (RSA加密)", required = true)
	private String identityNo;

	@ApiModelProperty(value = "签订之后，跳转返回的页面地址", required = true)
	private String jumpUrl;

	@ApiModelProperty(value = "后台通知地址", required = true)
	private String backUrl;

}
