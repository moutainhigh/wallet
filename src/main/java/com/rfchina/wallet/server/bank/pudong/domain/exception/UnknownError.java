package com.rfchina.wallet.server.bank.pudong.domain.exception;


public class UnknownError extends Exception implements IGatewayError {
	public String getErrCode(){
		return "SLW-0001";
	}

	public String getErrMsg(){
		return "某种未知原因失败，需要人工介入排查";
	}

	public boolean isUnknownErr() {
		return true;
	}

	public UnknownError(Exception e){
		super(e);
	}
}
