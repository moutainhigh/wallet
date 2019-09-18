package com.rfchina.wallet.server.bank.yunst.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YunstQueryBalanceResult {
	@ApiModelProperty(value = "总额 单位分")
	private Long allAmount;
	@ApiModelProperty(value = "冻结金额 单位分")
	private Long freezenAmount;

}
