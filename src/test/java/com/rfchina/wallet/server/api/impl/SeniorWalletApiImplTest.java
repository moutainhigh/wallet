package com.rfchina.wallet.server.api.impl;

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

		String url = seniorWalletApi.updateSecurityTel(super.accessToken, walletId, "/test");
		log.info("updateSecurityTel url = {}", url);
	}

	@Test
	public void updatePayPwd() {

		String url = seniorWalletApi.updatePayPwd(super.accessToken, walletId, "/test");
		log.info("updatePayPwd url = {}", url);
	}

}