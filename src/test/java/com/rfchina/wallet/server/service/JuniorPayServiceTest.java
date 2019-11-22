package com.rfchina.wallet.server.service;

import static org.junit.Assert.*;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JuniorPayServiceTest extends SpringBaseTest {

	@Autowired
	private JuniorPayService juniorPayService;

	@Test
	public void p0101PayInSingle() {
		PayInReq req1 = PayInReq.builder()
			.walletId(2L)
			.amount(61100000000000L)
			.bizNo(IdGenerator.createBizId("OVR", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInResp respBody = juniorPayService.payIn(Arrays.asList(req1));
		logStack(respBody);

		assertNotNull(respBody);
	}

	@Test
	public void p0102PayInBatch() {
		PayInReq req1 = PayInReq.builder()
			.walletId(2L)
			.amount(1L)
			.bizNo(IdGenerator.createBizId("SUCC", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInReq req2 = PayInReq.builder()
			.walletId(2L)
			.amount(2L)
			.bizNo(IdGenerator.createBizId("FAIL", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInResp respBody = juniorPayService.payIn(Arrays.asList(req1, req2));
		logStack(respBody);

		assertNotNull(respBody);
	}

}