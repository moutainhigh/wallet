package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.wallet.server.api.JuniorWalletApi;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.service.JuniorWalletService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JuniorWalletApiImpl implements JuniorWalletApi {

	@Autowired
	private JuniorWalletService juniorWalletService;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public PayInResp payIn(String accessToken, List<PayInReq> payInReqs) {
		return juniorWalletService.payIn(payInReqs);
	}


}
