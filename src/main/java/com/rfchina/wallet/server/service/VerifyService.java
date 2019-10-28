package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VerifyService {
	/**
	 * 检查钱包合法性
	 */
	public void checkWallet(Long walletId, Wallet wallet) {
		if (wallet == null) {
			log.error("高级钱包-查无此钱包, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
		}
		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包-钱包等级错误，不是高级钱包, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_LEVEL_ERROR);
		}
	}
}
