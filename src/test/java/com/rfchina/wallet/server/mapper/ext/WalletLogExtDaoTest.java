package com.rfchina.wallet.server.mapper.ext;

import static org.junit.Assert.*;

import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.HostSeqNo;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletLogExtDaoTest extends SpringBaseTest {

	@Autowired
	private WalletApplyExtDao walletApplyExtDao;

	@Test
	public void selectHostAcctNo() {
		HostSeqNo hostSeqNo = walletApplyExtDao.selectHostAcctNo("5248588993");
		assertTrue(hostSeqNo != null);
		assertTrue(hostSeqNo.getHostAcceptNo() != null);
	}

	@Test
	public void updateAcceptNoError() {
//		walletLogExtDao
//			.updateAcceptNoErrMsg("5248342611", "TEST-0", "测试失败");
	}

	@Test
	public void updateAcceptNoStatus() {
		walletApplyExtDao.updateAcceptNoStatus("5248342611",
			WalletApplyStatus.FAIL.getValue(), new Date(), new Date());
	}

	@Test
	public void selectByStatusNotified(){
		List<WalletApply> walletLogs = walletApplyExtDao
			.selectByStatusNotified(WalletApplyStatus.WAIT_DEAL.getValue(), 100);
		assertTrue(!walletLogs.isEmpty());
	}
}