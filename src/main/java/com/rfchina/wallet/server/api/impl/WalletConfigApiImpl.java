package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.wallet.domain.model.WalletConfig;
import com.rfchina.wallet.server.api.WalletConfigApi;
import com.rfchina.wallet.server.mapper.ext.WalletConfigExtDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WalletConfigApiImpl implements WalletConfigApi {

	@Autowired
	private WalletConfigExtDao walletConfigDao;


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletConfig getUniConfig() {
		return walletConfigDao.selectUniCfg();
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void editUniConfig(Long manualWithdrawCompanyMin, Long manualWithdrawPersonMin) {
		WalletConfig config = walletConfigDao.selectUniCfg();
		config.setManualWithdrawCompanyMin(manualWithdrawCompanyMin);
		config.setManualWithdrawPersonMin(manualWithdrawPersonMin);
		walletConfigDao.updateByPrimaryKey(config);
	}
}
