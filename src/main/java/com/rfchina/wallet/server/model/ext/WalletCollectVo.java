package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.WalletClearInfo;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WalletCollectVo {

	private WalletCollect walletCollect;

	private List<WalletCollectMethod> methods;

	private List<WalletClearInfo> clearInfos;

}
