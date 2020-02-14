package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description="钱包信息，包含企业钱包和个人钱包两种")
public class WalletBaseInfoVo {

	@ApiModelProperty("钱包主要信息")
	private Wallet wallet;

	@ApiModelProperty(name="owner_list", value = "所有者列表")
	private List<WalletOwner> walletOwnerList;

}
