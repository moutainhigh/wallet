package com.rfchina.wallet.server.api;

public interface ReportApi {

	void exportVerifyDetail(String accessToken, String fileName, String uniqueCode, String startTime, String endTime);
}
