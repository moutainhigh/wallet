package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.service.JuniorWalletService;
import com.rfchina.wallet.server.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class WalletApiImpl implements WalletApi {

	@Autowired
	private WalletService walletService;

	@Autowired
	private JuniorWalletService juniorWalletService;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public List<PayStatusResp> queryWalletLog(String accessToken,
		@ParamValid(nullable = true) String bizNo,
		@ParamValid(nullable = true) String batchNo) {
		return walletService.queryWalletLog(bizNo, batchNo);
	}

	@Log
	@Override
	public void quartzUpdate() {
		walletService.quartzUpdate();
	}

	@Log
	@Override
	public void quartzPay() {
		walletService.quartzPay();
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfo(String accessToken, Long walletId) {
		return walletService.queryWalletInfo(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfoByUserId(String accessToken, Long userId) {
		return walletService.queryWalletInfoByUserId(userId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Wallet createWallet(String accessToken, Byte type, String title, Byte source) {
		return walletService.createWallet(type, title, source);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<WalletLog> walletLogList(String accessToken, Long walletId, Date startTime,
		Date endTime, int limit, long offset, Boolean stat) {
		return walletService.walletLogList(walletId, startTime, endTime,
			limit, offset, stat);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public List<WalletCard> bankCardList(String accessToken, Long walletId) {
		return walletService.bankCardList(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletCard bindBankCard(String accessToken, Long walletId, String bankCode,
		String bankAccount, String depositName, Integer isDef,
		String telephone) {
		return walletService.bindBankCard(walletId, bankCode, bankAccount, depositName,
			isDef, telephone);
	}

	@Override
	public List<BankClass> bankClassList() {
		return walletService.bankClassList();
	}

	@Override
	public List<BankArea> bankAreaList(@ParamValid(nullable = false) String classCode) {
		return walletService.bankAreaList(classCode);
	}

	@Override
	public List<Bank> bankList(@ParamValid(nullable = false) String classCode,
		@ParamValid(nullable = false) String areaCode) {
		return walletService.bankList(classCode, areaCode);
	}
}
