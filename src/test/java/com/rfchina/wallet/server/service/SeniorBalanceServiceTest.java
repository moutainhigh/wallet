package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.SpringBaseTest;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SeniorBalanceServiceTest extends SpringBaseTest {

	@Autowired
	private SeniorBalanceService seniorBalanceService;

	@Test
	public void balance() {
		seniorBalanceService.doBalance(DateUtil.addDate2(new Date(), -1));
	}
}