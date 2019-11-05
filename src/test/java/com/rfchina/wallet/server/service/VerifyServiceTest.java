package com.rfchina.wallet.server.service;

import static org.junit.Assert.*;

import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.server.SpringBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class VerifyServiceTest extends SpringBaseTest {

	@Autowired
	private VerifyService verifyService;

	@Test
	public void checkSeniorWallet() {
		Wallet wallet = verifyService.checkSeniorWallet(10001L);
		log.info("{}", wallet);
	}
}