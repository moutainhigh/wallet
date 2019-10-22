package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@ApiModel
@Data
public class RefundReq {

	@ApiModelProperty(value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(value = "托管代收订单中的收款人的退款金额")
	private List<RefundInfo> refundList;

	@ApiModelProperty(value = "手续费,单位:分")
	private Long feeAmount;

	@ApiModel
	@Data
	public static class RefundInfo {

		@ApiModelProperty(value = "钱包ID")
		private Long walletId;

		@ApiModelProperty(value = "金额,单位:分")
		private Long amount;
	}
}
