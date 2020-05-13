package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SumOfFeeVo {

	@ApiModelProperty(name = "third_tunnel_fee", value = "第三方的通道手续费")
	private Long thirdTunnelFee;

	@ApiModelProperty(name = "local_tunnel_fee", value = "本地的通道手续费")
	private Long localTunnelFee;

	@ApiModelProperty(name = "local_tunnel_count", value = "本地的通道次数")
	private Long localTunnelCount;

}
