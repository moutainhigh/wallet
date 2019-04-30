package com.rfchina.wallet.server.bank.pudong.domain.response;

import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.util.StringIndex;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriPayResp extends PriPayReq {

	@StringIndex(23)
	@ApiModelProperty(value = "状态.0：成功 1：失败 2：处理中")
	private String status;

	@StringIndex(24)
	@ApiModelProperty(value = "错误信息")
	private String errMsg;
}
