package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.12 确认绑定银行卡")
public class YunstBindBankCardReq implements YunstBaseReq {
	private static final long serialVersionUID = -2043910859082331267L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "流水号",required = true)
	private String tranceNum;
	@ApiModelProperty(value = "申请时间 YYYYMMDD",required = false)
	private String transDate;
	@ApiModelProperty(value = "银行预留手机", required = true )
	private String phone;
	@ApiModelProperty(value = "有效期 格式为月年;如0321 2位月 2位年; (RSA加密)", required = false )
	private String validate;
	@ApiModelProperty(value = "CVV2 3位数字,(RSA加密)", required = false )
	private String cvv2;
	@ApiModelProperty(value = "短信验证码", required = true )
	private String verificationCode;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "bindBankCard";
	}
}
