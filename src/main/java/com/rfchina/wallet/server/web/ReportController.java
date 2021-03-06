package com.rfchina.wallet.server.web;


import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.server.api.ReportApi;
import com.rfchina.wallet.server.api.SeniorChargingApi;
import com.rfchina.wallet.server.api.SeniorOrderApi;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

	@Autowired
	private ReportApi reportApi;

	@Autowired
	private SeniorOrderApi seniorOrderApi;


	@ApiOperation("手续费明细")
	@PostMapping(UrlConstant.REPORT_CHARGING_DETAIL)
	public ResponseValue<Pagination<StatChargingDetailVo>> chargingDetailQuery(
		@ApiParam(name = "access_token", value = "访问令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(name = "start_time", value = "开始时间", required = true) @RequestParam("start_time") @DateTimeFormat(pattern = DateUtil.STANDARD_DTAETIME_PATTERN) String startTime,
		@ApiParam(name = "end_time", value = "结束时间", required = true) @RequestParam("end_time") @DateTimeFormat(pattern = DateUtil.STANDARD_DTAETIME_PATTERN) String endTime,
		@ApiParam(name = "limit", value = "必填，需要查询的数量（数量最大50）", required = true) @RequestParam("limit") Integer limit,
		@ApiParam(name = "offset", value = "必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。", required = true) @RequestParam("offset") Integer offset,
		@ApiParam(name = "stat", value = "非必填, false:否, true:是, 是否返回数据总量, 默认false", required = true) @RequestParam("stat") Boolean stat,
		@ApiParam(name = "asc", value = "升序排序", required = true) @RequestParam("asc") Boolean asc) {

		Pagination<StatChargingDetailVo> page = seniorChargingApi
			.queryChargingDetail(accessToken, startTime, endTime, limit, offset, stat, asc);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}

	@ApiOperation("手续费重做")
	@PostMapping(UrlConstant.REPORT_CHARGING_REDO)
	public ResponseValue chargingRedo(
		@ApiParam(name = "access_token", value = "访问令牌", required = true) @RequestParam(value = "access_token", required = false) String accessToken,
		@ApiParam(name = "start_time", value = "开始时间", required = true) @RequestParam("start_time") String startTime,
		@ApiParam(name = "end_time", value = "结束时间", required = true) @RequestParam("end_time") String endTime) {

		seniorChargingApi
			.chargingRedo(accessToken, DateUtil.parse(startTime, DateUtil.STANDARD_DTAE_PATTERN),
				DateUtil.parse(endTime, DateUtil.STANDARD_DTAE_PATTERN));
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-导出余额明细")
	@PostMapping(UrlConstant.REPORT_EXPORT_ORDER_DETAIL)
	public ResponseValue exportOrderDetail(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam(value = "wallet_id", required = true) Long walletId,
		@ApiParam(value = "交易时间开始", required = true) @RequestParam(value = "begin_time", required = true) String beginTime,
		@ApiParam(value = "交易时间结束", required = true) @RequestParam(value = "end_time", required = true) String endTime,
		@ApiParam(value = "交易类型", required = false) @RequestParam(value = "trade_type", required = false) Byte tradeType,
		@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) Byte status,
		@ApiParam(value = "文件名称", required = true) @RequestParam("file_name") String fileName,
		@ApiParam(value = "唯一码", required = true) @RequestParam("unique_code") String uniqueCode
	) {

		seniorOrderApi.exportOrderDetail(accessToken, walletId, beginTime, endTime,
			tradeType, status, fileName, uniqueCode);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("手续费报表")
	@PostMapping(UrlConstant.REPORT_CHARGING_QUERY)
	public ResponseValue<Pagination<StatCharging>> chargingQuery(
		@ApiParam(name = "access_token", value = "访问令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(name = "limit", value = "必填，需要查询的数量（数量最大50）", required = true) @RequestParam("limit") Integer limit,
		@ApiParam(name = "offset", value = "必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。", required = true) @RequestParam("offset") Integer offset,
		@ApiParam(name = "stat", value = "非必填, false:否, true:是, 是否返回数据总量, 默认false", required = true) @RequestParam("stat") Boolean stat) {

		Pagination<StatCharging> page = seniorChargingApi
			.queryCharging(accessToken, limit, offset, stat);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}

	@ApiOperation("当月手续费报表")
	@PostMapping(UrlConstant.REPORT_CUR_CHARGING_QUERY)
	public ResponseValue<StatCharging> curMonthChargingQuery(
		@ApiParam(name = "access_token", value = "访问令牌", required = true) @RequestParam("access_token") String accessToken) {
		StatCharging statCharging = seniorChargingApi.queryChargingByCurrentMonth(accessToken);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, statCharging);
	}

	@ApiOperation("导出通道明细文件")
	@PostMapping(UrlConstant.REPORT_TUNNEL_DETAIL_EXPORT)
	public ResponseValue exportTunnelDetail(
		@ApiParam(name = "access_token", value = "访问令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(name = "file_name", value = "文件名称", required = true) @RequestParam("file_name") String fileName,
		@ApiParam(name = "export_type", value = "导出类型。 1:实名； 2：提现支付单； 3： 手续费", required = true) @RequestParam("export_type") Byte exportType,
		@ApiParam(name = "unique_code", value = "唯一码", required = true) @RequestParam("unique_code") String uniqueCode,
		@ApiParam(name = "start_time", value = "开始时间", required = true) @RequestParam("start_time") @DateTimeFormat(pattern = DateUtil.STANDARD_DTAE_PATTERN) String startTime,
		@ApiParam(name = "end_time", value = "结束时间", required = true) @RequestParam("end_time") @DateTimeFormat(pattern = DateUtil.STANDARD_DTAE_PATTERN) String endTime
	) {

		reportApi
			.exportTunnelDetail(accessToken, fileName, exportType, uniqueCode, startTime, endTime);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

}
