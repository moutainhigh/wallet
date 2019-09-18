package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel(description = "4.2.20 查询订单状态")
public class GetOrderDetailReq implements YunstBaseReq {

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "getOrderDetail";
	}

}