package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
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
	public void exportTunnelBalance(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String fileName,
		@ParamValid(nullable = false) Byte type,
		@ParamValid(nullable = false) String uniqueCode,
		@ParamValid(nullable = false) String startTime,
		@ParamValid(nullable = false) String endTime
	) {


		reportService
			.exportChargingDetail(uniqueCode, fileName,type, startTime, endTime, ExportType.VERIFY);
	}


}
