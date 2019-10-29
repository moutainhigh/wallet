package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RechargeConfirmVo {

	@ApiModelProperty(name="apply_id", value = "工单id")
	private Long applyId;

	@ApiModelProperty(name="order_no", value = "订单号")
	private String orderNo;

	@ApiModelProperty(value = "交易编号")
	private String tradeNo;
}
