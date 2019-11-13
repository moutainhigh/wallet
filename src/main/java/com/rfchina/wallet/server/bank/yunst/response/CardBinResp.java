package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CardBinResp {

	@ApiModelProperty(name = "card_bin_info ", value = "卡 bin 信息")
	private String cardBinInfo;
}
