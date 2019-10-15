package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.6 个人实名认证")
public class YunstPersonSetRealNameReq implements YunstBaseReq {
	private static final long serialVersionUID = -705594183961767592L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "是否由云商通进行认证", required = false)
	private Boolean isAuth = true;
	@ApiModelProperty(value = "姓名", required = true )
	private String name;
	@ApiModelProperty(value = "证件类型 目前只支持身份证-1L", required = true )
	private Long identityType = 1L;
	@ApiModelProperty(value = "证件号码 (RSA加密)", required = true )
	private String identityNo;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "setRealName";
	}
}
