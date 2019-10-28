package com.rfchina.wallet.server.bank.yunst.exception;

import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.WalletResponseCode;
import lombok.Getter;

@Getter
public class CommonGatewayException extends WalletResponseException {

	/**
	 * 银行错误编码
	 */
	private String bankErrCode;

	/**
	 * 银行错误信息
	 */
	private String bankErrMsg;

	public CommonGatewayException(WalletResponseCode.EnumWalletResponseCode code, String bankErrCode,String bankErrMsg) {
		super(code.getValue(), WalletResponseCode.getMsg(code), null);
		this.bankErrMsg = bankErrMsg;
		this.bankErrCode = bankErrCode;
	}

}
