package com.rfchina.wallet.server.bank.pudong.domain.exception;

public interface IGatewayError {

	default String getErrCode() {
		return "SLW-0001";
	}

	default String getErrMsg() {
		return "某种未知原因失败，需要人工介入排查";
	}

	default boolean isUserErr() {
		return false;
	}

	default boolean isGatewayErr() {
		return false;
	}

	default boolean isUnknownErr() {
		return false;
	}
}
