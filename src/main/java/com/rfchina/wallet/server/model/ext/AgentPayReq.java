package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@ApiModel
@Data
public class AgentPayReq {


	@ApiModelProperty(name = "receivers", value = "收款人列表")
	private List<Reciever> receivers;

	@Data
	@ApiModel
	public static class Reciever {

		@ApiModelProperty(name = "wallet_id", value = "收款人钱包ID")
		private Long walletId;

		@ApiModelProperty(value = "金额,单位:分")
		private Long amount;

		@ApiModelProperty(value = "代付接口手续费（代收无效）,单位:分")
		private Long feeAmount;

	}
}
