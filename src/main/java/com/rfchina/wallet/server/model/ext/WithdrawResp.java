package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.WalletOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WithdrawResp extends WalletOrder {

	@ApiModelProperty(value = "商户系统用户标识")
	private String bizUserId ;

	@ApiModelProperty(value = "业务票据")
	private String ticket;

}
