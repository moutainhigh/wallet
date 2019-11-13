package com.rfchina.wallet.server.service;

import com.rfchina.wallet.server.SpringBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorCardServiceTest extends SpringBaseTest {

	public static final long WALLET_ID = 10035L;
	public static final String CARD_PHONE = "13710819640";

	@Autowired
	private SeniorCardService seniorCardService;

	@Test
	public void preBindBandCard() {
		String resp = seniorCardService.preBindBandCard(WALLET_ID, "6214850201481956",
			"观富昌", CARD_PHONE, "440923198711033434", null, null);
		log.info("预绑卡 {}", resp);

	}

	@Test
	public void confirmBindCard() {
		seniorCardService.confirmBindCard(WALLET_ID,
			"459918", "b24cc677-3e05-4e47-8728-9ebc77eb65be");
	}

	@Test
	public void unBindCard() {
		seniorCardService.unBindCard( 17L);
	}
}