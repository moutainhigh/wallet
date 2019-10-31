package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class RechargeResp extends WalletOrder {

	@ApiModelProperty(value = "扫码支付信息/ JS支付串信息/ 微信原生H5支付串信息")
	private String payInfo;

	@ApiModelProperty(value = "扩展参数")
	private String extendInfo;

	@ApiModelProperty(value = "微信 APP 支付信息")
	private String weChatAPPInfo;

	@ApiModelProperty(value = "短信验证")
	private Boolean smsConfirm;

	@ApiModelProperty(value = "密码验证")
	private Boolean passwordConfirm;

	@ApiModelProperty(value = "交易编号")
	private String tradeNo;

	@ApiModelProperty(value = "业务票据")
	private String ticket;

}
