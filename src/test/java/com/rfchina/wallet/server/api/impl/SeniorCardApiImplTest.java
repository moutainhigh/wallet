package com.rfchina.wallet.server.api.impl;

import static org.junit.Assert.*;

import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.api.SeniorCardApi;
import com.rfchina.wallet.server.msic.EnumWallet.WalletSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SeniorCardApiImplTest extends SpringBaseTest {

	@Autowired
	private SeniorCardApi seniorCardApi;

	@Test
	public void preBindBandCard() {
//		seniorCardApi.preBindBandCard(null,10001L, WalletSource.USER.getValue(),"",
	}

	@Test
	public void confirmBindCard() {
	}

	@Test
	public void unBindCard() {
	}
}