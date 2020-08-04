package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.api.ReportApi;
import com.rfchina.wallet.server.msic.EnumWallet.ExportType;
import com.rfchina.wallet.server.service.ReportService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportApiImpl implements ReportApi {

	@Autowired
	private ReportService reportService;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	public void exportVerifyDetail(String accessToken, String fileName, String uniqueCode,
		String startTimeStr, String endTimeStr) {

		Date startTme = DateUtil.parse(startTimeStr, DateUtil.STANDARD_DTAE_PATTERN);
		Date endTime = DateUtil.parse(endTimeStr, DateUtil.STANDARD_DTAE_PATTERN);
		endTime = DateUtil.addDate2(endTime,1); // 闭区间
		reportService
			.exportChargingDetail(uniqueCode, fileName, startTme, endTime, ExportType.VERIFY);
	}


}
