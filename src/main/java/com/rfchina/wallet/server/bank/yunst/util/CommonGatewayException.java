package com.rfchina.wallet.server.bank.yunst.util;

import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.wallet.domain.exception.WalletResponseException;

public class CommonGatewayException extends WalletResponseException {

	/**
	 * 银行错误编码
	 */
	private String bankErrCode;

	/**
	 * 银行错误信息
	 */
	private String bankErrMsg;

	public CommonGatewayException(EnumResponseCode code, String bankErrCode,String bankErrMsg) {
		super(code.getValue(), ResponseCode.getMsg(code), null);
		this.bankErrMsg = bankErrMsg;
		this.bankErrCode = bankErrCode;
	}

}
