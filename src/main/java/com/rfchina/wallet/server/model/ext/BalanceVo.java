package com.rfchina.wallet.server.model.ext;

import com.rfchina.platform.biztool.mapper.string.StringIndex;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BalanceVo {

	@StringIndex(1)
	@ApiModelProperty(name="order_no", value = "订单号")
	private String orderNo;

	@StringIndex(2)
	@ApiModelProperty(name="amount", value = "金额")
	private Long amount;

	public String toString(){
		return orderNo;
	}

}
