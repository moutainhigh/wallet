package com.rfchina.wallet.server.model.ext;

public class PayReq {
	/**
	 * 电子凭证号
	 */
	private String elecChequeNo;
	/**
	 * 付款账号
	 */
	private String acctNo;
	/**
	 * 付款人账户名称
	 */
	private String acctName;
	/**
	 * 预约日期  格式：20041008
	 */
	private String bespeakDate;
	/**
	 * 收款人账号
	 */
	private String payeeAcctNo;
	/**
	 * 收款人名称
	 */
	private String payeeName;
	/**
	 * 收款人账户类型 0-对公账号 1-卡
	 */
	private String payeeType;
	/**
	 * 收款行名称
	 */
	private String payeeBankName;
	/**
	 * 收款人地址
	 */
	private String payeeAddress;
	/**
	 * 支付金额
	 */
	private String amount;
	/**
	 * 本行/他行标志 0：表示本行 1：表示他行
	 */
	private String sysFlag;
	/**
	 * 同城异地标志 0：同城 1：异地
	 */
	private String remitLocation;
	/**
	 * 附言
	 */
	private String note;
	/**
	 * 收款行速选标志
	 */
	private String payeeBankSelectFlag;
	/**
	 * 支付号
	 */
	private String payeeBankNo;
	/**
	 * 支付用途 收款人为个人客户时必须输入
	 */
	private String payPurpose;

}
