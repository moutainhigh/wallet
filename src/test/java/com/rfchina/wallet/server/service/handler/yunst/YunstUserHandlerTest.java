package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.response.VspTermidResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class YunstUserHandlerTest extends SpringBaseTest {

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Test
	public void vspTermid() {

		String bizUserId = "PRODUCTWC1027649";
		String vspMerchantid = "56058104816W6FX";
		String vspCusid = "561581048164X2A";
		String appId = "00189916";
		String vspTermid = "12243778";
		String oper = "set";

		VspTermidResp resp = yunstUserHandler
			.vspTermid(bizUserId, vspMerchantid, vspCusid, appId, vspTermid, oper);

		log.info("{}", resp);
	}
}