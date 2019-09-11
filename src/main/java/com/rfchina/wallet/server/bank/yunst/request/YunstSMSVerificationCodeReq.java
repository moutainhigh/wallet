package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder=true, builderMethodName="builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.2 发送短信验证码")
public class YunstSMSVerificationCodeReq extends YunstBaseReq {
	private static final long serialVersionUID = 1076123197140587252L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:C+mchId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "电话号码", required = true)
	private String phone;
	@ApiModelProperty(value = "验证码类型 9-绑定手机", required = true)
	private Long verificationCodeType;
	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "sendVerificationCode";
	}
}
