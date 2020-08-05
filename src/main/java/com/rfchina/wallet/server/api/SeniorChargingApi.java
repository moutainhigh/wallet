package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import java.util.Date;

public interface SeniorChargingApi {

	Pagination<StatCharging> queryCharging(String accessToken, Integer limit, Integer offset,
		Boolean stat);

	Pagination<StatChargingDetailVo> queryChargingDetail(String accessToken, String startTime,
		String endTime, Integer limit, Integer offset, Boolean stat, Boolean asc);

	void chargingRedo(String accessToken, Date startTime, Date endTime);

	StatCharging queryChargingByCurrentMonth(String accessToken);

}
