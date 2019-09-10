package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.server.api.YunstApi;
import com.rfchina.wallet.server.service.yunst.handler.YunstHandler;
import com.rfchina.wallet.server.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.yunst.response.YunstCreateMemberResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YunstApiImpl implements YunstApi {
	@Autowired
	private YunstHandler yunstHandler;
	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public YunstCreateMemberResp createYunstMember(String accessToken, String bizUserId, Integer type)
			throws Exception {
		YunstCreateMemberReq.YunstCreateMemberReqBuilder reqBuilder = YunstCreateMemberReq.builder$().source(2L);
		Long memberType = 2L;
		if (type == 1){
			bizUserId = "U"+bizUserId;
			memberType = 3L;
		}else if (type == 2){
			reqBuilder.bizUserId("C"+bizUserId);
		}else {
			throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_INVALID_PARAMS,"type");
		}
		return yunstHandler.createMember(bizUserId,memberType);
	}
}
