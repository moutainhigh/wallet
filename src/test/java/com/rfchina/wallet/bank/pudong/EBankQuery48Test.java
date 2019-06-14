package com.rfchina.wallet.bank.pudong;

import static junit.framework.TestCase.assertTrue;

import com.alibaba.fastjson.JSON;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.builder.EBankQuery48Builder;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery48RespBody;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Test;

@Slf4j
public class EBankQuery48Test {

	private String signUrl = "http://192.168.197.217:5666";

	private String hostUrl = "http://192.168.197.217:5777";

	@Test
	public void lanch() throws Exception {
		// 查询网银受理状态
		EBankQuery48Builder req = EBankQuery48Builder.builder()
			.masterId(TestingData.MASTER_ID)
			.packetId(TestingData.PACKET_ID)
			.authMasterID(TestingData.AUTH_MASTER_ID)
			.beginDate("20190612")
			.endDate("20190614")
			.acceptNo("5000399572")
			.build();

		EBankQuery48RespBody resp = req.lanch(hostUrl, signUrl, new OkHttpClient());
		log.info(JSON.toJSONString(resp));
		assertTrue(resp != null);
	}
}
