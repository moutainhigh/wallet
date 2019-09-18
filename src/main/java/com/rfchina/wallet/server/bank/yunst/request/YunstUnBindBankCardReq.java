package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.15 解绑绑定银行卡")
public class YunstUnBindBankCardReq implements YunstBaseReq {
	private static final long serialVersionUID = 4565276229987182645L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:M+mchId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "银行卡号,(RSA加密)", required = true )
	private String cardNo;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "unbindBankCard";
	}
}
