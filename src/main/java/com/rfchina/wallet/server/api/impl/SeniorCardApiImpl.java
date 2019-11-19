package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.ParamVerify;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.wallet.server.api.SeniorCardApi;
import com.rfchina.wallet.server.service.SeniorCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeniorCardApiImpl implements SeniorCardApi {

	@Autowired
	private SeniorCardService seniorCardService;

	/**
	 * 高级钱包-预绑定银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public String preBindBandCard(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) String cardNo,
		@ParamValid(nullable = false) String realName,
		@ParamValid(nullable = false) String phone,
		@ParamValid(nullable = false) String identityNo,
		String validate, String cvv2) {

		return seniorCardService
			.preBindBandCard(walletId, cardNo, realName, phone, identityNo, validate, cvv2);
	}


	/**
	 * 高级钱包-确认绑定银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public void confirmBindCard(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) String verifyCode,
		@ParamValid(nullable = false) String preBindTicket) {

		seniorCardService.confirmBindCard(walletId, verifyCode, preBindTicket);
	}

	/**
	 * 解绑银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public void unBindCard(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Long cardId) {

		seniorCardService.unBindCard(cardId);
	}

}
