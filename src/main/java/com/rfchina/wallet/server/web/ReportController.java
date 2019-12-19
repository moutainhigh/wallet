package com.rfchina.wallet.server.web;


import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.server.api.SeniorChargingApi;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class ReportController {

	@Autowired
	private SeniorChargingApi seniorChargingApi;

	@ApiOperation("手续费报表")
	@PostMapping(UrlConstant.REPORT_CHARGING_QUERY)
	public ResponseValue<Pagination<StatCharging>> chargingQuery(
		@RequestParam("access_token") String accessToken,
		@RequestParam("limit") Integer limit,
		@RequestParam("offset") Integer offset,
		@RequestParam("stat") Boolean stat) {

		Pagination<StatCharging> page = seniorChargingApi
			.queryCharging(accessToken, limit, offset, stat);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}

	@ApiOperation("手续费报表")
	@PostMapping(UrlConstant.REPORT_CHARGING_DETAIL)
	public ResponseValue<Pagination<StatChargingDetailVo>> chargingDetailQuery(
		@RequestParam("access_token") String accessToken,
		@RequestParam("start_time") @DateTimeFormat(pattern = DateUtil.STANDARD_DTAETIME_PATTERN) String startTime,
		@RequestParam("end_time") @DateTimeFormat(pattern = DateUtil.STANDARD_DTAETIME_PATTERN) String endTime,
		@RequestParam("limit") Integer limit,
		@RequestParam("offset") Integer offset,
		@RequestParam("stat") Boolean stat) {

		Pagination<StatChargingDetailVo> page = seniorChargingApi
			.queryChargingDetail(accessToken,
				DateUtil.parse(startTime, DateUtil.STANDARD_DTAETIME_PATTERN),
				DateUtil.parse(endTime, DateUtil.STANDARD_DTAETIME_PATTERN), limit, offset, stat);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}

	@ApiOperation("手续费重做")
	@PostMapping(UrlConstant.REPORT_CHARGING_REDO)
	public ResponseValue chargingRedo(@RequestParam("access_token") String accessToken,
		@RequestParam("start_time") Date startTime,
		@RequestParam("end_time") Date endTime) {
		seniorChargingApi.chargingRedo(accessToken, startTime, endTime);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}
}
