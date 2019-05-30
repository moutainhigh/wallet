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
	public void p0101PayIn() {
		PayInReq req1 = PayInReq.builder()
			.walletId(3L)
			.amount(6000000L)
			.bizNo(IdGenerator.createBizId("Eno", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

//		PayInReq req2 = PayInReq.builder()
//			.walletId(2L)
//			.amount(6000000L)
//			.bizNo(IdGenerator.createBizId("Test", 16, (orderId) -> true))
//			.note("测试")
//			.payPurpose((byte) 1)
//			.build();

		PayInResp respBody = juniorWalletService.payIn(Arrays.asList(req1));
		logStack(respBody);

		assertNotNull(respBody);
	}

}