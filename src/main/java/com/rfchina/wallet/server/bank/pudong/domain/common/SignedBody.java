package com.rfchina.wallet.server.bank.pudong.domain.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignedBody {
	private String signature;

	/**
	 * 交易对应的返回码 （响应头）
	 */
	private String returnCode;

	/**
	 * 错误码对应的错误信息内容
	 */
	private String returnMsg;
}
