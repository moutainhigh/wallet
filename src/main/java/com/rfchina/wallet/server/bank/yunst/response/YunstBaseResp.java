package com.rfchina.wallet.server.bank.yunst.response;

import lombok.Data;

@Data
public class YunstBaseResp {
	public String status;
	public String signedValue;
	public String sign;
	public String errorCode;
	public String message;
}
