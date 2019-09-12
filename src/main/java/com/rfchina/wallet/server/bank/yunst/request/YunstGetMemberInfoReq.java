package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.9 获取会员信息")
public class YunstGetMemberInfoReq implements YunstBaseReq {

	private static final long serialVersionUID = -5280141342950809426L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:M+mchId)", required = true)
	private String bizUserId;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "getMemberInfo";
	}
}
