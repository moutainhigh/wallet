package com.rfchina.wallet.bank.pudong;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.builder.BalanceReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.response.BalanceRespBody.Balance;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static com.rfchina.wallet.bank.pudong.TestingData.*;

@Slf4j
public class BalanceReqTest {

	private String signUrl = "http://192.168.197.217:5666";

	private String hostUrl = "http://192.168.197.217:5777";

	@Test
	public void lanch() throws Exception {
		BalanceReqBuilder req = BalanceReqBuilder.builder()
			.acctNo(CMP_ACCT_ID)
			.masterId(MASTER_ID)
			.packetId(PACKET_ID)
			.build();

		Balance balance = req.lanch(hostUrl, signUrl, new OkHttpClient.Builder().build());
		log.info(JSON.toJSONString(balance));
		assertTrue(balance != null);
	}
}