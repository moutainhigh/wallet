package com.rfchina.wallet.server.bank.yunst.request;

import java.io.Serializable;

public interface YunstBaseReq extends Serializable {
	/**
	 * 通联sdk service名称
	 * @return
	 */
	String getServcieName();
	/**
	 * 通联sdk method名称
	 * @return
	 */
	String getMethodName();
}
