package com.rfchina.wallet.server.api.impl;

import static org.junit.Assert.*;

import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.SeniorWalletApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorWalletApiImplTest extends SpringApiTest {

	@Autowired
	private SeniorWalletApi seniorWalletApi;
	private Long walletId = 10035L;

	@Test
	public void resetSecurityTel() {

		String url = seniorWalletApi.resetSecurityTel(super.accessToken, walletId, "/test");
		log.info("resetSecurityTel url = {}", url);
	}

	@Test
	public void resetPayPwd() {

		String url = seniorWalletApi.resetPayPwd(super.accessToken, walletId, "/test");
		log.info("resetPayPwd url = {}", url);
	}
}