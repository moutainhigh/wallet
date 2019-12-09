package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler.YunstMemberType;
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
	private WalletTunnelExtDao walletTunnelDao;


	private Long payerWalletId = 10035L;

	@Test
	public void personChangeBindPhone() throws Exception {
		WalletPerson walletPerson = walletPersonDao.selectByWalletId(payerWalletId);
		WalletTunnel channel = walletTunnelDao
			.selectByWalletId(payerWalletId, TunnelType.YUNST.getValue());

		String url = seniorWalletService
			.updateSecurityTel(walletPerson, channel, "http://www.163.com");
		log.info("changeBindPhone url = ", url);
	}

}