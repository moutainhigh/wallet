package com.rfchina.wallet.server.api.impl;

import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.SeniorCardApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorCardApiImplTest extends SpringApiTest {

	public static final long WALLET_ID = 10035L;
	public static final String CARD_PHONE = "13710819640";
	@Autowired
	private SeniorCardApi seniorCardApi;

	@Test
	public void preBindBandCard() {
		String resp = seniorCardApi
			.preBindBandCard(super.accessToken, WALLET_ID, "6214850201481956",
				"观富昌", CARD_PHONE, "440923198711033434", null, null);
		log.info("预绑卡 {}", resp);

	}

	@Test
	public void confirmBindCard() {
		seniorCardApi.confirmBindCard(super.accessToken, WALLET_ID,
			"177982799745", null);
	}

	@Test
	public void preBindBandCard2() {
		String resp = seniorCardApi
			.preBindBandCard(super.accessToken, 10054L, "6222023602016631699",
				"刘裕", "15088069541", "44011119911012183X", null, null);
		log.info("预绑卡 {}", resp);

	}

	@Test
	public void confirmBindCard2() {
		seniorCardApi.confirmBindCard(super.accessToken, 10054L,
			"132506", "78d4b700-b4df-418d-be22-7a95a0fdd522");
	}

	@Test
	public void unBindCard() {
		seniorCardApi.unBindCard(super.accessToken, 10054L);
	}
}