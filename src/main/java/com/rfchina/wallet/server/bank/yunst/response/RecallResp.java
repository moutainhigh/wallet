package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RecallResp {

	@ApiModelProperty(value = "分配的系统编号")
	private String sysid;

	@ApiModelProperty(value = "签名")
	public String sign;

	@ApiModelProperty(value = "请求时间")
	private String timestamp;

	@ApiModelProperty(value = "接口版本(现为 2.0")
	private String v;

	@ApiModelProperty(value = "服务请求的 JSON 对象，参与签名，非必填参数在报文可出现，也可不出现")
	private String rps;
}
