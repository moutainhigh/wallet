package com.rfchina.wallet.server.mapper.ext;

import static org.junit.Assert.*;

import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.HostSeqNo;
import org.apache.commons.lang3.StringUtils;
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

}