package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayTuple {

	@ApiModelProperty(value = "受理编号", hidden = true)
	private String acceptNo;

	@ApiModelProperty(value = "报文流水号", hidden = true)
	private String packetId;

	@ApiModelProperty(value = "电子凭证号", hidden = true)
	private Map<String, String> elecMap;

	@ApiModelProperty(value = "银行处理阶段", hidden = true)
	private String stage;

}
