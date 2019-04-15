package com.rfchina.wallet.server.bank.pudong.domain.common;

import lombok.Data;

@Data
public class ResponseHeader extends Header{

	/**
	 * 交易对应的返回码 （响应头）
	 */
	private String returnCode;

	/**
	 * 错误码对应的错误信息内容
	 */
	private String returnMsg;
}
