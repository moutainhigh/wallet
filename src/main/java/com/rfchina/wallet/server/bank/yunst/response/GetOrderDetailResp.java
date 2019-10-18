package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetOrderDetailResp {

	@ApiModelProperty(value = "云商通订单号")
	private String orderNo;

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(value = "订单状态 1:未支付 3:交易失败 4:交易成功 5:交易成功-发生退款 6:关闭 99:进行中 ")
	private Long orderStatus;

	@ApiModelProperty(value = "失败原因")
	private String errorMessage;

	@ApiModelProperty(value = "订单金额")
	private Long amount;

	@ApiModelProperty(value = "订单支付完成时间 yyyy-MM-dd HH:mm:ss")
	private String payDatetime;

	@ApiModelProperty(value = "商户系统用户标识")
	private String buyerBizUserId;

	@ApiModelProperty(value = "退款去向 1:到账户余额 2:到原支付账户银行卡/微信/支付宝等")
	private Long refundWhereabouts;

	@ApiModelProperty(value = "代付去向 1:到账户余额 	2:到银行卡")
	private Long payWhereabouts;

	@ApiModelProperty(value = "支付人帐号 微信支付的 openid, 支付宝平台的 user_id, 刷卡交易:隐藏的卡号 ")
	private String acct;

	@ApiModelProperty(value = "借贷标志 刷卡消费交易必传 00-借记卡 01-存折 02-信用卡 03-准贷记卡 04-预付费卡 05-境外卡 99-其他")
	private String accttype;

	@ApiModelProperty(value = "终端号")
	private String termno;

	@ApiModelProperty(value = "渠道商户号")
	private String cusid;

	@ApiModelProperty(value = "通道交易流水号")
	private String payInterfaceOutTradeN;

	@ApiModelProperty(value = "收银宝终端流水")
	private String traceno;

	@ApiModelProperty(value = "扩展参数")
	private String extendInfo;

}
