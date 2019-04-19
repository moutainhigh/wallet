package com.rfchina.wallet.server.bank.pudong;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Test;

import static com.rfchina.wallet.server.bank.pudong.TestingData.*;

@Slf4j
public class PayResultReqTest {

	@Test
	public void lanch() throws Exception {
		PubPayQueryBuilder req = PubPayQueryBuilder.builder()
			.masterId(MASTER_ID)
			.acctNo(CMP_ACCT_ID)
			.beginDate("20190415")
			.endDate("20190415")
			.beginNumber("1")
			.queryNumber("30")
			.build();
		PubPayQueryRespBody respBody = req.lanch(new OkHttpClient());
		log.info(JSON.toJSONString(respBody));
		assertTrue(respBody != null);

	}
}