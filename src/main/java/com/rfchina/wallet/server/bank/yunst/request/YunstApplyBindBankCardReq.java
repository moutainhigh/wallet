package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.11 请求绑定银行卡")
public class YunstApplyBindBankCardReq implements YunstBaseReq {
	private static final long serialVersionUID = -2043910859082331267L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "银行卡号 (RSA加密)", required = true )
	private String cardNo;
	@ApiModelProperty(value = "银行预留手机", required = true )
	private String phone;
	@ApiModelProperty(value = "姓名,如果是企业会员,为法人名字", required = true )
	private String name;
	@ApiModelProperty(value = "绑卡方式 收银宝快捷支付签约（有银行范围） —支持收银宝快捷支付 —支持提现", required = true )
	private Long cardCheck = 7L;
	@ApiModelProperty(value = "证件类型 目前只支持身份证-1L", required = true )
	private Long identityType = 1L;
	@ApiModelProperty(value = "证件号码 (RSA加密)", required = true )
	private String identityNo;
	@ApiModelProperty(value = "有效期 格式为月年;如0321 2位月 2位年; (RSA加密)", required = false )
	private String validate;
	@ApiModelProperty(value = "CVV2 3位数字,(RSA加密)", required = false )
	private String cvv2;
	@ApiModelProperty(value = "是否安全卡,信用卡时不能填写", required = false)
	private Boolean isSafeCard = false;
	@ApiModelProperty(value = "支付行号", required = false )
	private String unionBank;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "applyBindBankCard";
	}
}
