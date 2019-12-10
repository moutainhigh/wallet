package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler.YunstMemberType;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorCardServiceTest extends SpringBaseTest {

	public static final long WALLET_ID = 10035L;
	public static final String CARD_PHONE = "13710819640";

	@Autowired
	private SeniorCardService seniorCardService;
	@Autowired
	private YunstUserHandler yunstUserHandler;
	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Test
	public void preBindBandCard() {
		String resp = seniorCardService.preBindBandCard(WALLET_ID, "6214850201481956",
			"观富昌", CARD_PHONE, "440923198711033434", null, null, "308581002407");
		log.info("预绑卡 {}", resp);

	}

	@Test
	public void confirmBindCard() {
		seniorCardService.confirmBindCard(WALLET_ID,
			"468590", "fe00db5b-8095-4ce0-8542-e4a54f5b5bf1");
	}

	@Test
	public void unBindCard() {
		seniorCardService.unBindCard(17L);
	}


}