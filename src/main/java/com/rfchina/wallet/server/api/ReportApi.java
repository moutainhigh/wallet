package com.rfchina.wallet.server.api;

public interface ReportApi {

	/** 通联对账-导出通道对账文件 */
	void exportTunnelBalance(String accessToken, String fileName, Byte type, String uniqueCode, String startTime, String endTime);
}
