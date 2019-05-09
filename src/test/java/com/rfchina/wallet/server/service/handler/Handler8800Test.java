package com.rfchina.wallet.server.service.handler;

import static org.junit.Assert.*;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletLogDao;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Handler8800Test extends SpringBaseTest {

	@Autowired
	private Handler8800 handler8800;

	@Autowired
	private WalletLogDao walletLogDao;


	@Test
	public void p0101Pay() throws Exception {
		WalletLog walletLog = walletLogDao.selectByPrimaryKey(110L);

		Tuple<GatewayMethod, PayInResp> tuple = handler8800.pay(Arrays.asList(walletLog));
		assertNotNull(tuple);
		assertNotNull(tuple.right);
		assertNotNull(tuple.right.getAcceptNo());

	}

	@Test
	public void p0102UpdatePayStatus() {
		String acceptNo = "5248528489";
		List<WalletLog> walletLogs = handler8800.updatePayStatus(acceptNo,
			DateUtil.parse("2019-05-08", DateUtil.STANDARD_DTAE_PATTERN));
		assertTrue(walletLogs.size() > 0);
	}
}