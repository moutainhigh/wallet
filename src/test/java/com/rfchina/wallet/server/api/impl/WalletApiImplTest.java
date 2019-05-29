package com.rfchina.wallet.server.api.impl;

import static junit.framework.TestCase.assertTrue;

import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.security.SecurityCoder;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.SignUtil;
import com.rfchina.wallet.server.SignedSpringTest;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.service.AppService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class WalletApiImplTest extends SignedSpringTest {

	@Autowired
	private WalletApi walletApi;

	@Test
	public void query() {
		String bizNo = "Test201904308142";
		List<PayStatusResp> result = walletApi.queryWalletLog(accessToken, bizNo, null);
		logStack(result);
	}

	@Test
	public void quartzPay() {
		walletApi.quartzPay();
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
}