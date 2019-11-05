package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class GetCheckAccountFileResp {

	@ApiModelProperty(value = "通商云对账文件地址")
	private String url;
}
