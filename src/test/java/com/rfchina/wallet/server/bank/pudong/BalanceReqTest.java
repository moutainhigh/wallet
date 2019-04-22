package com.rfchina.wallet.server.bank.pudong;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.Builder.BalanceReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.response.BalanceRespBody.Balance;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Test;
import static com.rfchina.wallet.server.bank.pudong.TestingData.*;

@Slf4j
public class BalanceReqTest {

	@Test
	public void lanch() throws Exception {
		BalanceReqBuilder req = BalanceReqBuilder.builder()
			.acctNo(CMP_ACCT_ID)
			.masterId(MASTER_ID)
			.packetId(PACKET_ID)
			.build();

		Balance balance = req.lanch(new OkHttpClient.Builder().build());
		log.info(JSON.toJSONString(balance));
		assertTrue(balance != null);
	}
}