package com.rfchina.wallet.server.service;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import io.github.xdiamond.client.annotation.AllKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JuniorWalletServiceTest extends SpringBaseTest {

	@Autowired
	private JuniorWalletService juniorWalletService;

	@Test
	public void p0101PayInSingle() {
		PayInReq req1 = PayInReq.builder()
			.walletId(2L)
			.amount(61100000000000L)
			.bizNo(IdGenerator.createBizId("OVR", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInResp respBody = juniorWalletService.payIn(Arrays.asList(req1));
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

		PayInResp respBody = juniorWalletService.payIn(Arrays.asList(req1, req2));
		logStack(respBody);

		assertNotNull(respBody);
	}

}