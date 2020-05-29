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

		String bizUserId = "TESTWU305";
		String vspMerchantid = "56058104816VDT7";
		String vspCusid = "56058104816U8U6";
		String appId = "00183310";
		String vspTermid = "12243790";
		String oper = "query";

		VspTermidResp resp = yunstUserHandler
			.vspTermid(bizUserId, vspMerchantid, vspCusid, appId, vspTermid, oper);

		log.info("{}", resp);
	}
}