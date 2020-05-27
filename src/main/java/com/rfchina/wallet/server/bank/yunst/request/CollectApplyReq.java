package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "4.2.5 托管代收申请(标准版)")
@Getter
@Builder
public class CollectApplyReq implements YunstBaseReq {

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "agentCollectApply";
	}

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(required = true, value = "商户系统用户标识,商户系统中唯一编号。付款用户的 bizUserId,支持个人会员、企业会员")
	private String payerId;

	@ApiModelProperty(required = true, value = "收款列表")
	private List<RecieveInfo> recieverList;

	@ApiModel(description = "收款信息")
	@Getter
	@Builder
	public static class RecieveInfo {

		@ApiModelProperty(value = "商户系统用户标识,商户系统中唯一编号。", required = true)
		private String bizUserId;

		@ApiModelProperty(value = "金额,单位:分", required = true)
		private Long amount;
	}

	@ApiModelProperty(value = "商品类型")
	private Long goodsType;

	@ApiModelProperty(value = "商户系统商品编号")
	private String bizGoodsNo;

	@ApiModelProperty(required = true, value = "业务码")
	private String tradeCode;

	@ApiModelProperty(required = true, value = "订单金额")
	private Long amount;

	@ApiModelProperty(value = "手续费")
	private Long fee;

	@ApiModelProperty(value = "交易验证方式")
	private Long validateType;

	@ApiModelProperty(value = "前台通知地址")
	private String frontUrl;

	@ApiModelProperty(required = true, value = "后台通知地址")
	private String backUrl;

	@ApiModelProperty(value = "订单过期时间")
	private String orderExpireDatetime;

	@ApiModelProperty(required = true, value = "支付方式")
	private Map<String, Object> payMethod;

	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	@ApiModelProperty(value = "商品描述")
	private String goodsDesc;

	@ApiModelProperty(required = true, value = "行业代码")
	private String industryCode;

	@ApiModelProperty(required = true, value = "行业名称")
	private String industryName;

	@ApiModelProperty(required = true, value = "访问终端类型")
	private Long source;

	@ApiModelProperty(value = "摘要")
	private String summary;

	@ApiModelProperty(value = "扩展参数")
	private String extendInfo;

	@ApiModel
	@Getter
	@Builder
	public static class CollectPayMethod {


		@ApiModel
		@Getter
		@Builder
		public static class Balance {

			public static String KEY_Balance = "BALANCE";

			@ApiModelProperty(value = "账户集编号", required = true)
			private String accountSetNo;

			@ApiModelProperty(required = true, value = "支付金额")
			private Long amount;
		}

		@ApiModel
		public static class Wechat {

			public static String KEY_MiniProgram = "WECHATPAY_MINIPROGRAM_ORG";
			public static String KEY_AppOpen = "WECHATPAY_APP_OPEN";
			public static String KEY_H5Open = "WECHATPAY_H5_OPEN";
			public static String KEY_WechatPublic = "WECHAT_PUBLIC_ORG";
			public static String KEY_ScanWeixin = "SCAN_WEIXIN_ORG";

			@ApiModel(description = "微信小程序支付（收银宝）")
			@Getter
			@Builder
			public static class MiniProgram {

				@ApiModelProperty(required = true, value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串")
				private String limitPay;

				@ApiModelProperty(required = true, value = "微信小程序支付appid参数,当商户有多个小程序或公众号时接口指定上送")
				private String subAppid;

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "微信JS支付openid——微信分配")
				private String acct;

				@ApiModelProperty(required = true, value = "收银宝子商户号")
				private String vspCusid;
			}

			@ApiModel(description = "微信原生APP支付")
			@Getter
			@Builder
			public static class AppOpen {

				@ApiModelProperty(required = true, value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串")
				private String limitPay;

				@ApiModelProperty(required = true, value = "微信小程序支付appid参数,当商户有多个小程序或公众号时接口指定上送")
				private String subAppId;

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;
			}

			@ApiModel(description = "微信原生H5支付")
			@Getter
			@Builder
			public static class H5Open {

				@ApiModelProperty(required = true, value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串")
				private String limitPay;

				@ApiModelProperty(required = true, value = "微信小程序支付appid参数,当商户有多个小程序或公众号时接口指定上送")
				private String subAppId;

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "微信JS支付openid——微信分配")
				private String acct;

				@ApiModelProperty(value = "用户下单及调起支付的终端IP")
				private String cusip;

				@ApiModelProperty(name = "scene_info", value = "场景信息（H5支付）")
				private String sceneInfo;
			}

			@ApiModel(description = "微信JS支付(公众号)")
			@Getter
			@Builder
			public static class WechatPublic {

				@ApiModelProperty(required = true, value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串")
				private String limitPay;

				@ApiModelProperty(required = true, value = "微信小程序支付appid参数,当商户有多个小程序或公众号时接口指定上送")
				private String subAppid;

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "微信JS支付openid——微信分配")
				private String acct;

				@ApiModelProperty(required = true, value = "收银宝子商户号")
				private String vspCusid;

			}

			@ApiModel(description = "微信扫码支付(正扫)")
			@Getter
			@Builder
			public static class ScanWeixin {

				@ApiModelProperty(required = true, value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串")
				private String limitPay;

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "收银宝子商户号")
				private String vspCusid;
			}

		}

		@ApiModel
		public static class Alipay {

			public static String KEY_ScanAlipay = "SCAN_ALIPAY_ORG";
			public static String KEY_AlipayService = "ALIPAY_SERVICE_ORG";
			public static String KEY_AppOpen = "ALIPAY_APP_OPEN";

			@ApiModel(description = "支付宝扫码支付(正扫)")
			@Getter
			@Builder
			public static class ScanAlipay {

				@ApiModelProperty(required = true, value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串")
				private String limitPay;

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "收银宝子商户号")
				private String vspCusid;
			}

			@ApiModel(description = "支付宝JS支付(生活号)")
			@Getter
			@Builder
			public static class AlipayService {

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "支付宝JS支付user_id")
				private String acct;

				@ApiModelProperty(required = true, value = "收银宝子商户号")
				private String vspCusid;
			}

			@ApiModel(description = "支付宝原生")
			@Getter
			@Builder
			public static class AppOpen {

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(value = "支付渠道名称(最长 100),如balance-余额;moneyFund-余额宝;coupon-红包;pcredit-花呗;creditCard-信用卡")
				private String enablePayChannels;

				@ApiModelProperty(value = "支付摘要(最长 100)")
				private String paysummary;
			}

		}

		@ApiModel
		public static class CodePay {

			public static String KEY_CodePayVsp = "CODEPAY_VSP_ORG";

			@ApiModel(description = "收银宝刷卡支付（被扫）")
			@Getter
			@Builder
			public static class CodePayVsp {

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "支付授权码，支付宝被扫刷卡支付时,用户的付款二维码")
				private String authcode;

				@ApiModelProperty(required = true, value = "非贷记卡:no_credit 借、贷记卡:””需要传空字符串")
				private String limitPay;

				@ApiModelProperty(required = true, value = "收银宝子商户号")
				private String vspCusid;
			}
		}


		@ApiModel
		public static class BankCard {

			public static String KEY_QuickPayVsp = "QUICKPAY_VSP";

			@ApiModel(description = "收银宝快捷支付")
			@Getter
			@Builder
			public static class CardQuickPay {

				@ApiModelProperty(required = true, value = "支付金额,单位:分")
				private Long amount;

				@ApiModelProperty(required = true, value = "银行卡号，RSA 加密")
				private String bankCardNo;

				@ApiModelProperty(value = "有效期，信用卡必填，格式为月年； 如 0321，2 位月 2 位年；RSA 加密。")
				private String validate;

				@ApiModelProperty(value = "信用卡必填。RSA 加密。")
				private String cvv2;
			}
		}

		@ApiModel
		@Getter
		@Builder
		public static class Pos {

			public static String KEY_POS = "ORDER_VSPPAY";

			@ApiModelProperty(value = "集团模式：收银宝子商户号", required = true)
			private String vspCusid;

			@ApiModelProperty(required = true, value = "支付金额")
			private Long amount;
		}
	}

}
