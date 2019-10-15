package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "4.2.7 批量托管代付(标准版)")
public class BatchAgentPayReq implements YunstBaseReq {

	@ApiModelProperty(required = true, value = "商户批次号")
	private String bizBatchNo;

	@ApiModelProperty(required = true, value = "批量代付列表")
	private List<AgentPayReq> batchPayList;

	@ApiModelProperty(value = "商品类型")
	private Long goodsType;

	@ApiModelProperty(value = "商户系统商品编号")
	private String bizGoodsNo;

	@ApiModelProperty(required = true, value = "业务码")
	private String tradeCode;

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "batchAgentPay";
	}
}
