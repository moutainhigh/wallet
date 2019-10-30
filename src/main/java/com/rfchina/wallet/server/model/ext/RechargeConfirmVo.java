package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RechargeConfirmVo {

	@ApiModelProperty(name="order_id", value = "订单id")
	private Long orderId;

	@ApiModelProperty(value = "交易编号")
	private String tradeNo;
}
