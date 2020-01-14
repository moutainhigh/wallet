package com.rfchina.wallet.server.service;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class WalletServiceTest extends SpringBaseTest {

	@Autowired
	private WalletService walletService;

	private Long walletId = 2L;




	@Test
	public void queryWalletInfo() {
		WalletInfoResp resp = walletService.queryWalletInfo(walletId);

		log.info(JSON.toJSONString(resp));
		assertNotNull(resp);

	}

	@Test
	public void queryWalletInfoByUid() {
		Long userId = 33443L;
		WalletInfoResp resp = walletService.queryWalletInfoByUserId(userId);

		log.info(JSON.toJSONString(resp));
		assertNotNull(resp);

	}


	@Test
	public void auditWalletPerson() {
		Long walletId = 2L;
//		walletService.activeWalletPerson(walletId, "张三", (byte) 1, "430224197009285684",
//			1L);
	}

	@Test
	public void auditWalletCompany() {
		Long walletId = 3L;
//		walletService.activeWalletCompany(walletId, "公司", 1L);
	}



}