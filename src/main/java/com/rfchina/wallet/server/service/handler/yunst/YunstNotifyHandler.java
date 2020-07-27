package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.rfchina.biztools.functionnal.LockDone;
import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.misc.Try;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.EnumDefBankCard;
import com.rfchina.wallet.domain.misc.EnumDef.EnumPublicAccount;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletCardStatus;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.VerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletCardType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletProgress;
import com.rfchina.wallet.domain.misc.EnumDef.WalletStatus;
import com.rfchina.wallet.domain.misc.EnumDef.WalletTunnelSetPayPwd;
import com.rfchina.wallet.domain.misc.EnumDef.WalletTunnelSignContract;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyRefType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyType;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletCompany;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletVerifyHis;
import com.rfchina.wallet.server.bank.yunst.response.YunstNotify;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletCardExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCompanyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletVerifyHisExtDao;
import com.rfchina.wallet.server.model.ext.SLWalletMqMessage;
import com.rfchina.wallet.server.msic.EnumWallet.YunstCompanyInfoAuditStatus;
import com.rfchina.wallet.server.msic.RedisConstant;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.SeniorWalletService;
import com.rfchina.wallet.server.service.WalletEventService;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler.YunstMemberType;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YunstNotifyHandler {

	public static final String YUNST_SIGN_SUCCESS = "success";
	@Autowired
	private WalletTunnelExtDao walletTunnelExtDao;
	@Autowired
	private WalletVerifyHisExtDao walletVerifyHisExtDao;
	@Autowired
	private WalletCardExtDao walletCardDao;
	@Autowired
	private YunstBizHandler yunstBizHandler;
	@Autowired
	private SeniorWalletService seniorWalletService;
	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletDao walletDao;
	@Autowired
	private WalletCollectMethodExtDao walletCollectMethodDao;

	@Autowired
	private WalletCompanyExtDao walletCompanyDao;

	@Autowired
	private WalletEventService walletEventService;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Autowired
	private SimpleExclusiveLock lock;

	@PostMq(routingKey = MqConstant.WALLET_SENIOR_COMPANY_AUDIT)
	public SLWalletMqMessage handleVerfiyResult(ChannelNotify channelNotify,
		YunstNotify.CompanyAuditResult rtnVal) {
		log.info("处理企业信息审核结果通知 rtnVal:{}", rtnVal);

		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);

		WalletTunnel walletTunnel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);

		try {

			if (YunstCompanyInfoAuditStatus.SUCCESS.getValue().longValue() == rtnVal.getResult()) {
				log.info("通道会员[{}]审核成功", walletTunnel.getBizUserId());
				CompanyInfoResult companyInfo = (CompanyInfoResult) yunstUserHandler
					.getMemberInfo(walletTunnel.getBizUserId());
				companyInfo.setCheckTime(Optional.ofNullable(rtnVal.getCheckTime())
					.orElse(companyInfo.getCheckTime()));
				handleCompanyAuditSucc(walletTunnel, companyInfo);
				return SLWalletMqMessage.builder()
					.isPass(true)
					.checkTime(rtnVal.getCheckTime())
					.build();

			} else if (YunstCompanyInfoAuditStatus.FAIL.getValue().longValue() == rtnVal
				.getResult()) {
				log.info("通道会员[{}]审核失败", walletTunnel.getBizUserId());
				handleAuditFail(walletTunnel, rtnVal.getFailReason(), rtnVal.getRemark());
				return SLWalletMqMessage.builder()
					.isPass(false)
					.checkTime(rtnVal.getCheckTime())
					.failReason(rtnVal.getFailReason())
					.build();
			}
		} catch (Exception e) {
			log.error("更新审核回调失败", e);
		}
		return null;
	}

	public void handleAuditFail(WalletTunnel walletTunnel, String failReason, String remark) {
		walletTunnel.setStatus(EnumDef.WalletTunnelAuditStatus.AUDIT_FAIL.getValue()
			.byteValue());
		walletTunnel.setFailReason(failReason);
		walletTunnel.setRemark(remark);
		walletTunnelDao.updateByPrimaryKey(walletTunnel);
	}

	public void handleCompanyAuditSucc(WalletTunnel walletTunnel, CompanyInfoResult companyInfo) {

		walletTunnel
			.setStatus(EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
		walletTunnel.setFailReason(null);
		Date checkDate = new Date();
		if (StringUtil.isNotBlank(companyInfo.getCheckTime())) {
			checkDate = DateUtil
				.parse(companyInfo.getCheckTime(), DateUtil.STANDARD_DTAETIME_PATTERN);
		}
		walletTunnel.setCheckTime(checkDate);
		walletTunnel.setSecurityTel(companyInfo.getPhone());
		walletTunnel.setRemark(companyInfo.getRemark());
		walletTunnelExtDao.updateByPrimaryKey(walletTunnel);
		log.info("[通道同步]通道[{}]会员[{}]，更新本地通道成功", walletTunnel.getId(),
			walletTunnel.getBizUserId());
		// 更新验证记录
		byte type = WalletVerifyRefType.COMPANY.getValue().byteValue();
		byte verifyType = WalletVerifyType.COMPANY_VERIFY.getValue().byteValue();
		WalletVerifyHis walletVerifyHis = walletVerifyHisExtDao
			.selectByWalletIdAndRefIdAndType(walletTunnel.getWalletId(), walletTunnel.getId(),
				type);
		if (null == walletVerifyHis
			|| walletVerifyHis.getVerifyTime().compareTo(walletTunnel.getCheckTime()) == -1) {
			WalletVerifyHis verifyHis = WalletVerifyHis.builder()
				.walletId(walletTunnel.getWalletId())
				.refId(walletTunnel.getId())
				.type(type)
				.verifyChannel(VerifyChannel.YUNST.getValue())
				.verifyType(verifyType)
				.verifyTime(walletTunnel.getCheckTime())
				.createTime(walletTunnel.getCheckTime())
				.build();
			walletVerifyHisExtDao.insertSelective(verifyHis);
			log.info("[通道同步]通道[{}]会员[{}]，插入审核日志[{}]", walletTunnel.getId(),
				walletTunnel.getBizUserId(), verifyHis.getId());
		}

		Wallet wallet = walletDao.selectByPrimaryKey(walletTunnel.getWalletId());
		// 更新钱包进度
		wallet.setProgress(Optional.ofNullable(wallet.getProgress().intValue()).orElse(0)
			| WalletProgress.TUNNEL_VALIDATE.getValue().intValue());
		// 钱包激活
		if (WalletStatus.WAIT_AUDIT.getValue().byteValue() == wallet.getStatus()) {

			wallet.setStatus(WalletStatus.ACTIVE.getValue());
		}
		walletDao.updateByPrimaryKeySelective(wallet);

		// 企业钱包更新银行卡账户
		if (YunstMemberType.COMPANY.getValue().longValue() == walletTunnel.getMemberType()) {
			updateCompanyInfo(wallet.getId(), companyInfo);
			updateCompanyCard(wallet.getId(), companyInfo);
		}

		// 发送钱包事件
		walletEventService.sendEventMq(EnumDef.WalletEventType.CHANGE, wallet.getId()
			, wallet.getLevel(), wallet.getStatus());
	}

	public void handleAuditWaiting(WalletTunnel walletTunnel) {
		walletTunnel.setStatus(
			EnumDef.WalletTunnelAuditStatus.WAITING_AUDIT.getValue()
				.byteValue());
		walletTunnel.setFailReason(null);
		walletTunnel.setRemark(null);
		walletTunnelDao.updateByPrimaryKeySelective(walletTunnel);
	}

	public void updateCompanyInfo(Long walletId, CompanyInfoResult companyInfo) {
		WalletCompany walletCompany = walletCompanyDao.selectByWalletId(walletId);
		Optional.ofNullable(walletCompany)
			.ifPresent(company -> {
				company.setCompanyName(companyInfo.getCompanyName());
				company.setLastUpdTime(new Date());
				walletCompanyDao.updateByPrimaryKeySelective(company);
			});

	}

	public void updateCompanyCard(Long walletId, CompanyInfoResult companyInfo) {

		new LockDone(lock)
			.apply(RedisConstant.LOCK_CARD_UPDATE, 30, () -> {
				List<WalletCard> walletCards = walletCardDao.selectPubAccountByWalletId(walletId);
				// 解绑旧卡
				if (walletCards != null && !walletCards.isEmpty()) {
					walletCardDao.updateWalletCard(walletId,
						EnumWalletCardStatus.UNBIND.getValue(),
						EnumWalletCardStatus.BIND.getValue(),
						null, null);
				}

				Date checkDate = new Date();
				if (StringUtil.isNotBlank(companyInfo.getCheckTime())) {
					checkDate = DateUtil
						.parse(companyInfo.getCheckTime(), DateUtil.STANDARD_DTAETIME_PATTERN);
				}
				// 插入新卡
				String accountNo = Try.of((String data) -> RSAUtil.decrypt(data), null)
					.apply(companyInfo.getAccountNo());
				walletCardDao.insertSelective(WalletCard.builder()
					.cardType(WalletCardType.DEPOSIT.getValue())
					.walletId(walletId)
					.bankCode(" ")
					.bankName(companyInfo.getParentBankName())
					.depositBank(companyInfo.getBankName())
					.bankAccount(accountNo)
					.verifyChannel(VerifyChannel.YUNST.getValue())
					.verifyTime(checkDate)
					.isPublic(EnumPublicAccount.YES.getValue().byteValue())
					.isDef(EnumDefBankCard.YES.getValue().byteValue())
					.status(EnumWalletCardStatus.BIND.getValue().byteValue())
					.build());
			});
	}

	public void handleSignContractResult(ChannelNotify channelNotify,
		YunstNotify.SignContractResult rtnVal) {
		log.info("处理会员电子签约通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);
		WalletTunnel walletChannel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);
		Optional.ofNullable(walletChannel)
			.ifPresent(c -> walletDao.addProgress(c.getWalletId(),
				WalletProgress.TUNNEL_SIGN.getValue()));
		walletChannel.setIsSignContact(WalletTunnelSignContract.MEMBER.getValue().byteValue());

		int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("更新电子签约状态失败:bizUserId:{}", bizUserId);
		}
	}

	public void handleSignBalanceContractResult(ChannelNotify channelNotify,
		YunstNotify.BalanceContractResult rtnVal) {
		log.info("处理扣款协议签约通知 rtnVal:{}", rtnVal);
		String reqSn = rtnVal.getProtocolReqSn();
		String signStatus = rtnVal.getSignStatus();
		String protocolNo = rtnVal.getProtocolNo();

		WalletTunnel walletTunnel = walletTunnelExtDao.selectByBanlceProtocolReqSn(reqSn);
		if (Objects.isNull(walletTunnel)) {
			log.error("查无对应walletTunnel, reqSn:{}", reqSn);
			return;
		}
		String bizUserId = walletTunnel.getBizUserId();
		channelNotify.setBizUserId(bizUserId);

		if (YUNST_SIGN_SUCCESS.equalsIgnoreCase(signStatus)) {
			walletTunnel
				.setIsSignContact(WalletTunnelSignContract.BALANCE.getValue().byteValue());
			walletTunnel.setBalanceProtocolNo(protocolNo);
			int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletTunnel);
			if (effectRows != 1) {
				log.error("更新扣款协议状态失败:bizUserId:{}", bizUserId);
			}
		}
	}


	public void handleChangeBindPhoneResult(ChannelNotify channelNotify,
		YunstNotify.UpdatePhoneResult rtnVal) {
		log.info("处理个人会员修改绑定手机通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		String newPhone = rtnVal.getNewPhone();
		channelNotify.setBizUserId(bizUserId);
		WalletTunnel walletChannel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);
		Optional.ofNullable(walletChannel)
			.ifPresent(c -> walletDao.addProgress(c.getWalletId(),
				WalletProgress.TUNNEL_BIND_MOBILE.getValue()));
		walletChannel.setSecurityTel(newPhone);

		int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理个人会员修改绑定手机失败:bizUserId:{},newPhone:{}", bizUserId, newPhone);
		}
	}

	public void handleSetPayPwdResult(ChannelNotify channelNotify,
		YunstNotify.SetPayPwd rtnVal) {
		log.info("处理个人设置支付密码通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);
		WalletTunnel walletChannel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);
		Optional.ofNullable(walletChannel)
			.ifPresent(c -> walletDao.addProgress(c.getWalletId(),
				WalletProgress.TUNNEL_SET_PASSWORD.getValue()));
		walletChannel.setHasPayPassword(WalletTunnelSetPayPwd.YES.getValue().byteValue());

		int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理个人设置支付密码失败:bizUserId:{}", bizUserId);
		}
	}

}
