package com.rfchina.wallet.server.mapper.ext;

import static org.junit.Assert.*;

import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.HostSeqNo;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletLogExtDaoTest extends SpringBaseTest {

	@Autowired
	private WalletLogExtDao walletLogExtDao;

	@Test
	public void selectHostAcctNo() {
		HostSeqNo hostSeqNo = walletLogExtDao.selectHostAcctNo("5248588993");
		assertTrue(hostSeqNo != null);
		assertTrue(hostSeqNo.getHostAcceptNo() != null);
	}

	@Test
	public void updateAcceptNoError() {
		walletLogExtDao
			.updateAcceptNoErrMsg("5248342611", "TEST-0", "测试失败");
	}

	@Test
	public void updateAcceptNoStatus() {
		walletLogExtDao.updateAcceptNoStatus("5248342611",
			WalletLogStatus.FAIL.getValue(), new Date(), new Date());
	}
}