package com.rfchina.wallet.server.bank.yunst.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class YunstNotify {
	private String service;
	private String method;
	private Object returnValue;
	private String status;
	private String ContractNo;
	private String errorCode;
	private String message;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class CompanyAuditResult{
		private String bizUserId;
		private Long result;
		private String checkTime;
		private String remark;
		private String failReason;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class SignContractResult{
		private String bizUserId;
	}


	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class BalanceContractResult{
		private String protocolReqSn;
		private String signStatus;
		private String protocolNo;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class UpdatePhoneResult{
		private String bizUserId;
		private String newPhone;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class SetPayPwd{
		private String bizUserId;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class OrderResult{
		private Long amount;
		private String orderNo;
		private String termrefnum;
		private String traceno;
		private String channelFee;
		private String channelPaytime;
		private String extendInfo;
		private String accttype;
		private String payInterfaceOutTradeNo;
		private String buyerBizUserId;
		private String payInterfacetrxcode;
		private String payDatetime;
		private String bizOrderNo;
	}
}
