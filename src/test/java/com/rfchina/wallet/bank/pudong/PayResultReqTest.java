package com.rfchina.wallet.bank.pudong;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Test;

import static com.rfchina.wallet.bank.pudong.TestingData.*;

@Slf4j
public class PayResultReqTest {

	private String signUrl = "http://192.168.197.217:5666";

	private String hostUrl = "http://192.168.197.217:5777";

	@Test
	public void lanch() throws Exception {
		PubPayQueryBuilder req = PubPayQueryBuilder.builder()
			.masterId(MASTER_ID)
			.acctNo(CMP_ACCT_ID)
			.beginDate("20190415")
			.endDate("20190415")
			.build();
		PubPayQueryRespBody respBody = req.lanch(hostUrl, signUrl, new OkHttpClient());
		log.info(JSON.toJSONString(respBody));
		assertTrue(respBody != null);

	}
}