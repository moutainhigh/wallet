package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletClearInfo;
import com.rfchina.wallet.domain.model.WalletClearing;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WalletApplyVo {

	private WalletApply walletApply;

	private List<WalletCollectVo> collects;

	private List<WalletClearing> clearings;

}
