package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.BankCodeDao;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletPersonDao;
import com.rfchina.wallet.domain.misc.EnumDef.EnumDefBankCard;
import com.rfchina.wallet.domain.misc.EnumDef.EnumIdType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumPublicAccount;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletCardStatus;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.VerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletCardType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.response.result.ApplyBindBankCardResp;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.model.ext.PreBindCardVo;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SeniorCardService {

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
	public String preBindBandCard(Long walletId,
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
				.set(PRE_BINDCARD + preBindTicket, preBindCardVo, 30, TimeUnit.MINUTES);
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void confirmBindCard(Long walletId, String verifyCode, String preBindTicket) {

		// 检查钱包
		verifyService.checkSeniorWallet(walletId);
		// 检查令牌
		PreBindCardVo preBindCardVo = (PreBindCardVo) redisTemplate.opsForValue()
			.get(PRE_BINDCARD + preBindTicket);
		Optional.ofNullable(preBindCardVo)
			.filter(vo -> vo.getWalletId() == walletId.longValue())
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.BANK_CARD_BIND_TIMEOUT));
		// 确定绑卡
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByWalletId(walletId, TunnelType.YUNST.getValue());
		try {
			yunstUserHandler.bindBankCard(walletTunnel.getBizUserId(), preBindCardVo.getTransNum(),
				preBindCardVo.getTransDate(), preBindCardVo.getPhone(), preBindCardVo.getValidate(),
				preBindCardVo.getCvv2(), verifyCode);
		} catch (CommonGatewayException e) {
			String errMsg = e.getBankErrMsg();
			if (errMsg.indexOf("参数validate为空") > -1) {
				throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_CREDIT_INVALID);
			}
			log.error("高级钱包-确认绑定银行卡, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_INFO_INVALID);
		} catch (Exception e) {
			log.error("高级钱包-确认绑定银行卡, walletId: {}", walletId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_INFO_INVALID);
		}
		// 登记绑定卡
		WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
		int cardCount = walletCardDao
			.selectCountByWalletId(walletId, EnumWalletCardStatus.BIND.getValue());
		//兼容升级银行卡做通联验证,将旧的卡解绑
		WalletCard exsitsCard = walletCardDao.selectByCardNo(walletId, preBindCardVo.getCardNo());
		if (Objects.nonNull(exsitsCard)) {
			exsitsCard.setStatus(EnumWalletCardStatus.UNBIND.getValue().byteValue());
			walletCardDao.updateByPrimaryKeySelective(exsitsCard);
		}

		WalletCard walletCard = WalletCard.builder().walletId(walletId)
			.bankCode(preBindCardVo.getBankCode())
			.bankName(preBindCardVo.getBankName())
			.depositBank(null)
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
	public void unBindCard(Long cardId) {
		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		verifyService.checkCard(walletCard);

		WalletTunnel walletTunnel = walletTunnelDao
			.selectByWalletId(walletCard.getWalletId(), TunnelType.YUNST.getValue());
		try {
			yunstUserHandler
				.unbindBankCard(walletTunnel.getBizUserId(), walletCard.getBankAccount());
			walletCard.setStatus(EnumWalletCardStatus.UNBIND.getValue().byteValue());
			walletCard.setLastUpdTime(new Date());
			walletCardDao.updateByPrimaryKeySelective(walletCard);
		} catch (Exception e) {
			log.error("高级钱包银行卡解绑失败, cardId: {}", cardId);
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_UNBIND_ERROR);
		}
	}

}
