package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.misc.EnumDef.WalletCardType;
import com.rfchina.wallet.server.msic.EnumWallet.ChannelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "代收请求")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectReq {


	@ApiModelProperty(required = true, name = "biz_no", value = "业务凭证号(业务方定义唯一,最长32字节)", example = "123")
	private String bizNo;

	@ApiModelProperty(required = true, value = "支付金额(单位分)", example = "1")
	private Long amount;

	@ApiModelProperty(value = "附言", required = false, example = "收入")
	private String note;

	@ApiModelProperty(required = true, value = "手续费，单位:分。如果不存在,则填 0。")
	private Long fee;

	@ApiModelProperty(required = true, name = "validate_type", value = "交易验证方式 0：无验证 1：短信 2：密码")
	private Byte validateType;

	@ApiModelProperty(name = "expire_time", value = "订单过期时间,订单最长时效为 24 小时")
	private Date expireTime;

	@ApiModelProperty(name = "industry_code", value = "行业代码（由渠道分配）")
	private String industryCode;

	@ApiModelProperty(name = "industry_name", value = "行业名称（由渠道分配）")
	private String industryName;

	@ApiModelProperty(value = "收款列表")
	private List<Reciever> recievers;

	@ApiModelProperty(name = "wallet_pay_method", value = "钱包支付方式")
	private WalletPayMethod walletPayMethod;

	@ApiModel
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class WalletPayMethod {

		@ApiModelProperty(value = "余额支付")
		private Balance balance;

		@ApiModelProperty(value = "支付宝支付")
		private Alipay alipay;

		@ApiModelProperty(value = "微信支付")
		private Wechat wechat;

		@ApiModelProperty(value = "刷卡支付")
		private CodePay codePay;

		@ApiModelProperty(value = "银行卡支付")
		private BankCard bankCard;

		@ApiModelProperty(hidden = true)
		public byte getMethods() {
			byte method = 0;
			if (balance != null) {
				method |= ChannelType.BALANCE.getValue().byteValue();
			} else if (wechat != null) {
				method |= ChannelType.WECHAT.getValue().byteValue();
			} else if (alipay != null) {
				method |= ChannelType.ALIPAY.getValue().byteValue();
			} else if (codePay != null) {
				method |= ChannelType.CODEPAY.getValue().byteValue();
			} else if (bankCard != null) {
				method |= ChannelType.BANKCARD.getValue().byteValue();
			}
			return method;
		}

		@ApiModelProperty(hidden = true)
		public BigDecimal getRate() {
			BigDecimal bigDecimal = new BigDecimal("0");
			if (balance != null) {
			} else if (wechat != null) {
				bigDecimal = bigDecimal.add(new BigDecimal("0.0033"));
			} else if (alipay != null) {
				bigDecimal = bigDecimal.add(new BigDecimal("0.0033"));
			} else if (codePay != null) {
			} else if (bankCard != null) {
				bigDecimal = bigDecimal.add(new BigDecimal(
					(WalletCardType.CREDIT.getValue().equals(bankCard.cardType)) ? "0.0055"
						: "0.0025"));
			}
			return bigDecimal;
		}

		@ApiModel
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Balance {

			@ApiModelProperty(required = true, name = "wallet_id", value = "钱包用户登陆态ID")
			private Long payerWalletId;

			@ApiModelProperty(value = "渠道出资额(单位分)")
			private Long amount;
		}

		@ApiModel
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Wechat {

			@ApiModelProperty(name = "pay_type", value = "21:微信小程序支付 22:微信原生APP支付 23:微信原生H5支付 24:微信JS支付(公众号) 25:微信扫码支付(正扫)")
			private Byte payType;

			@ApiModelProperty(value = "渠道出资额(单位分)")
			private Long amount;

			@ApiModelProperty(name = "open_id", value = "微信JS支付openid")
			private String openId;

			@ApiModelProperty(value = "用户下单及调起支付的终端IP")
			private String cusip;

			@ApiModelProperty(name = "sub_app_id", value = "微信端应用ID:appid（H5支付）")
			private String subAppId;

			@ApiModelProperty(name = "scene_info", value = "场景信息（H5支付）")
			private String sceneInfo;
		}

		@ApiModel
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Alipay {

			@ApiModelProperty(name = "pay_type", value = "31：支付宝扫码支付(正扫) 32：支付宝JS支付(生活号) 33：支付宝原生")
			private Byte payType;

			@ApiModelProperty(value = "渠道出资额(单位分)")
			private Long amount;

			@ApiModelProperty(name = "user_id", value = "支付宝JS支付user_id")
			private String userId;

		}

		@ApiModel
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class CodePay {

			@ApiModelProperty(name = "pay_type", value = "41：收银宝刷卡支付（被扫）")
			private Byte payType;

			@ApiModelProperty(value = "渠道出资额(单位分)")
			private Long amount;

			@ApiModelProperty(value = "支付授权码，支付宝被扫刷卡支付时,用户的付款二维码")
			private String authcode;
		}

		@ApiModel
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class BankCard {

			@ApiModelProperty(name = "pay_type", value = "51：银行卡快捷支付")
			private Byte payType;

			@ApiModelProperty(value = "银行卡号")
			private String bankCardNo;

			@ApiModelProperty(value = "渠道出资额(单位分)")
			private Long amount;

			@ApiModelProperty(value = "银行卡类型 1-储蓄卡 2-信用卡")
			private Byte cardType;

		}

	}

	@ApiModel
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Reciever {

		@ApiModelProperty(name = "wallet_id", value = "收款人钱包ID")
		private Long walletId;

		@ApiModelProperty(value = "金额,单位:分")
		private Long amount;

	}
}
