package com.rfchina.wallet.server.api.impl;

import static junit.framework.TestCase.assertTrue;

import com.alibaba.fastjson.JSON;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletCardVo;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
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
	public void queryWalletInfo() {
		WalletInfoResp result = walletApi.queryWalletInfo(accessToken, 2L);
		assertTrue(result != null);
	}

	@Test
	public void queryWalletCard() {
		List<WalletCardVo> walletCardVos = walletApi.queryWalletCard(super.accessToken, 10035L);
		log.info("{}", JsonUtil.toJSON(walletCardVos));
	}

	@Test
	public void walletList(){
		Pagination<Wallet> walletPagination = walletApi.walletList(super.accessToken, "sn", null, null, null, 100, 0, true);
		log.info("result: {}", JSON.toJSON(walletPagination));
	}
}