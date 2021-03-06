package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.ParamVerify;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletConsumeDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletOrderDao;
import com.rfchina.wallet.domain.mapper.ext.WalletTerminalDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletProgress;
import com.rfchina.wallet.domain.misc.EnumDef.WalletSource;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.api.SeniorWalletApi;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.PersonInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.msic.EnumYunst;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.SeniorWalletService;
import com.rfchina.wallet.server.service.VerifyService;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class SeniorWalletApiImpl implements SeniorWalletApi {

	@Autowired
	private SeniorWalletService seniorWalletService;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletTunnelExtDao walletChannelDao;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private WalletOrderDao walletOrderDao;

	@Autowired
	private VerifyService verifyService;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private WalletConsumeDao walletConsumeDao;

	@Autowired
	private WalletTerminalDao walletTerminalDao;

	@Autowired
	private ConfigService configService;


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletTunnel seniorWalletTunnelInfo(String accessToken, Byte channelType,
		Long walletId) {
		WalletTunnel walletChannel = null;
		try {
			walletChannel = seniorWalletService.getWalletTunnelInfo(channelType, walletId);
		} catch (Exception e) {
			log.error("查询高级钱包渠道信息失败, walletId:" + walletId, e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"查询高级钱包渠道信息失败");
		}
		return walletChannel;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletTunnel seniorWalletCreateTunnel(String accessToken,
		@ParamValid(nullable = false) Byte source,
		Integer channelType, Long walletId) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		Objects.requireNonNull(wallet);
		WalletTunnel walletTunnel = null;
		try {
			walletTunnel = seniorWalletService
				.createTunnel(channelType, walletId, source);
		} catch (Exception e) {
			log.error("用户创建高级钱包失败，walletId = " + walletId, e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"用户创建高级钱包失败");
		}
		return walletTunnel;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletTunnel seniorWalletSmsCodeVerification(String accessToken, Byte source,
		Integer channelType,
		Long walletId, String mobile, Integer smsCodeType) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		Objects.requireNonNull(wallet);
		WalletTunnel walletChannel = walletChannelDao
			.selectByTunnelTypeAndWalletId(channelType.byteValue(), walletId);
		if (walletChannel == null) {
			log.error("发送云商通账户绑定手机验证码失败,未创建高级钱包用户, 查无此钱包, walletId:", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"发送云商通账户绑定手机验证码失败,未创建高级钱包用户, 查无此钱包");
		}
		if (smsCodeType.intValue() == EnumYunst.EnumVerifyCodeType.BIND_PHONE.getValue()
			|| smsCodeType.intValue() == EnumYunst.EnumVerifyCodeType.UNBIND_PHONE.getValue()) {

			if (EnumDef.WalletSource.FHT_CORP.getValue().intValue() == source
				&& EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue()
				!= walletChannel.getStatus()) {
				log.error("企业用户资料通道未审核通过，walletId:{}", walletId);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"企业用户资料通道未审核通过");
			}
			try {
				seniorWalletService.sendVerifyCode(channelType, walletId, mobile, smsCodeType);
			} catch (Exception e) {
				log.error("发送云商通账户绑定手机验证码失败", e);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"发送云商通账户绑定手机验证码失败");
			}
		}
		return walletChannel;
	}

	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String updateSecurityTel(String accessToken, Long walletId, String jumpUrl) {

		verifyService.checkSeniorWallet(walletId);
		WalletTunnel channel = verifyService.checkChannel(walletId, TunnelType.YUNST);
		WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
		return seniorWalletService.updateSecurityTel(walletPerson, channel, jumpUrl);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	public Wallet bindPhone(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Byte tunnelType,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) String mobile,
		@ParamValid(nullable = false) String verifyCode) {

		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		Objects.requireNonNull(wallet);
		// 查渠道用户
		WalletTunnel walletTunnel = walletTunnelDao.selectByTunnelTypeAndWalletId(tunnelType,
			walletId);
		Objects.requireNonNull(walletTunnel);
		try {
			seniorWalletService.seniorWalletBindPhone(walletTunnel, mobile, verifyCode);
		}  catch (Exception e) {
			log.error("高级钱包绑定手机失败", e);
			if (e instanceof  CommonGatewayException){
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					((CommonGatewayException) e).getBankErrMsg());
			}
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包绑定手机失败");
		}
		return wallet;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	public void unBindPhone(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Byte tunnelType,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) String mobile,
		@ParamValid(nullable = false) String verifyCode) {

		// 查渠道用户
		WalletTunnel walletTunnel = walletTunnelDao.selectByTunnelTypeAndWalletId(tunnelType,
			walletId);
		Optional.ofNullable(walletTunnel)
			.orElseThrow(() ->
				new WalletResponseException(EnumWalletResponseCode.TUNNEL_INFO_NOT_EXISTS));
		seniorWalletService.unBindPhone(walletTunnel, mobile, verifyCode);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	public String personAudit(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Byte tunnelType,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) Long userId,
		@ParamValid(nullable = false) String realName,
		@ParamValid(nullable = false) String idNo,
		@ParamValid(nullable = false) String mobile,
		@ParamValid(nullable = false) String verifyCode,
		String jumpUrl) {
		try {
			// 查渠道用户
			WalletTunnel walletTunnel = walletTunnelDao.selectByTunnelTypeAndWalletId(tunnelType,
				walletId);
			Objects.requireNonNull(walletTunnel);
			// 如果未审核通过
			if (EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue()
				!= walletTunnel.getStatus()) {
				// 通道未绑手机
				if (StringUtils.isEmpty(walletTunnel.getSecurityTel())) {
					seniorWalletService.seniorWalletBindPhone(walletTunnel, mobile, verifyCode);
					walletDao.addProgress(walletTunnel.getWalletId(),
						WalletProgress.TUNNEL_BIND_MOBILE.getValue());
				}
				// 用户实名
				seniorWalletService.personAudit(userId, walletTunnel, realName, idNo);
				// 更新钱包
				seniorWalletService.updatePersionAuditInfo(walletTunnel, realName, idNo, mobile);
			}

			return seniorWalletService.signMemberProtocol(walletId, jumpUrl);
		} catch (Exception e) {
			log.error("高级钱包个人认证失败", e);
			if (e instanceof  CommonGatewayException){
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					((CommonGatewayException) e).getBankErrMsg());
			}
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包个人认证失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	@Override
	public void personIdBind(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) Long userId) {

		WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
		if (walletPerson == null || walletPerson.getIdNo() == null) {
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_NOT_AUTH);
		}
		seniorWalletService.syncRealInfo(userId, walletPerson.getName(), walletPerson.getIdNo());
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletTunnel setCompanyInfo(String accessToken, Integer channelType,
		Integer auditType, Long walletId,
		YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) {

		WalletTunnel walletTunnel = walletChannelDao
			.selectByTunnelTypeAndWalletId(channelType.byteValue(), walletId);
		if (Objects.isNull(walletTunnel)) {
			log.info("未创建高级钱包用户: walletId:{}", walletId);
			try {
				walletTunnel = seniorWalletService
					.createTunnel(channelType, walletId, WalletSource.FHT_CORP.getValue());
			} catch (Exception e) {
				log.error("高级钱包企业创建高级钱包失败", e);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"高级钱包企业创建高级钱包失败");
			}
			Objects.requireNonNull(walletTunnel);
			seniorWalletService.upgradeWalletLevel(walletId);
		}
		return seniorWalletService
			.setCompanyInfo(channelType, walletId, auditType, companyBasicInfo);
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public YunstMemberInfoResult.CompanyInfoResult manualCompanyAudit(
		String accessToken, Long walletId) {
		try {
			return seniorWalletService.resetCompanyAudit(walletId);
		} catch (Exception e) {
			log.error("高级钱包线下确认更新企业信息失败", e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包线下确认更新企业信息失败");
		}

	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String signMemberProtocol(String accessToken, Long walletId, String jumpUrl) {
		try {
			return seniorWalletService.signMemberProtocol(walletId, jumpUrl);
		} catch (Exception e) {
			log.error("高级钱包返回会员签约协议页面链接失败", e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包返回会员签约协议页面链接失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String personSetPayPassword(String accessToken, Long walletId, String jumpUrl) {
		try {
			return seniorWalletService
				.setPersonPayPassword(walletId, jumpUrl);
		} catch (Exception e) {
			log.error("高级钱包返回个人设置支付密码页面链接失败", e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包返回个人设置支付密码页面链接失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String updatePayPwd(String accessToken, Long walletId, String jumpUrl) {
		verifyService.checkSeniorWallet(walletId);
		return seniorWalletService.updateTunnelPayPwd(walletId, jumpUrl);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String resetPayPwd(String accessToken, Long walletId, String jumpUrl) {
		verifyService.checkSeniorWallet(walletId);
		return seniorWalletService.resetTunnelPayPwd(walletId, jumpUrl);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String signBalanceProtocol(String accessToken, Long walletId, String jumpUrl) {
		try {
			return seniorWalletService.signBalanceProtocol(walletId, jumpUrl);
		} catch (Exception e) {
			log.error("高级钱包返回扣款协议链接失败", e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包返回扣款协议链接失败");
		}
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public CompanyInfoResult getCompanyInfo(String accessToken, Long walletId) {

		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		Objects.requireNonNull(wallet);
		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包获取企业用户信息失败, 钱包不是高级钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取企业用户信息失败, 钱包不是高级钱包");
		}

		try {
			return seniorWalletService.getCompanyInfo(walletId);
		} catch (Exception e) {
			log.error("高级钱包获取企业会员信息失败", e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取企业会员信息失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public PersonInfoResult getPersonInfo(String accessToken, Long walletId) {

		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		Objects.requireNonNull(wallet);

		if (wallet.getLevel() != EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue()) {
			log.error("高级钱包获取个人用户信息失败, 钱包不是高级钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取个人用户信息失败, 钱包不是高级钱包");
		}

		try {
			return seniorWalletService.seniorWalletGetPersonInfo(walletId);
		} catch (Exception e) {
			log.error("高级钱包获取个人会员信息失败", e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包获取个人会员信息失败");
		}

	}

}
