package com.rfchina.wallet.server.service;

import static org.junit.Assert.assertNotNull;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.domain.model.StatChargingDetail;
import com.rfchina.wallet.server.SpringBaseTest;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorChargingServiceTest extends SpringBaseTest {

	@Autowired
	private SeniorChargingService seniorChargingService;

	@Test
	public void doCharging() {
		seniorChargingService.doExtract(TunnelType.YUNST, new Date());
		seniorChargingService.doCharging(TunnelType.YUNST, new Date());
	}

	@Test
	public void queryCharging() {
		Pagination<StatCharging> page = seniorChargingService.queryCharging(100, 0, true);
		assertNotNull(page);
		log.info("{}", page);
	}

	@Test
	public void queryChargingDetail() {
		Date startTime = DateUtil.getDate2(DateUtil.getFirstDayOfMonth(new Date()));
		Date endTime = DateUtil.getDate(DateUtil.getLastDayOfMonth(new Date()));
		Pagination<StatChargingDetail> page = seniorChargingService
			.queryChargingDetail(startTime, endTime, 100, 0, true);
		assertNotNull(page);
		log.info("{}", page);
	}
}