//package com.rfchina.wallet.server.bank.yunst.request;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//
//public class PayMethod {
//
//	public class WECHATPAY {
//
//		@ApiModel(description = "微信小程序支付")
//		public class MINIPROGRAM {
//
//			@ApiModelProperty(value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串,不能不传")
//			private String limitPay;
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "微信 JS 支付 openid")
//			private String acct;
//
//		}
//
////		@ApiModel(description = "微信APP支付(收银宝)")
////		public class VSP {
////
////			@ApiModelProperty(value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串,不能不传")
////			private String limitPay;
////
////			@ApiModelProperty(value = "支付金额,单位:分")
////			private Long amount;
////
////			@ApiModelProperty(value = "商户 app 类型;枚举值:IOS,Android,Wap")
////			private String apptype;
////
////			@ApiModelProperty(value = "商户 app 名称")
////			private String appname;
////
////			@ApiModelProperty(value = "商户 app 包名;app 包 名 或 者 网 站 地 址")
////			private String apppackage;
////
////			@ApiModelProperty(value = "用户下单及调起支付的终端 IP")
////			private String cusip;
////
////		}
//
//		@ApiModel(description = "微信原生APP支付")
//		public class AppOpen {
//
//			@ApiModelProperty(value = "微信端应用 ID:appid")
//			private String subAppId;
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "非贷记卡:no_credit")
//			private String limitPay;
//		}
//
//
//		@ApiModel(description = "微信原生H5支付")
//		public class H5Open {
//
//			@ApiModelProperty(value = "微信端应用 ID:appid")
//			private String subAppId;
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "非贷记卡:no_credit")
//			private String limitPay;
//
//			@ApiModelProperty(value = "微信用户标识 openid——微信")
//			private String acct;
//
//			@ApiModelProperty(value = "用户下单及调起支付的终端 IP")
//			private String cusip;
//
//			@ApiModelProperty(value = "场景信息")
//			private String sceneInfo;
//		}
//
//		@ApiModel(description = "微信JS支付(公众号)")
//		public class JsPublic {
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "非贷记卡:no_credit")
//			private String limitPay;
//
//			@ApiModelProperty(value = "微信用户标识 openid——微信")
//			private String acct;
//		}
//
//		@ApiModel(description = "微信扫码支付(正扫)")
//		public class ScanWeixin {
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "非贷记卡:no_credit")
//			private String limitPay;
//		}
//
//	}
//
//	public class Alipay {
//
//		@ApiModel(description = "支付宝扫码支付(正扫)")
//		public class ScanAlipay {
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "非贷记卡:no_credit")
//			private String limitPay;
//		}
//
//		@ApiModel(description = "支付宝JS支付(生活号)")
//		public class AlipayService {
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "支付宝 JS 支付 user_id")
//			private String acct;
//		}
//
//		@ApiModel(description = "支付宝原生")
//		public class ALIPAY_APP_OPEN {
//
//			@ApiModelProperty(value = "支付金额,单位:分")
//			private Long amount;
//
//			@ApiModelProperty(value = "支付渠道名称,如balance-余额;moneyFund-余额宝;coupon-红包;pcredit-花呗;creditCard-信用卡")
//			private String enablePayChannels;
//
//			@ApiModelProperty(value = "支付摘要")
//			private String paysumm;
//		}
//	}
//
//	public class Balance {
//
//		@ApiModelProperty(value = "支付金额,单位:分")
//		private Long amount;
//
//		@ApiModelProperty(value = "账户集编号")
//		private String accountSetNo;
//	}
//}
