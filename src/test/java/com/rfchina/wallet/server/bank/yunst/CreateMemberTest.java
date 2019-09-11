package com.rfchina.wallet.server.bank.yunst;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.bank.yunst.response.YunstBaseResp;
import com.rfchina.wallet.server.bank.yunst.response.YunstCreateMemberResp;
import com.rfchina.wallet.server.bank.yunst.response.YunstCreateMemberResp.CreateMemeberResult;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CreateMemberTest extends SpringBaseTest {

	private ConfigService configService;

	@Autowired
	private YunstTpl yunstTpl;

	@Test
	public void lanch() throws Exception {
		YunstCreateMemberReq req = YunstCreateMemberReq.builder$()
			.bizUserId("FHT" + System.currentTimeMillis()).memberType(2L).source(2L).build();

		CreateMemeberResult result = yunstTpl.execute(req, CreateMemeberResult.class);
		log.info("response = {}",result);
	}
}
