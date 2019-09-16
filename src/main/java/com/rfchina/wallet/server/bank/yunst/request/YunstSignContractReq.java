package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.4 会员电子协议签约")
public class YunstSignContractReq implements YunstBaseReq {
	private static final long serialVersionUID = 118332437377003408L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:M+mchId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "签订之后，跳转返回的页面地址", required = true)
	private String jumpUrl;
	@ApiModelProperty(value = "后台通知地址", required = true)
	private String backUrl;
	@ApiModelProperty(value = "访问终端类型 1-Mobile,2-PC", required = true)
	private Long source = 2L;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "signContract";
	}
}
