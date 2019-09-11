package com.rfchina.wallet.server.bank.yunst;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.bank.yunst.response.YunstBaseResp;
import com.rfchina.wallet.server.bank.yunst.response.YunstCreateMemberResp;
import com.rfchina.wallet.server.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CreateMemberTest extends SpringBaseTest {

	private ConfigService configService;

	@Test
	public void lanch() throws Exception {
		YunstBaseResp reponse = YunstCreateMemberReq.builder$()
			.bizUserId("FHT" + System.currentTimeMillis()).memberType(2L).source(2L).build()
			.execute();


		if ("OK".equals(reponse.status)) {
			YunstCreateMemberResp.CreateMemeberResult resp = JsonUtil
				.toObject(reponse.getSignedValue(), YunstCreateMemberResp.CreateMemeberResult.class,
					objectMapper -> {
						objectMapper.configure(
							DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					});

			System.out.println(resp);
		}
	}
}
