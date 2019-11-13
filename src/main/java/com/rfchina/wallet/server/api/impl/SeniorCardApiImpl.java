package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.wallet.server.api.SeniorCardApi;
import com.rfchina.wallet.server.service.SeniorCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	public String preBindBandCard(String accessToken, Long walletId,
		String cardNo, String realName, String phone, String identityNo, String validate,
		String cvv2) {

		return seniorCardService
			.preBindBandCard(walletId, cardNo, realName, phone, identityNo, validate, cvv2);
	}


	/**
	 * 高级钱包-确认绑定银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void confirmBindCard(String accessToken, Long walletId,
		String verifyCode, String preBindTicket) {

		seniorCardService.confirmBindCard(walletId, verifyCode, preBindTicket);
	}

	/**
	 * 解绑银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void unBindCard(String accessToken, Long cardId) {

		seniorCardService.unBindCard(cardId);
	}

}
