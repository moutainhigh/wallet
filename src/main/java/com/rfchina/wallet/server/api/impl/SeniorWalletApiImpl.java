package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.server.api.SeniorWalletApi;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.PersonInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.service.SeniorWalletService;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeniorWalletApiImpl implements SeniorWalletApi {

	@Autowired
	private SeniorWalletService seniorWalletService;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletChannelExtDao walletChannelDao;


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletChannel seniorWalletChannelInfo(String accessToken, Integer channelType,
		Long walletId) {
		WalletChannel walletChannel = null;
		try {
			walletChannel = seniorWalletService.getWalletChannel(channelType, walletId);
		} catch (Exception e) {
			log.error("查询高级钱包渠道信息失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"查询高级钱包渠道信息失败");
		}
		return walletChannel;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletChannel seniorWalletUpgrade(String accessToken,
		@ParamValid(nullable = false) Byte source,
		Integer channelType, Long walletId) {
		return seniorWalletService.createSeniorWallet(channelType, walletId, source);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletChannel seniorWalletSmsCodeVerification(String accessToken, Byte source,
		Integer channelType,
		Long walletId, String mobile, Integer smsCodeType) {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(channelType, walletId);
		if (walletChannel == null) {
			log.error("发送云商通账户绑定手机验证码失败,未创建高级钱包用户, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"发送云商通账户绑定手机验证码失败,未创建高级钱包用户, 查无此钱包");
		}

		if (smsCodeType == EnumDef.EnumVerifyCodeType.YUNST_BIND_PHONE.getValue().intValue()) {
			if (EnumDef.WalletSource.FHT_CORP.getValue().intValue() == source
				&& EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue()
				!= walletChannel.getStatus()) {
				log.error("企业用户资料通道未审核通过，walletId:{}", walletId);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"企业用户资料通道未审核通过");
			}
			try {
				seniorWalletService
					.seniorWalletApplyBindPhone(channelType, walletId, mobile);
			} catch (Exception e) {
				log.error("发送云商通账户绑定手机验证码失败, walletId:{}", walletId);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"发送云商通账户绑定手机验证码失败");
			}
		}
		return walletChannel;
	}

	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String personChangeBindPhone(String accessToken,
		Long walletId, String realName, String idNo, String oldPhone, String jumpUrl) {
		try {
			return seniorWalletService
				.personChangeBindPhone(walletId, realName, idNo, oldPhone, jumpUrl);
		} catch (Exception e) {
			log.error("高级钱包返回个人修改手机页面链接失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包返回个人修改手机页面链接失败");
		}
	}

	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Wallet seniorWalletBindPhone(String accessToken, Byte source, Integer channelType,
		Long walletId, String mobile, String verifyCode) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包绑定手机, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包绑定手机, 查无此钱包");
		}
		try {
			seniorWalletService
				.seniorWalletBindPhone(channelType, walletId, mobile, verifyCode);
		} catch (Exception e) {
			log.error("高级钱包绑定手机失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包绑定手机失败");
		}
		return wallet;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String seniorWalletPersonAuthentication(String accessToken, Byte source,
		Integer channelType, Long walletId,
		String realName, String idNo, @ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
		String verifyCode) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包个人认证失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包个人认证失败, 查无此钱包");
		}
		try {
			seniorWalletService
				.seniorWalletPersonAuth(channelType, walletId, realName, idNo, mobile,
					verifyCode);
			return seniorWalletService.signMemberProtocol(walletId);
		} catch (Exception e) {
			log.error("高级钱包个人认证失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包个人认证失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletChannel seniorWalletCompanyAudit(String accessToken, Byte source,
		Integer channelType,
		Integer auditType,
		Long walletId, YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包企业信息审核失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包企业信息审核失败, 查无此钱包");
		}
		String transformBizUserId = YunstBaseHandler.transferToYunstBizUserFormat(walletId, source);
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(channelType, walletId);
		if (walletChannel == null) {
			log.info("未创建高级钱包用户: bizUserId:{}", transformBizUserId);
			walletChannel = seniorWalletService.createSeniorWallet(channelType, walletId, source);
			if (walletChannel == null) {
				log.error("高级钱包企业信息审核失败, 创建云商通用户失败, bizUserId:{}", transformBizUserId);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"高级钱包企业信息审核失败, 创建云商通用户失败");
			}
			seniorWalletService.upgradeWalletLevel(walletId);
		}
		return seniorWalletService
			.seniorWalletCompanyAudit(channelType, walletId, auditType, companyBasicInfo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String signMemberProtocol(String accessToken, Byte source, Long walletId) {
		try {
			return seniorWalletService.signMemberProtocol(walletId);
		} catch (Exception e) {
			log.error("高级钱包返回会员签约协议页面链接失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包返回会员签约协议页面链接失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String personSetPayPassword(String accessToken, Byte source, Long walletId, String phone,
		String name, String identityNo) {
		try {
			return seniorWalletService
				.setPersonPayPassword(walletId, phone, name, identityNo);
		} catch (Exception e) {
			log.error("高级钱包返回个人设置支付密码页面链接失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包返回个人设置支付密码页面链接失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String signBalanceProtocol(String accessToken, Byte source, Long walletId) {
		try {
			return seniorWalletService.signBalanceProtocol(walletId);
		} catch (Exception e) {
			log.error("高级钱包返回扣款协议链接失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包返回扣款协议链接失败");
		}
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public CompanyInfoResult seniorWalletGetCompanyInfo(String accessToken, Long walletId,
		Byte source) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包获取企业用户信息失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取企业用户信息失败, 查无此钱包");
		}
		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包获取企业用户信息失败, 钱包不是高级钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取企业用户信息失败, 钱包不是高级钱包");
		}

		try {
			return seniorWalletService.seniorWalletGetCompanyInfo(walletId);
		} catch (Exception e) {
			log.error("高级钱包获取企业会员信息失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取企业会员信息失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public PersonInfoResult seniorWalletGetPersonInfo(String accessToken, Long walletId,
		Byte source) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包获取个人用户信息失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取个人用户信息失败, 查无此钱包");
		}
		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包获取个人用户信息失败, 钱包不是高级钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取个人用户信息失败, 钱包不是高级钱包");
		}
		try {
			return seniorWalletService.seniorWalletGetPersonInfo(walletId);
		} catch (Exception e) {
			log.error("高级钱包获取个人会员信息失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取个人会员信息失败");
		}

	}
}
