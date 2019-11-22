package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.api.SeniorWalletApi;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.PersonInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.WalletSource;
import com.rfchina.wallet.server.service.SeniorWalletService;
import com.rfchina.wallet.server.service.VerifyService;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
	private WalletTunnelExtDao walletChannelDao;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private WalletOrderExtDao walletOrderExtDao;

	@Autowired
	private VerifyService verifyService;


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
	public WalletTunnel seniorWalletUpgrade(String accessToken,
		@ParamValid(nullable = false) Byte source,
		Integer channelType, Long walletId) {
		WalletTunnel walletTunnel = null;
		try {
			walletTunnel = seniorWalletService
				.createSeniorWallet(channelType, walletId, source);
		} catch (Exception e) {
			log.error("用户创建高级钱包失败，walletId:{}", walletId);
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
		WalletTunnel walletChannel = walletChannelDao
			.selectByTunnelTypeAndWalletId(channelType.byteValue(), walletId);
		if (walletChannel == null) {
			log.error("发送云商通账户绑定手机验证码失败,未创建高级钱包用户, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"发送云商通账户绑定手机验证码失败,未创建高级钱包用户, 查无此钱包");
		}

		if (smsCodeType == EnumDef.EnumVerifyCodeType.YUNST_BIND_PHONE.getValue().intValue()) {
			if (EnumDef.WalletSource.FHT_CORP.getValue().intValue() == source
				&& EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue()
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
	public String updateSecurityTel(String accessToken, Long walletId, String jumpUrl) {

		verifyService.checkSeniorWallet(walletId);
		WalletTunnel channel = verifyService.checkChannel(walletId, TunnelType.YUNST);
		WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
		return seniorWalletService.updateSecurityTel(walletPerson, channel, jumpUrl);
	}

	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Wallet seniorWalletBindPhone(String accessToken, Integer channelType,
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
	public String seniorWalletPersonAuthentication(String accessToken, Integer channelType,
		Long walletId, String realName, String idNo,
		@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
		String verifyCode, String jumpUrl) {
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
			return seniorWalletService.signMemberProtocol(walletId, jumpUrl);
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
	public WalletTunnel seniorWalletCompanyAudit(String accessToken, Integer channelType,
		Integer auditType, Long walletId,
		YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包企业信息审核失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"高级钱包企业信息审核失败, 查无此钱包");
		}
		WalletTunnel walletChannel = walletChannelDao
			.selectByTunnelTypeAndWalletId(channelType.byteValue(), walletId);
		if (walletChannel == null) {
			log.info("未创建高级钱包用户: walletId:{}", walletId);
			try {
				walletChannel = seniorWalletService
					.createSeniorWallet(channelType, walletId, WalletSource.FHT_CORP.getValue());
			} catch (Exception e) {
				log.error("高级钱包企业创建高级钱包失败, walletId:{}", walletId);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"高级钱包企业创建高级钱包失败");
			}
			if (walletChannel == null) {
				log.error("高级钱包企业信息审核失败, 创建云商通用户失败, walletId:{}", walletId);
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
	public String signMemberProtocol(String accessToken, Long walletId, String jumpUrl) {
		try {
			return seniorWalletService.signMemberProtocol(walletId, jumpUrl);
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
	public String personSetPayPassword(String accessToken, Long walletId, String jumpUrl) {
		try {
			return seniorWalletService
				.setPersonPayPassword(walletId, jumpUrl);
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
	public String updatePayPwd(String accessToken, Long walletId, String jumpUrl) {
		verifyService.checkSeniorWallet(walletId);
		return seniorWalletService.updateTunnelPayPwd(walletId, jumpUrl);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public String signBalanceProtocol(String accessToken, Long walletId, String jumpUrl) {
		try {
			return seniorWalletService.signBalanceProtocol(walletId, jumpUrl);
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
	public CompanyInfoResult seniorWalletGetCompanyInfo(String accessToken, Long walletId) {
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
	public PersonInfoResult seniorWalletGetPersonInfo(String accessToken, Long walletId) {
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

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<WalletOrder> queryWalletOrderDetail(String accessToken, Long walletId,
		Date fromTime, Date toTime, Integer tradeType, Integer status, int limit, int offset,
		Boolean stat) {
		List<WalletOrder> walletOrderList = walletOrderExtDao
			.selectByCondition(walletId, fromTime, toTime, tradeType, status, limit, offset);
		long total = Objects.nonNull(stat) && stat ? walletOrderExtDao
			.selectCountByCondition(walletId, fromTime, toTime, tradeType, status) : 0;
		return new Pagination.PaginationBuilder<WalletOrder>().data(walletOrderList)
			.total(total)
			.offset(offset)
			.pageLimit(limit)
			.build();
	}
}
