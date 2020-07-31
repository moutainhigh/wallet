package com.rfchina.wallet.server.api.impl;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.WalletArea;
import com.rfchina.wallet.domain.model.ext.WalletTerminalExt;
import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.WalletTerminalApi;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletTerminalApiImplTest extends SpringApiTest {

	@Autowired
	private WalletTerminalApi walletTerminalApi;

//	@Test
//	public void bindTerminal() {
//		walletTerminalApi.bindTerminal(super.accessToken, "440000", "", "", "");
//	}
//
//	@Test
//	public void bindVspId() {
//		walletTerminalApi.bindVspId(super.accessToken, "440000", "", "", 0L, "", "");
//	}

	@Test
	public void queryArea() {
		Pagination<WalletArea> page = walletTerminalApi.queryArea(super.accessToken, 100, 0);
		Assert.assertTrue(page.getTotal() > 0);
	}

	@Test
	public void queryTerminalExt() {
		Pagination<WalletTerminalExt> page = walletTerminalApi
			.queryTerminalExt(null, "440000", null, null, null, null, 100, 0);
		Assert.assertTrue(page != null);
	}
}