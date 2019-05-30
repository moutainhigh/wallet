package com.rfchina.wallet.server.bank.pudong.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayError extends Exception implements IGatewayError {

	/**
	 * 交易码
	 */
	private String transCode;
	/**
	 * 网银错误码
	 */
	private String errCode;
	/**
	 * 错误信息
	 */
	private String errMsg;

	public boolean isGatewayErr() {
		return true;
	}

	public GatewayError(Exception e) {
		super(e);
	}
}
