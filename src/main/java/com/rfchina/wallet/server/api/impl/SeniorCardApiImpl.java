package com.rfchina.wallet.server.api.impl;

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

	/**
	 * 高级钱包-预绑定银行卡
	 */
	@Override
	public ApplyBindBankCardResp preBindBandCard(String accessToken, Long walletId, Byte source,
		String cardNo, String realName, String phone, String identityNo, String validate,
		String cvv2) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包验证银行卡失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包验证银行卡失败, 钱包不是高级钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		try {
			return yunstUserHandler.applyBindBankCard(walletId, source, cardNo, realName, phone,
				EnumDef.EnumIdType.ID_CARD.getValue().longValue(), identityNo, validate, cvv2);
		} catch (CommonGatewayException e) {
			String errMsg = e.getBankErrMsg();
			if (errMsg.indexOf("参数validate为空") > -1) {
				log.error("高级钱包银行卡 信用卡资料缺失, walletId: {}", walletId);
				throw new WalletResponseException(
					EnumWalletResponseCode.SENIOR_BANK_CARD_CREDIT_INVALID);
			}
			log.error("高级钱包银行卡验证失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.SENIOR_BANK_CARD_INFO_INVALID);
		} catch (Exception e) {
			log.error("高级钱包银行卡验证失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.SENIOR_BANK_CARD_INFO_INVALID);
		}
	}

	/**
	 * 高级钱包-确认绑定银行卡
	 */
	@Override
	public Long confirmBindCard(String accessToken, Long walletId, Byte source,
		String transNum, String transDate, String phone, String validate, String cvv2,
		String verifyCode) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包绑定银行卡失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包绑定银行卡失败, 钱包不是高级钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		try {
			yunstUserHandler
				.bindBankCard(walletId, source, transNum, transDate, phone, validate, cvv2,
					verifyCode);
		} catch (CommonGatewayException e) {
			String errMsg = e.getBankErrMsg();
			if (errMsg.indexOf("参数validate为空") > -1) {
				log.error("高级钱包银行卡 信用卡资料缺失, walletId: {}", walletId);
				throw new WalletResponseException(
					EnumWalletResponseCode.SENIOR_BANK_CARD_CREDIT_INVALID);
			}
			log.error("高级钱包银行卡确认绑定失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.SENIOR_BANK_CARD_INFO_INVALID);
		} catch (Exception e) {
			log.error("高级钱包银行卡确认绑定失败, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.SENIOR_BANK_CARD_INFO_INVALID);
		}
		return walletId;
	}

	@Override
	public Long unBindCard(String accessToken, Long walletId, Byte source, String cardNo) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包绑定银行卡失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包绑定银行卡失败, 钱包不是高级钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		try {
			yunstUserHandler.unbindBankCard(walletId, source, cardNo);
		} catch (Exception e) {
			log.error("高级钱包银行卡解绑失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return walletId;
	}


}
