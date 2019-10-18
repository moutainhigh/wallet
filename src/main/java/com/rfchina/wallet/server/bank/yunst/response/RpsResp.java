package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RpsResp {

	@ApiModelProperty(value = "服务对象")
	private String service;

	@ApiModelProperty(value = "调用方法")
	private String method;

	@ApiModelProperty(value = "订单是否成功")
	private String status;

	@ApiModelProperty(value = "错误代码")
	private String errorCode;

	@ApiModelProperty(value = "错误信息")
	private String message;

	@ApiModelProperty(value = "请求参数，嵌套的 JSON 对象，key 为参数名称，value 为参数值")
	private RpsValue returnValue;

	@Data
	public static class RpsValue {

		@ApiModelProperty(value = "通商云订单号")
		private String orderNo;

		@ApiModelProperty(value = "商户订单号（支付订单）")
		private String bizOrderNo;

		@ApiModelProperty(value = "订单金额 单位：分")
		private Long amount;

		@ApiModelProperty(value = "订单支付完成时间 yyyy-MM-dd HH:mm:ss")
		private String payDatetime;

		@ApiModelProperty(value = "商户系统用户标识，商户系统中唯一编号。")
		private String buyerBizUserId;

		@ApiModelProperty(value = "退款去向 1：到账户余额 2：到原支付账户银行卡/微信/支付宝等")
		private Long refundWhereabouts;

		@ApiModelProperty(value = "代付去向 1：到账户余额 ")
		private Long payWhereabouts;

		@ApiModelProperty(value = "支付人帐号 微信支付的 openid 支付宝平台的 user_id 刷卡交易：隐藏的卡号,例如621700****4586")
		private String acct;

		@ApiModelProperty(value = "借贷标志 刷卡消费交易必传 00-借记卡 01-存折 02-信用卡 03-准贷记卡 04-预付费卡 05-境外卡 99-其他")
		private String accttype;

		@ApiModelProperty(value = "终端号")
		private String termno;

		@ApiModelProperty(value = "渠道商户号")
		private String cusid;

		@ApiModelProperty(value = "通道交易流水号 支付渠道的交易流水号，微信订单详情“商户单号”，支付宝订单详情“商家订单号走收银宝渠道-对应收银宝接口指定 trxid")
		private String payInterfaceOutTradeNo;

		@ApiModelProperty(value = "交易参考号")
		private String termrefnum;

		@ApiModelProperty(value = "取值为收银宝接口手续费字段")
		private String channelFee;

		@ApiModelProperty(value = "渠道交易完成时间")
		private String channelPaytime;

		@ApiModelProperty(value = "通道交易类型 收银宝渠道返回的交易类型对应收银宝接口字段trxcode VSP501 微信支付 VSP502 微信支付撤销 VSP503 微信支付退款 VSP505 手机 QQ 支付 VSP506 手机 QQ 支付撤销 VSP507 手机 QQ 支付退款 VSP511 支付宝支付 VSP512 支付宝支付撤销 VSP513 支付宝支付退款 VSP551 银联扫码支付 VSP552 银联扫码撤销 VSP553 银联扫码退货")
		private String payInterfacetrxcode;

		@ApiModelProperty(value = "收银宝终端流水")
		private String traceno;

		@ApiModelProperty(value = "扩展参数")
		private String extendInfo;

	}

}
