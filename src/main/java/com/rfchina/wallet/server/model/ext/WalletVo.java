package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.Wallet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WalletVo extends Wallet {

	@ApiModelProperty(name="biz_user_id", value = "通道会员id")
	private String bizUserId;
}
