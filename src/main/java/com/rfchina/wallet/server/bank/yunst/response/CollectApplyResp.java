package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CollectApplyResp {

	@ApiModelProperty(value = "支付状态")
	private String payStatus;

	@ApiModelProperty(value = "支付失败信息")
	private String payFailMessage;

	@ApiModelProperty(value = "商户系统用户标识 仅交易验证方式为“0”时返回")
	private String bizUserId;

	@ApiModelProperty(value = "通商云订单号")
	private String orderNo;

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(value = "交易编号 收银宝快捷支付必传")
	private String tradeNo;

	@ApiModelProperty(value = "POS 支付的付款码 收银宝ORDER_VSPPAY支付时必传")
	private String payCode;

	@ApiModelProperty(value = "扩展参数")
	private String extendInfo;

	@ApiModelProperty(value = "微信 APP 支付信息")
	private String weChatAPPInfo;

	@Data
	@ApiModel
	public static class WechatAppInfo {

		@ApiModelProperty(value = "签名")
		private String sign;

		@ApiModelProperty(value = "时间戳")
		private String timestamp;

		@ApiModelProperty(value = "随机字符串")
		private String noncestr;

		@ApiModelProperty(value = "商户号")
		private String partnerid;

		@ApiModelProperty(value = "预支付交易会话")
		private String prepayid;

		@ApiModelProperty(value = "扩展字段")
		private String packageStr;

		public void setPackage(String packageStr) {
			this.packageStr = packageStr;
		}

		public String getPackage() {
			return this.packageStr;
		}

		@ApiModelProperty(value = "应用 ID")
		private String appid;
	}

	@ApiModelProperty(value = "扫码支付信息/ JS支付串信息/ 微信原生H5支付串信息")
	private String payInfo;

	@ApiModelProperty(value = "交易验证方式")
	private Long validateType;
}
