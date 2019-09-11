package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder=true, builderMethodName="builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.1 创建会员")
public class YunstCreateMemberReq extends YunstBaseReq {
	private static final long serialVersionUID = -8809511241662352693L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:C+mchId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "会员类型 2-企业会员,3-个人会员", required = true)
	private Long memberType;
	@ApiModelProperty(value = "访问终端类型 1-Mobile,2-PC", required = true)
	private Long source=2L;
	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "createMember";
	}
}
