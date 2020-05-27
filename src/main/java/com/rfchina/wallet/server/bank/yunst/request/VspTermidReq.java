package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;


@ApiModel(description = "4.1.21 会员收银宝渠道商户信息及终端信息绑定")
@Getter
@Builder
public class VspTermidReq implements YunstBaseReq {

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "vspTermidService";
	}

	@ApiModelProperty(required = true, value = "商户系统用户标识，商户系统中唯一 编号。")
	private String bizUserId;

	@ApiModelProperty(required = true, value = "操作,set-绑定 query-查询")
	private String operationType;

	@ApiModelProperty(required = true, value = "收银宝集团商户号")
	private String vspMerchantid;

	@ApiModelProperty(required = true, value = "收银宝商户号")
	private String vspCusid;

	@ApiModelProperty(required = true, value = "收银宝分配的")
	private String appid;

	@ApiModelProperty(required = false, value = "收银宝当面付二维码编号/POS 终端号")
	private String vspTermid;
}
