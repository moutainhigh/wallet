package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.BankCodeDao;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletPersonDao;
import com.rfchina.wallet.domain.misc.EnumDef.EnumDefBankCard;
import com.rfchina.wallet.domain.misc.EnumDef.EnumIdType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumPublicAccount;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletCardStatus;
import com.rfchina.wallet.domain.misc.EnumDef.VerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletCardType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.api.SeniorCardApi;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.response.result.ApplyBindBankCardResp;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.model.ext.PreBindCardVo;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.service.VerifyService;
import com.rfchina.wallet.server.service.WalletService;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeniorCardApiImpl implements SeniorCardApi {

	public static final String PRE_BINDCARD = "wallet:bindcard:";
	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletService walletService;

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Autowired
	private VerifyService verifyService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private BankCodeDao bankCodeDao;

	@Autowired
	private WalletPersonDao walletPersonDao;

	@Autowired
	private WalletCardDao walletCardDao;

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

		verifyService.checkSeniorWallet(walletId);
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByWalletId(walletId, TunnelType.YUNST.getValue());

		try {
			ApplyBindBankCardResp result = yunstUserHandler
				.applyBindBankCard(walletTunnel.getBizUserId(), cardNo, realName, phone,
					EnumIdType.ID_CARD.getValue().longValue(), identityNo, validate, cvv2);

			PreBindCardVo preBindCardVo = PreBindCardVo.builder()
				.walletId(walletId)
				.cardNo(cardNo)
				.phone(phone)
				.transNum(result.getTranceNum())
				.transDate(result.getTransDate())
				.cardType(result.getCardType() != null ? result.getCardType().byteValue()
					: WalletCardType.DEPOSIT.getValue())
				.validate(validate)
				.cvv2(cvv2)
				.bankName(result.getBankName())
				.bankCode(result.getBankCode())
				.build();
			String preBindTicket = UUID.randomUUID().toString();
			redisTemplate.opsForValue()
				.set(PRE_BINDCARD + preBindTicket, preBindCardVo, 15, TimeUnit.MINUTES);
			return preBindTicket;
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
	public void confirmBindCard(String accessToken, Long walletId,
		String verifyCode, String preBindTicket) {

		verifyService.checkSeniorWallet(walletId);

		PreBindCardVo preBindCardVo = (PreBindCardVo) redisTemplate.opsForValue()
			.get(PRE_BINDCARD + preBindTicket);
		if (preBindCardVo == null || walletId.longValue() != preBindCardVo.getWalletId()) {
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_BIND_TIMEOUT);
		}
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByWalletId(walletId, TunnelType.YUNST.getValue());

		try {
			yunstUserHandler.bindBankCard(walletTunnel.getBizUserId(), preBindCardVo.getTransNum(),
				preBindCardVo.getTransDate(), preBindCardVo.getPhone(), preBindCardVo.getValidate(),
				preBindCardVo.getCvv2(), verifyCode);
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

		// 登记绑定卡
		BankCode bankCode = bankCodeDao.selectByBankCode(preBindCardVo.getBankCode());
		WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
		int cardCount = walletCardDao
			.selectCountByWalletId(walletId, EnumWalletCardStatus.BIND.getValue());

		WalletCard walletCard = WalletCard.builder().walletId(walletId)
			.bankCode(bankCode != null ? bankCode.getBankCode() : null)
			.bankName(bankCode != null ? bankCode.getClassName() : null)
			.depositBank(bankCode != null ? bankCode.getBankName() : null)
			.bankAccount(preBindCardVo.getCardNo())
			.depositName(walletPerson.getName())
			.telephone(preBindCardVo.getPhone())
			.verifyChannel(VerifyChannel.YUNST.getValue())
			.verifyTime(new Date())
			.isPublic(EnumPublicAccount.NO.getValue().byteValue())
			.isDef(cardCount == 0 ? EnumDefBankCard.YES.getValue().byteValue()
				: EnumDefBankCard.NO.getValue().byteValue())
			.status(EnumWalletCardStatus.BIND.getValue().byteValue())
			.cardType(preBindCardVo.getCardType())
			.build();
		walletCardDao.insertSelective(walletCard);
	}

	/**
	 * 解绑银行卡
	 */
	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void unBindCard(String accessToken, Long cardId) {
		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByWalletId(walletCard.getWalletId(), TunnelType.YUNST.getValue());
		try {
			yunstUserHandler.unbindBankCard(walletTunnel.getBizUserId(), walletCard.getBankAccount());
			walletCard.setStatus(EnumWalletCardStatus.UNBIND.getValue().byteValue());
			walletCardDao.updateByPrimaryKeySelective(walletCard);
		} catch (Exception e) {
			log.error("高级钱包银行卡解绑失败, cardId: {}", cardId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_UNBIND_ERROR);
		}
	}

}
