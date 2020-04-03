package com.rfchina.wallet.server.api;

import com.rfchina.wallet.domain.model.WalletConfig;

public interface WalletConfigApi {


	WalletConfig getUniConfig();

	void editUniConfig(Long manualWithdrawCompanyMin, Long manualWithdrawPersonMin);
}
