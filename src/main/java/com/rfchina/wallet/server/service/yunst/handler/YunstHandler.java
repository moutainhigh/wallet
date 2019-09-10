package com.rfchina.wallet.server.service.yunst.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.yunst.response.YunstBaseResp;
import com.rfchina.wallet.server.yunst.response.YunstCreateMemberResp;
import org.springframework.stereotype.Component;

@Component
public class YunstHandler {

	public YunstCreateMemberResp createMember(String bizUserId,Long memberType) throws Exception {
		YunstBaseResp reponse = YunstCreateMemberReq.builder$().bizUserId(bizUserId).memberType(memberType).source(2L).build().execute();

		if ("OK".equals(reponse.status)){
			YunstCreateMemberResp.CreateMemeberResult result = JsonUtil.toObject(reponse.getSignedValue(), YunstCreateMemberResp.CreateMemeberResult.class,objectMapper -> {objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);});
			return YunstCreateMemberResp.builder().status("0").data(result).build();
		}else {
			return YunstCreateMemberResp.builder().status("1").errorMsg(reponse.message).build();
		}
	}



}
