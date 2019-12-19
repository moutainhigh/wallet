package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletCompany;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description="钱包信息，包含企业钱包和个人钱包两种")
public class WalletInfoResp {

	@ApiModelProperty("钱包主要信息")
	private Wallet wallet;

	@ApiModelProperty("钱包通道信息")
	private WalletTunnel walletTunnel;

	@ApiModelProperty("钱包公司信息")
	private WalletCompany companyInfo;

	@ApiModelProperty("钱包个人信息")
	private WalletPerson personInfo;

	@ApiModelProperty("钱包默认卡")
	private WalletCard defWalletCard;

	@ApiModelProperty(name="bank_card_count", value = "银行卡数量")
	private Integer bankCardCount;

}
