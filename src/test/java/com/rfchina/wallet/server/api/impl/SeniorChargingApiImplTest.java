package com.rfchina.wallet.server.api.impl;

import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.SeniorChargingApi;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SeniorChargingApiImplTest extends SpringApiTest {

	@Autowired
	private SeniorChargingApi seniorChargingApi;

//	@Test
//	public void exportChargingDetail() {
//		seniorChargingApi
//			.exportChargingDetail(super.accessToken, "test.xlsx", "2020-01-01", "2020-09-01");
//		try {
//			Thread.sleep(60 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
}