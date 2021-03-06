package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnifiedConfirmVo {

	@ApiModelProperty(name="order_id", value = "订单id")
	private Long orderId;

	@ApiModelProperty(value = "交易编号（短信验证）")
	private String tradeNo;

	@ApiModelProperty(value = "回调地址（密码验证）")
	private String jumpUrl;
}
