package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.domain.model.StatChargingDetail;
import java.util.Date;

public interface SeniorChargingApi {

	Pagination<StatCharging> queryCharging(String accessToken, Integer limit, Integer offset,
		Boolean stat);

	Pagination<StatChargingDetail> queryChargingDetail(String accessToken, Date startTime, Date endTime,
		Integer limit, Integer offset, Boolean stat);

	void chargingRedo(String accessToken, Date startTime, Date endTime);
}
