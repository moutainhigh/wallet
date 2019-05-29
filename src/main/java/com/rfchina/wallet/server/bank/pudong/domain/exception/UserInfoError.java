package com.rfchina.wallet.server.bank.pudong.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoError extends Exception implements IGatewayError {
	/**
	 * 网银错误码
	 */
	private String errCode;
	/**
	 * 错误信息
	 */
	private String errMsg;

	public boolean isUserErr() {
		return true;
	}

	public UserInfoError(Exception e){
		super(e);
	}
}
