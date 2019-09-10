package com.rfchina.wallet.yunst;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.yunst.response.YunstBaseResp;
import com.rfchina.wallet.server.yunst.response.YunstCreateMemberResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CreateMemberTest {

	@Test
	public void lanch() throws Exception {
		YunstBaseResp reponse = YunstCreateMemberReq.builder$().bizUserId("FHT"+System.currentTimeMillis()).memberType(2L).source(2L).build().execute();

		if ("OK".equals(reponse.status)){
			YunstCreateMemberResp.CreateMemeberResult resp = JsonUtil.toObject(reponse.getSignedValue(), YunstCreateMemberResp.CreateMemeberResult.class,objectMapper -> {objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);});

			System.out.println(resp);
		}
	}
}
