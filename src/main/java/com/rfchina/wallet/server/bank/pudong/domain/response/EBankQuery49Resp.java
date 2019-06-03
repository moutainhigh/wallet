package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EBankQuery49Resp {

	@ApiModelProperty(value = "明细字段间用|分隔")
	private String detailedContent;

}
