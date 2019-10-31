package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WalletCollectResp extends WalletOrder {

	@ApiModelProperty(name = "wechat_app_info", value = "微信 APP 支付信息")
	private String weChatAPPInfo;

	@ApiModelProperty(name = "pay_info", value = "扫码支付信息/ JS 支付串信息/微信原生 H5 支付串信息")
	private String payInfo;

	@ApiModelProperty(name = "trade_no", value = "交易编号")
	private String tradeNo;

	@ApiModelProperty(value = "业务票据")
	private String ticket;
}
