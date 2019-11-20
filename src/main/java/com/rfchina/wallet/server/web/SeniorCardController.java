package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.server.api.SeniorCardApi;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SeniorCardController {

	@Autowired
	private SeniorCardApi seniorCardApi;

	@ApiOperation("高级钱包-预绑定银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_PRE_BIND_BANK_CARD)
	public ResponseValue preBindBankCard(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "银行卡号", required = true) @RequestParam("card_no") String cardNo,
		@ApiParam(value = "姓名", required = true) @RequestParam("real_name") String realName,
		@ApiParam(value = "银行预留手机号", required = true) @RequestParam("phone") String phone,
		@ApiParam(value = "身份证", required = true) @RequestParam("identity_no") String identityNo,
		@ApiParam(value = "信用卡到期4位日期", required = false) @RequestParam(value = "validate", required = false) String validate,
		@ApiParam(value = "信用卡cvv2码", required = false) @RequestParam(value = "cvv2", required = false) String cvv2) {

		String preBindTicket = seniorCardApi.preBindBandCard(accessToken, walletId,
			cardNo, realName, phone, identityNo, validate, cvv2);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			BeanUtil.asPureStringMap("pre_bind_ticket", preBindTicket));
	}

	@ApiOperation("高级钱包-确认绑定银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_CONFIRM_BIND_CARD)
	public ResponseValue confirmBindBankCard(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "预绑卡票据", required = true) @RequestParam(value = "pre_bind_ticket") String preBindTicket,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		seniorCardApi.confirmBindCard(accessToken, walletId, verifyCode, preBindTicket);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-解绑银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_UNBIND_CARD)
	public ResponseValue unBindBankCard(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "银行卡id", required = true) @RequestParam("card_id") Long cardId) {

		seniorCardApi.unBindCard(accessToken, cardId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

}
