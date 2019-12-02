package com.rfchina.wallet.server.service;

import com.rfchina.wallet.server.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduleServiceTest extends SpringBaseTest {

	@Autowired
	private ScheduleService scheduleService;

	@Test
	public void quartzUpdateJunior() {
		scheduleService.quartzUpdateJunior(100);
	}

	@Test
	public void quartzUpdateSenior() {
		scheduleService.quartzUpdateSenior(100,1);
	}

	@Test
	public void quartzPay() {
		scheduleService.doTunnelFinanceJob("");
	}

	@Test
	public void sendEmail() {
		scheduleService.sendEmail("测试", "测试邮件", "niezengming@rfchina.com,xiejueheng@rfchina.com");
	}
}