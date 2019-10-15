package com.rfchina.wallet.server.mapper.ext;

import static org.junit.Assert.*;

import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletApplyExtDaoTest extends SpringBaseTest {

	@Autowired
	private WalletApplyExtDao walletApplyExtDao;


	@Test
	public void selectUnSendBatchNo() {
//		List<String> result = walletApplyExtDao.selectUnSendBatchNo(100);
//		assertTrue(result != null);
	}

	@Test
	public void selectUnFinishBatchNo() {
//		List<String> result = walletApplyExtDao.selectUnFinishBatchNo(100);
//		assertTrue(result != null);
	}

	@Test
	public void selectByBatchNo() {
		WalletApply walletApplies = walletApplyExtDao
			.selectByBatchNo("SLW20190517788233107");
		assertNotNull(walletApplies);
	}

	@Test
	public void incTryTimes() {
		int count = walletApplyExtDao.incTryTimes("SLW20190517788233107", new Date());
		logStack(count);
		assertTrue(count > 0);
	}

	@Test
	public void updateNotified() {
		int count = walletApplyExtDao.updateNotified(Arrays.asList(1L, 2L), (byte) 5);
		logStack(count);
		assertTrue(count > 0);
	}

	@Test
	public void selectByStatusNotified() {
		List<WalletApply> walletLogs = walletApplyExtDao
			.selectByStatusNotNotified(WalletApplyStatus.WAIT_DEAL.getValue(), 100);
		assertTrue(!walletLogs.isEmpty());
	}
}