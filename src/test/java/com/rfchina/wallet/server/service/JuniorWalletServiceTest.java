package com.rfchina.wallet.server.service;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JuniorWalletServiceTest extends SpringBaseTest {

	@Autowired
	private JuniorWalletService juniorWalletService;

	@Test
	public void p0101PubPayIn() {
		PayInReq payInReq = PayInReq.builder()
			.walletId(3L)
			.amount(1L)
			.elecChequeNo(
				IdGenerator.createBizId("Eno", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInResp respBody = juniorWalletService.payIn(Arrays.asList(payInReq));
		log.info("{}.response = {}", currMethod(), JSON.toJSONString(respBody));

		assertNotNull(respBody);
		assertNotNull(respBody.getSeqNo());
	}

	@Test
	public void p0101PriPayIn() {
		PayInReq payInReq = PayInReq.builder()
			.walletId(2L)
			.amount(1L)
			.elecChequeNo(
				IdGenerator.createBizId("Test", 16, (orderId) -> true))
			.note("测试")
			.payPurpose((byte) 1)
			.build();

		PayInResp respBody = juniorWalletService.payIn(Arrays.asList(payInReq));
		log.info("{}.response = {}", currMethod(), JSON.toJSONString(respBody));

		assertNotNull(respBody);
		assertNotNull(respBody.getSeqNo());
	}

	@Test
	public void quartzUpdate(){
		juniorWalletService.quartzUpdate();
	}
}