package com.rfchina.wallet.server.api.impl;

import static org.junit.Assert.assertNotNull;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.JuniorPayApi;
import com.rfchina.wallet.server.api.ScheduleApi;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import java.util.Arrays;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JuniorPayApiImplTest extends SpringApiTest {

	@Autowired
	private JuniorPayApi juniorPayApi;

	@Autowired
	private ScheduleApi scheduleApi;

	@Test
	public void p01PayIn() {

		PayInReq req1 = PayInReq.builder()
			.walletId(2L)
			.amount(1L)
			.bizNo(IdGenerator.createBizId("JUNIT", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInReq req2 = PayInReq.builder()
			.walletId(2L)
			.amount(2L)
			.bizNo(IdGenerator.createBizId("JUNIT", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInResp respBody = juniorPayApi.payIn(super.accessToken, Arrays.asList(req1, req2), null);
		logStack(respBody);

		assertNotNull(respBody);
	}

	@Test
	public void p02QuartzPay() {
		scheduleApi.quartzPay();
	}

	@Test
	public void p03UpdateStatus(){
		scheduleApi.quartzUpdateJunior();
	}

}