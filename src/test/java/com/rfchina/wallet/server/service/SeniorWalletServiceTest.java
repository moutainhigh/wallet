package com.rfchina.wallet.server.service;

import static org.junit.Assert.*;

import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorWalletServiceTest extends SpringBaseTest {

	@Autowired
	private SeniorWalletService seniorWalletService;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private WalletChannelExtDao walletChannelDao;


	private Long payerWalletId = 10035L;

	@Test
	public void personChangeBindPhone() throws Exception {
		WalletPerson walletPerson = walletPersonDao.selectByWalletId(payerWalletId);
		WalletChannel channel = walletChannelDao
			.selectByWalletId(payerWalletId, TunnelType.YUNST.getValue());

		String url = seniorWalletService
			.personChangeBindPhone(walletPerson, channel, "http://www.163.com");
		log.info("changeBindPhone url = ", url);
	}
}