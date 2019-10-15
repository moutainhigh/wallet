package com.rfchina.wallet.server.api.impl;

import static junit.framework.TestCase.assertTrue;

import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletApiImplTest extends SpringApiTest {

	@Autowired
	private WalletApi walletApi;

	@Test
	public void query() {
		String bizNo = "Test201904308142";
		List<PayStatusResp> result = walletApi.queryWalletApply(accessToken, bizNo, null);
		logStack(result);
	}

	@Test
	public void quartzPay() {
//		walletApi.quartzPay();
	}

	@Test
	public void quartzUpdate() {
		walletApi.quartzUpdate();
	}

	@Test
	public void queryWalletInfo() {
		WalletInfoResp result = walletApi.queryWalletInfo(accessToken, 2L);
		assertTrue(result != null);
	}

	@Test
	public void quartzNotify() {
		walletApi.quartzNotify();
	}
}