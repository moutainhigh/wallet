package com.rfchina.wallet.server.bank.pudong.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeader extends Header {

	/**
	 * 交易码
	 */
	private String transCode;
	/**
	 * 签名标志  0：表示数据没有签名 1：表示数据经过签名 企业提交浦发的所有报文都必须签名，浦发返回的结果报文除9001，9003外，其它都经过签名。
	 */
	private String signFlag;

	/**
	 * 报文号 对应企业发送报文的报文号 当天内唯一
	 */
	private String packetID;
	/**
	 * 交易时间戳
	 */
	private String timeStamp;

	/**
	 * 企业客户号 (请求头)
	 */
	private String masterID;
}
