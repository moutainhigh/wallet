package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.mq.PostMq;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.EnumParamValid;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletUserDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletUser;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.domain.model.ext.WalletCardExt;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletCardVo;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.JuniorPayService;
import com.rfchina.wallet.server.service.UserService;
import com.rfchina.wallet.server.service.WalletService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WalletApiImpl implements WalletApi {

	@Autowired
	private WalletService walletService;

	@Autowired
	private JuniorPayService juniorPayService;

	@Autowired
	private WalletUserDao walletUserDao;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletCardDao walletCardDao;


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public List<PayStatusResp> queryWalletApply(String accessToken,
		@ParamValid(nullable = true) String bizNo,
		@ParamValid(nullable = true) String batchNo) {
		return walletService.queryWalletApply(bizNo, batchNo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfo(String accessToken,
		@ParamValid(nullable = false) Long walletId) {
		return walletService.queryWalletInfo(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfoByUserId(String accessToken, Long userId) {
		return walletService.queryWalletInfoByUserId(userId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<Wallet> walletList(String accessToken, String title, Byte type,
		Byte walletLevel,
		Byte status, Integer limit, Integer offset, Boolean stat) {
		return walletService.walletList(title, type, walletLevel, status, limit, offset, stat);
	}

	@Log
	@PostMq(routingKey = MqConstant.WALLET_CREATE)
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Wallet createWallet(String accessToken,
		@ParamValid(nullable = false) Byte type,
		@ParamValid(nullable = false) String title,
		@ParamValid(nullable = false) Byte source) {
		try {
			return walletService.createWallet(type, title, source);
		} catch (Exception e) {
			log.error("创建钱包失败",e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"创建钱包失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<WalletOrder> walletApplyList(String accessToken, Long walletId,
		Date startTime,
		Date endTime, int limit, int offset, Boolean stat) {
		return walletService.walletOrderList(walletId, startTime, endTime,
			limit, offset, stat);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public List<WalletCard> bankCardList(String accessToken, Long walletId) {
		return walletService.bankCardList(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@PostMq(routingKey = MqConstant.WALLET_BANKCARD_BIND)
	public WalletCardExt bindBankCard(String accessToken, Long walletId, String bankCode,
		String bankAccount, String depositName, Integer isDef,
		String telephone) {
		return walletService.bindBankCard(walletId, bankCode, bankAccount, depositName,
			isDef, telephone);
	}

	@Override
	public List<BankClass> bankClassList() {
		return walletService.bankClassList();
	}

	@Override
	public List<BankArea> bankAreaList(@ParamValid(nullable = false) String classCode) {
		return walletService.bankAreaList(classCode);
	}

	@Override
	public List<Bank> bankList(@ParamValid(nullable = false) String classCode,
		@ParamValid(nullable = false) String areaCode) {
		return walletService.bankList(classCode, areaCode);
	}

	@Override
	public BankCode bank(String bankCode) {
		return walletService.bank(bankCode);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void activeWalletPerson(Long walletId, String name, Byte idType, String idNo,
		Byte status, Long auditType) {
		walletService.activeWalletPerson(walletId, name, idType, idNo, status, auditType);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void activeWalletCompany(Long walletId, String companyName, Byte status,
		Long auditType, String phone, String email) {
		walletService.activeWalletCompany(walletId, companyName, status, auditType, phone, email);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public ResponseValue sendVerifyCode(String accessToken,
		Long userId, @ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
		@EnumParamValid(valuableEnumClass = com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.class) Integer type,
		@ParamValid(nullable = false) String verifyToken, String redirectUrl, String ip) {

		com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType enumSendSmsType = EnumUtil
			.parse(com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.class, type);

		if (enumSendSmsType
			== com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.VERIFY_BINDING_ACCOUNT) {
			//检查已绑定钱包的手机号
			WalletUser walletUser = Optional.ofNullable(walletUserDao.selectByMobile(mobile))
				.orElseThrow(() -> new WalletResponseException(
					WalletResponseCode.EnumWalletResponseCode.WALLET_NOT_BINDING));

			if (walletUser.getStatus()
				== com.rfchina.wallet.domain.misc.EnumDef.EnumWalletStatus.DISABLE.getValue()
				.byteValue()) {
				throw new WalletResponseException(
					WalletResponseCode.EnumWalletResponseCode.WALLET_DISABLE);
			}
		}
		//直接发送短信
		return userService
			.sendSmsVerifyCode(com.rfchina.wallet.domain.misc.EnumDef.EnumVerifyCodeType.LOGIN,
				mobile, verifyToken, redirectUrl, enumSendSmsType.msg(), ip);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletUser loginWithVerifyCode(String accessToken,
		@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
		@ParamValid(nullable = false, min = 1, max = 6) String verifyCode,
		@EnumParamValid(valuableEnumClass = EnumDef.EnumVerifyType.class) Integer type, String ip) {
		//通过短信验证码登录
		userService.userLoginWithVerifyCode(mobile, verifyCode, ip);

		//检查帐号是否已开通钱包
		WalletUser walletUser = walletUserDao.selectByMobile(mobile);
		if (null == walletUser) {
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
		}

		return walletUser;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER, EnumTokenType.APP})
	@SignVerify
	@Override
	public List<WalletCardVo> queryWalletCard(String accessToken, Long walletId) {
		List<WalletCard> walletCards = walletCardDao.selectByWalletId(walletId);
		return walletCards.stream()
			.map(card -> BeanUtil.newInstance(card, WalletCardVo.class))
			.collect(Collectors.toList());
	}


}
