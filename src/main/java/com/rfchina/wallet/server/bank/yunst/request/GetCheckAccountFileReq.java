package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@ApiModel("4.3.2 商户集合对账文件下载")
public class GetCheckAccountFileReq implements YunstBaseReq {

	@ApiModelProperty(value = "对账文件日期 yyyyMMdd ", required = true)
	private String date;

	@ApiModelProperty(value = "文件类型 1-明细 2-汇总 默认为 1 ", required = false)
	private Long fileType;

	@Override
	public String getServcieName() {
		return "MerchantService";
	}

	@Override
	public String getMethodName() {
		return "getCheckAccountFile";
	}


}
