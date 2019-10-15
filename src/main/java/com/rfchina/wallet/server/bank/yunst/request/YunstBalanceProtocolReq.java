package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.1 账户余额扣款协议签约")
public class YunstBalanceProtocolReq implements YunstBaseReq {
	private static final long serialVersionUID = 118332437377003408L;
	@ApiModelProperty(value = "请求流水号", required = true)
	private String protocolReqSn;
	@ApiModelProperty(value = "付款方 商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String payerId;
	@ApiModelProperty(value = "收款方", required = true)
	private String receiverId;
	@ApiModelProperty(value = "协议名称", required = true)
	private String protocolName;
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
		return "signBalanceProtocol";
	}
}
