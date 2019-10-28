package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.server.api.SeniorCardApi;
import com.rfchina.wallet.server.bank.yunst.response.result.ApplyBindBankCardResp;
import com.rfchina.wallet.server.bank.yunst.util.CommonGatewayException;
import com.rfchina.wallet.server.service.VerifyService;
import com.rfchina.wallet.server.service.WalletService;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeniorCardApiImpl implements SeniorCardApi {

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletService walletService;

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Autowired
	private VerifyService verifyService;

	/**
	 * 高级钱包-预绑定银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public ApplyBindBankCardResp preBindBandCard(String accessToken, Long walletId, Byte source,
		String cardNo, String realName, String phone, String identityNo, String validate,
		String cvv2) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		verifyService.checkWallet(walletId, wallet);
		try {
			return yunstUserHandler.applyBindBankCard(walletId, source, cardNo, realName, phone,
				EnumDef.EnumIdType.ID_CARD.getValue().longValue(), identityNo, validate, cvv2);
		} catch (CommonGatewayException e) {
			String errMsg = e.getBankErrMsg();
			if (errMsg.indexOf("参数validate为空") > -1) {
				log.error("高级钱包-银行卡资料缺失, walletId: {}", walletId);
				throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_CREDIT_INVALID);
			}
			log.error("高级钱包-银行卡验证失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_INFO_INVALID);
		} catch (Exception e) {
			log.error("高级钱包-银行卡验证失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_INFO_INVALID);
		}
	}


	/**
	 * 高级钱包-确认绑定银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Long confirmBindCard(String accessToken, Long walletId, Byte source,
		String transNum, String transDate, String phone, String validate, String cvv2,
		String verifyCode) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		verifyService.checkWallet(walletId, wallet);
		try {
			yunstUserHandler.bindBankCard(walletId, source, transNum, transDate, phone, validate,
				cvv2,verifyCode);
		} catch (CommonGatewayException e) {
			String errMsg = e.getBankErrMsg();
			if (errMsg.indexOf("参数validate为空") > -1) {
				log.error("高级钱包银行卡 信用卡资料缺失, walletId: {}", walletId);
				throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_CREDIT_INVALID);
			}
			log.error("高级钱包银行卡确认绑定失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_INFO_INVALID);
		} catch (Exception e) {
			log.error("高级钱包银行卡确认绑定失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_INFO_INVALID);
		}
		return walletId;
	}

	/**
	 * 解绑银行卡
	 *
	 * @param walletId 必填, 钱包id
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param cardNo 必填, 卡号
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Long unBindCard(String accessToken, Long walletId, Byte source, String cardNo) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		verifyService.checkWallet(walletId, wallet);
		try {
			yunstUserHandler.unbindBankCard(walletId, source, cardNo);
		} catch (Exception e) {
			log.error("高级钱包银行卡解绑失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return walletId;
	}

}
