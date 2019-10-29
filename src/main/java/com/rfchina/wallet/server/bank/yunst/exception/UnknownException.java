package com.rfchina.wallet.server.bank.yunst.exception;

import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;

public class UnknownException extends WalletResponseException {

	public UnknownException(EnumWalletResponseCode code,
		String... stringFormatArg) {
		super(code, stringFormatArg);
	}
}
