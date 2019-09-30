package com.rfchina.wallet.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.backoff.Sleeper;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WalletTest extends WalletBaseTest {

	@Autowired
	private com.rfchina.internal.api.ApiClient internalApiClient;

	@Test
	public void testWalletInfo() {
		walletInfo(36L);
	}

	@Test
	public void testCreateWallet() {
		createWallet((byte) 2, "个人钱包1", (byte) 3);
	}

	@Test
	public void testWalletLogList() {
		walletLogList(1L, null, null, 10, 0, true);
	}

	@Test
	public void testBindingBankCardList() {
		bindingBankCardList(1L);
	}

	@Test
	public void testBindBankCard() {
		bindBankCard(1L, "402336100092", "12345678901234567890", "哈哈", 1, "");
	}

	@Test
	public void testBankClassList() {
		bankClassList();
	}

	@Test
	public void testBankAreaList() {
		bankAreaList("001");
	}

	@Test
	public void testBankList() {
		bankList("001", "4910");
	}

	@Test
	public void testQueryWalletByUserId() {
		queryWalletByUserId(30799L);
	}

	@Test
	public void testSendVerifyCode() {
		sendVerifyCode("13560166318", 1, "127.0.0.1");
	}

	@Test
	public void testLoginWithVerify() {
		loginWithVerify("13560166318", "12345", 1, "127.0.0.1");
	}

	@Test
	public void testSetStatusFailWithApplyBill() throws Exception {
		setStatusFailWithApplyBill("43", "1", "rfchina", "fail");
		Thread.sleep(3000);
	}
}
