package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "4.1.19 重置支付密码【密码验证版】")
public class ResetPayPwdReq implements YunstBaseReq {


	@Override
	public String getServcieName() {
		return "MemberPwdService";
	}

	@Override
	public String getMethodName() {
		return "resetPayPwd";
	}


	@ApiModelProperty(value = "商户系统用户标识", required = true)
	private String bizUserId;

	@ApiModelProperty(value = "用户姓名", required = true)
	private String name;

	@ApiModelProperty(value = "手机号码", required = true)
	private String phone;

	@ApiModelProperty(value = "证件类型 目前只支持身份证1L", required = true)
	private Long identityType;

	@ApiModelProperty(value = "证件号码 (RSA加密)", required = true)
	private String identityNo;

	@ApiModelProperty(value = "后台通知地址", required = true)
	private String backUrl;

	@ApiModelProperty(value = "签订之后，跳转返回的页面地址", required = true)
	private String jumpUrl;

}
