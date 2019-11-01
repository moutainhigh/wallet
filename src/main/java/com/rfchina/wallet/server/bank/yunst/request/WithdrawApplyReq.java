package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "4.2.2 充值申请")
@Getter
@Builder
public class WithdrawApplyReq implements YunstBaseReq {

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "withdrawApply";
	}

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(required = true, value = "商户系统用户标识")
	private String bizUserId;

	@ApiModelProperty(required = true, value = "账户集编号")
	private String accountSetNo;

	@ApiModelProperty(required = true, value = "订单金额")
	private Long amount;

	@ApiModelProperty(required = true, value = "手续费")
	private Long fee;

	@ApiModelProperty(value = "交易验证方式")
	private Long validateType;

	@ApiModelProperty(required = true, value = "后台通知地址")
	private String backUrl;

	@ApiModelProperty(value = "后台通知地址")
	private String orderExpireDatetime;

	@ApiModelProperty(required = true, value = "支付方式 如不传，则默认为通联通代付")
	private Map<String, Object> payMethod;

	@ApiModelProperty(required = true, value = "绑定的银行卡号/账号")
	private String bankCardNo;

	@ApiModelProperty(required = true, value = "银行卡/账户属性 0：个人银行卡 1：企业对公账户")
	private Long bankCardPro;

	@ApiModelProperty(required = true, value = "提现方式 D0：D+0 到账 D1：D+1 到账 T1customized：T+1 到账，仅工作日代付 D0customized：D+0 到账，根据平台资金头寸付款 默认为 D0")
	private String withdrawType;

	@ApiModelProperty(required = true, value = "行业代码")
	private String industryCode;

	@ApiModelProperty(required = true, value = "行业名称")
	private String industryName;

	@ApiModelProperty(required = true, value = "访问终端类型")
	private Long source;

	@ApiModelProperty(value = "摘要")
	private String summary;

	@ApiModelProperty(value = "扩展信息")
	private String extendInfo;

}
