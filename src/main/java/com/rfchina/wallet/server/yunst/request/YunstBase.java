package com.rfchina.wallet.server.yunst.request;

import java.io.Serializable;

public interface YunstBase extends Serializable {
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
