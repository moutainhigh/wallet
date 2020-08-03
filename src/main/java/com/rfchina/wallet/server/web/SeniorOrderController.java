package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.SeniorOrderApi;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SeniorOrderController {

	@Autowired
	private SeniorOrderApi seniorOrderApi;


	@ApiOperation("高级钱包-余额明细")
	@PostMapping(UrlConstant.WALLET_SENIOR_ORDER_DETAIL)
	public ResponseValue<Pagination<WalletOrder>> seniorOrderDetail(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = false) @RequestParam(value = "wallet_id", required = false) Long walletId,
		@ApiParam(value = "交易时间开始", required = false) @RequestParam(value = "from_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
			Date fromTime,
		@ApiParam(value = "交易时间结束", required = false) @RequestParam(value = "to_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
			Date toTime,
		@ApiParam(value = "交易类型", required = false) @RequestParam(value = "trade_type", required = false) Byte tradeType,
		@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) Byte status,
		@ApiParam(value = "每页限制", required = false) @RequestParam(value = "limit") int limit,
		@ApiParam(value = "起始页偏移量", required = false) @RequestParam(value = "offset") int offset,
		@ApiParam(value = "是否统计", required = false) @RequestParam(value = "stat", required = false) Boolean stat
	) {

		if (Objects.nonNull(fromTime)) {
			toTime = Objects.isNull(toTime) ? new Date()
				: fromTime.compareTo(toTime) == 0 ? DateUtil
					.addSecs(DateUtil.addDate2(toTime, 1), -1) : toTime;
		}

		Pagination<WalletOrder> result = seniorOrderApi.queryWalletOrderDetail(accessToken,
			walletId, fromTime, toTime, tradeType, status, limit, offset, stat);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}
}
