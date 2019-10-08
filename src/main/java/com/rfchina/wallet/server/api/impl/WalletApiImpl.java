package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.mq.PostMq;
import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.EnumParamValid;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletUserDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.*;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.domain.model.ext.WalletCardExt;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.msic.EnumWallet;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.JuniorWalletService;
import com.rfchina.wallet.server.service.UserService;
import com.rfchina.wallet.server.service.WalletService;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class WalletApiImpl implements WalletApi {

	@Autowired
	private WalletService walletService;

	@Autowired
	private JuniorWalletService juniorWalletService;

	@Autowired
	private SimpleExclusiveLock lock;

	@Autowired
	private WalletUserDao walletUserDao;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletApplyExtDao walletApplyExtDao;

	@Autowired
	private WalletChannelExtDao walletChannelDao;

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public List<PayStatusResp> queryWalletApply(String accessToken, @ParamValid(nullable = true) String bizNo,
			@ParamValid(nullable = true) String batchNo) {
		return walletService.queryWalletApply(bizNo, batchNo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public void redoWalletApply(String accessToken, @ParamValid(nullable = false) Long walletLogId) {

		String lockName = "redoWalletApply：" + walletLogId;
		boolean succ = lock.acquireLock(lockName, 60, 0, 1);
		if (succ) {
			try {
				walletService.redo(walletLogId);
			} finally {
				lock.unLock(lockName);
			}
		} else {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_REDO_DUPLICATE, walletLogId.toString());
		}
	}

	@Log
	@Override
	public void quartzUpdate() {

		String lockName = "quartzUpdate";
		boolean succ = lock.acquireLock(lockName, 900, 0, 1);
		if (succ) {
			try {
				walletService.quartzUpdate(configService.getBatchUpdateSize());
			} finally {
				lock.unLock(lockName);
			}
		} else {
			log.warn("获取分布式锁失败， 跳过执行的quartzUpdatePayStatus任务");
		}
	}

	@Log
	@Override
	public void quartzPay() {

		String lockName = "quartzPay";
		boolean succ = lock.acquireLock(lockName, 1800, 0, 1);
		if (succ) {
			try {
				walletService.quartzPay(configService.getBatchPaySize());
			} finally {
				lock.unLock(lockName);
			}
		} else {
			log.warn("获取分布式锁失败， 跳过执行的quartzPay任务");
		}
	}

	@Override
	public void quartzNotify() {
		String lockName = "quartzNotify";
		boolean succ = lock.acquireLock(lockName, 600, 0, 1);
		if (succ) {
			try {
				List<WalletApply> walletApplys = walletApplyExtDao.selectByStatusNotNotified(
						WalletApplyStatus.WAIT_DEAL.getValue(), 200);
				walletService.notifyDeveloper(walletApplys);

				walletApplys = walletApplyExtDao.selectByStatusNotNotified(WalletApplyStatus.REDO.getValue(), 200);
				walletService.notifyBusiness(walletApplys);
			} finally {
				lock.unLock(lockName);
			}
		} else {
			log.warn("获取分布式锁失败， 跳过执行的{}任务", lockName);
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfo(String accessToken, @ParamValid(nullable = false) Long walletId) {
		return walletService.queryWalletInfo(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfoByUserId(String accessToken, Long userId) {
		return walletService.queryWalletInfoByUserId(userId);
	}

	@Log
	@PostMq(routingKey = MqConstant.WALLET_CREATE)
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public Wallet createWallet(String accessToken, @ParamValid(nullable = false) Byte type,
			@ParamValid(nullable = false) String title, @ParamValid(nullable = false) Byte source) {
		return walletService.createWallet(type, title, source);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public Pagination<WalletApply> walletApplyList(String accessToken, Long walletId, Date startTime, Date endTime,
			int limit, long offset, Boolean stat) {
		return walletService.walletApplyList(walletId, startTime, endTime, limit, offset, stat);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public List<WalletCard> bankCardList(String accessToken, Long walletId) {
		return walletService.bankCardList(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	@PostMq(routingKey = MqConstant.WALLET_BANKCARD_BIND)
	public WalletCardExt bindBankCard(String accessToken, Long walletId, String bankCode, String bankAccount,
			String depositName, Integer isDef, String telephone) {
		return walletService.bindBankCard(walletId, bankCode, bankAccount, depositName, isDef, telephone);
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
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public void activeWalletPerson(Long walletId, String name, Byte idType, String idNo, Long auditType) {
		walletService.activeWalletPerson(walletId, name, idType, idNo, auditType);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public void activeWalletCompany(Long walletId, String companyName, Long auditType) {
		walletService.activeWalletCompany(walletId, companyName, auditType);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public ResponseValue sendVerifyCode(String accessToken, Long userId,
			@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
			@EnumParamValid(valuableEnumClass = com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.class)
					Integer type, @ParamValid(nullable = false) String verifyToken, String redirectUrl, String ip) {

		com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType enumSendSmsType = EnumUtil.parse(
				com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.class, type);

		if (enumSendSmsType == com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.VERIFY_BINDING_ACCOUNT) {
			//检查已绑定钱包的手机号
			WalletUser walletUser = Optional.ofNullable(walletUserDao.selectByMobile(mobile))
					.orElseThrow(() -> new WalletResponseException(
							WalletResponseCode.EnumWalletResponseCode.WALLET_NOT_BINDING));

			if (walletUser.getStatus() == com.rfchina.wallet.domain.misc.EnumDef.EnumWalletStatus.DISABLE.getValue()
					.byteValue()) {
				throw new WalletResponseException(WalletResponseCode.EnumWalletResponseCode.WALLET_DISABLE);
			}
		}
		//直接发送短信
		return userService.sendSmsVerifyCode(com.rfchina.wallet.domain.misc.EnumDef.EnumVerifyCodeType.LOGIN, mobile,
				verifyToken, redirectUrl, enumSendSmsType.msg(), ip);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
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
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public WalletChannel upgradeSeniorWallet(String accessToken, @ParamValid(nullable = false) Byte source,
			Integer channelType, Long walletId) {
		return walletService.createSeniorWallet(channelType, walletId, source);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public WalletChannel seniorWalletSmsCodeVerification(String accessToken, Byte source, Integer channelType,
			Long walletId, String mobile, Integer smsCodeType) {
		String transformBizUserId = YunstBaseHandler.transferToYunstBizUserFormat(walletId, source);
		WalletChannel walletChannel = walletChannelDao.selectByChannelTypeAndBizUserId(channelType,
				transformBizUserId);
		if (walletChannel == null) {
			log.info("未创建高级钱包用户: bizUserId:{}", transformBizUserId);
			walletChannel = walletService.createSeniorWallet(channelType, walletId, source);
			walletService.upgradeWalletLevel(walletId);
		}
		if (smsCodeType == EnumDef.EnumVerifyCodeType.YUNST_BIND_PHONE.getValue().intValue()) {
			if (EnumDef.WalletSource.FHT_CORP.getValue().intValue() == source
					&& EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue()
					!= walletChannel.getStatus()) {
				log.error("企业用户资料通道未审核通过");
				return walletChannel;
			}
			String rtn = walletService.seniorWalletApplyBindPhone(channelType, walletId, source, mobile);
			if (!rtn.equals(walletChannel.getBizUserId())) {
				log.error("发送云商通账户绑定手机验证码失败, expect: {},actucal:{}", transformBizUserId, rtn);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
			}
		}
		return walletChannel;
	}

	@Override
	public String seniorWalletPersonChangeBindPhone(String accessToken, Byte source, Integer channelType,
			Long walletId,
			String realName, String idNo, String oldPhone) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包企业信息审核失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return walletService.seniorWalletPersonChangeBindPhone(channelType, walletId, source, realName, idNo,
				oldPhone);
	}

	@Override
	public Long seniorWalletBindPhone(String accessToken, Byte source, Integer channelType, Long walletId,
			String mobile, String verifyCode) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包绑定手机, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		if (!walletService.seniorWalletBindPhone(channelType, walletId, source, mobile, verifyCode)) {
			log.error("高级钱包绑定手机失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return wallet.getId();
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public String seniorWalletPersonAuthentication(String accessToken, Byte source, Integer channelType, Long walletId,
			String realName, String idNo, @ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
			String verifyCode) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包个人认证失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		if (!walletService.seniorWalletBindPhone(channelType, walletId, source, mobile, verifyCode)) {
			log.error("高级钱包个人失败, 查钱包绑定手机失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return walletService.seniorWalletPersonAuth(channelType, walletId, source, realName, idNo, mobile);
	}

	@Override
	public Integer seniorWalletCompanyAudit(String accessToken, Byte source, Integer channelType, Integer auditType,
			Long walletId, YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("高级钱包企业信息审核失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		String transformBizUserId = YunstBaseHandler.transferToYunstBizUserFormat(walletId, source);
		WalletChannel walletChannel = walletChannelDao.selectByChannelTypeAndBizUserId(channelType,
				transformBizUserId);
		if (walletChannel == null) {
			log.info("未创建高级钱包用户: bizUserId:{}", transformBizUserId);
			walletChannel = walletService.createSeniorWallet(channelType, walletId, source);
			if (walletChannel == null) {
				log.error("高级钱包企业信息审核失败, 创建云商通用户失败, bizUserId:{}", transformBizUserId);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
			}
			walletService.upgradeWalletLevel(walletId);
		}
		return walletService.seniorWalletCompanyAudit(channelType, walletId, source, auditType, companyBasicInfo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public String signMemberProtocol(String accessToken, Byte source, Long walletId) {
		return walletService.signMemberProtocol(source, walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public String signBalanceProtocol(String accessToken, Byte source, Long walletId) {
		return walletService.signBalanceProtocol(source, walletId);
	}

}
