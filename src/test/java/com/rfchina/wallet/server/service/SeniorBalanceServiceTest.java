package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.SpringBaseTest;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorBalanceServiceTest extends SpringBaseTest {

	@Autowired
	private SeniorBalanceService seniorBalanceService;

	@Test
	public void balance() {
		for (int i = 0; i < 30; i++) {
			try {
				seniorBalanceService.doBalance(DateUtil.addDate2(new Date(), -i - 1));
			}catch (Exception e){
				log.error("",e);
			}
		}
	}
}