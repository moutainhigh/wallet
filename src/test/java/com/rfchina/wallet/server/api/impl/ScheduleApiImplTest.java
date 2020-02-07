package com.rfchina.wallet.server.api.impl;

import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.ScheduleApi;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduleApiImplTest extends SpringApiTest {

	@Autowired
	private ScheduleApi scheduleApi;

	@Test
	public void quartzCharging() {
		scheduleApi.quartzCharging(null);
	}
}