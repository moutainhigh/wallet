package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.functionnal.LockDone;
import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.server.api.SeniorChargingApi;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.service.SeniorChargingService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeniorChargingApiImpl implements SeniorChargingApi {

	@Autowired
	private SeniorChargingService seniorChargingService;

	@Autowired
	private SimpleExclusiveLock lock;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<StatCharging> queryCharging(String accessToken, Integer limit, Integer offset,
		Boolean stat) {
		return seniorChargingService.queryCharging(limit, offset, stat);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<StatChargingDetailVo> queryChargingDetail(String accessToken,
		String startTime,
		String endTime, Integer limit, Integer offset, Boolean stat, Boolean asc) {
		return seniorChargingService.queryChargingDetail(
			DateUtil.parse(startTime, DateUtil.STANDARD_DTAE_PATTERN),
			DateUtil.parse(endTime, DateUtil.STANDARD_DTAE_PATTERN),
			limit, offset, stat, asc);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void chargingRedo(String accessToken, Date startTime, Date endTime) {
		String key = String.format("%s_%s",
			DateUtil.formatDate(startTime, DateUtil.SHORT_DTAE_PATTERN),
			DateUtil.formatDate(endTime, DateUtil.SHORT_DTAE_PATTERN));
		new LockDone(lock).apply(key, 30, () -> {
			seniorChargingService.chargingRedo(startTime, endTime);
		});
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	public StatCharging queryChargingByCurrentMonth(String accessToken) {
		return seniorChargingService.queryChargingByCurrentMonth();
	}
}