package com.rfchina.wallet.server.service;

import com.rfchina.p.user.api.SyncRealInfoRequest;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.platform.sdk2.ApiClient;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.DirtyType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumDefBankCard;
import com.rfchina.wallet.domain.misc.EnumDef.EnumIdType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumPublicAccount;
import com.rfchina.wallet.domain.misc.EnumDef.EnumVerifyCodeType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletAuditType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletCardStatus;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.VerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletCardType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletProgress;
import com.rfchina.wallet.domain.misc.EnumDef.WalletSource;
import com.rfchina.wallet.domain.misc.EnumDef.WalletStatus;
import com.rfchina.wallet.domain.misc.EnumDef.WalletTunnelAuditStatus;
import com.rfchina.wallet.domain.misc.EnumDef.WalletTunnelSignContract;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyRefType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletTunnel.WalletTunnelBuilder;
import com.rfchina.wallet.domain.model.WalletVerifyHis;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.PersonInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstPersonSetRealNameResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstQueryBalanceResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSetCompanyInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletCardExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletVerifyHisExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.YunstCompanyInfoAuditStatus;
import com.rfchina.wallet.server.msic.EnumYunst.EnumYunstResponse;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler.YunstMemberType;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SeniorWalletService {

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private WalletVerifyHisExtDao walletVerifyHisExtDao;

	@Autowired
	private WalletCardExtDao walletCardDao;

	@Autowired
	private VerifyService verifyService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private AppService appService;

	@Autowired
	@Qualifier("apiTemplate")
	private ApiClient apiTemplate;

	@Autowired
	private WalletEventService walletEventService;


	/**
	 * 升级高级钱包
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletTunnel createTunnel(Integer channelType, Long walletId, Byte source)
		throws Exception {
		WalletTunnel walletChannel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(channelType.byteValue(), walletId);
		if (walletChannel != null) {
			return walletChannel;
		}
		WalletTunnelBuilder builder = WalletTunnel.builder()
			.tunnelType(channelType.byteValue())
			.status(WalletTunnelAuditStatus.NOT_COMMIT.getValue().byteValue())
			.walletId(walletId)
			.createTime(new Date());
		if (channelType == TunnelType.YUNST.getValue().intValue()) {
			Tuple<YunstCreateMemberResult, YunstMemberType> member = null;
			try {
				member = yunstUserHandler.createMember(walletId, source);
				builder.bizUserId(member.left.getBizUserId())
					.tunnelUserId(member.left.getUserId())
					.memberType(member.right.getValue().byteValue());
			} catch (CommonGatewayException e) {
				log.error("", e);
				String errCode = e.getBankErrCode();
				if (!EnumYunstResponse.ALREADY_EXISTS_MEMEBER.getValue().equals(errCode)) {
					log.error("开通高级钱包失败, channelType: {}, walletId: {}, source:{}", channelType,
						walletId, source);
					throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
						"开通高级钱包失败");
				} else {
					YunstMemberType memberType = YunstMemberType.PERSON;
					if (source == 1) {
						memberType = YunstMemberType.COMPANY;
					}
					String bizUserId = yunstUserHandler
						.transferToYunstBizUserFormat(walletId, source, configService.getEnv());
					builder.bizUserId(bizUserId)
						.tunnelUserId(null)
						.memberType(memberType.getValue().byteValue());
				}
			}


		}
		walletChannel = builder.build();
		int effectRows = walletTunnelDao.insertSelective(walletChannel);
		if (effectRows != 1) {
			log.error("开通高级钱包失败, channelType: {}, walletId: {}, source:{}", channelType,
				walletId,
				source);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"开通高级钱包失败-插入渠道信息");
		}

		if (source != WalletSource.USER.getValue().byteValue()) {
			Wallet wallet = walletDao.selectByPrimaryKey(walletId);
			wallet.setLevel(EnumDef.EnumWalletLevel.SENIOR.getValue());
			walletDao.updateByPrimaryKeySelective(wallet);
		}

		return walletChannel;
	}

	/**
	 * 更新钱包等级
	 */
	public Wallet upgradeWalletLevel(Long walletId) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		Objects.requireNonNull(wallet);
		if (wallet.getLevel().byteValue() == EnumDef.EnumWalletLevel.SENIOR.getValue()) {
			return wallet;
		}
		wallet.setLevel(EnumDef.EnumWalletLevel.SENIOR.getValue());
		int effctRows = walletDao.updateByPrimaryKeySelective(wallet);
		if (effctRows != 1) {
			log.error("更新钱包等级失败, walletId: {}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"更新钱包等级失败");
		}
		return wallet;
	}

	/**
	 * 高级钱包绑定手机
	 */
	public void seniorWalletBindPhone(WalletTunnel walletTunnel, String mobile,
		String verifyCode) throws Exception {
		yunstUserHandler.bindPhone(walletTunnel.getBizUserId(), mobile, verifyCode);
		walletTunnel.setSecurityTel(mobile);
		walletTunnelDao.updateByPrimaryKeySelective(walletTunnel);
	}


	/**
	 * 高级钱包-解绑手机
	 */
	public void unBindPhone(WalletTunnel walletTunnel, String mobile,
		String verifyCode) {

		yunstUserHandler.unBindPhone(walletTunnel.getBizUserId(), mobile, verifyCode);
		log.info("[高级钱包][解绑手机] bizUserId={},mobile={}", walletTunnel.getBizUserId(), mobile);

		walletTunnel.setSecurityTel(null);
		walletTunnelDao.updateByPrimaryKey(walletTunnel);
	}

	/**
	 * 高级钱包个人修改绑定手机
	 */
	public String updateSecurityTel(WalletPerson person, WalletTunnel channel, String jumpUrl) {

		jumpUrl = configService.getYunstJumpUrlPrefix() + jumpUrl;
		String signedParams = yunstUserHandler.updateSecurityTel(channel.getBizUserId(),
			person.getName(), channel.getSecurityTel(), person.getIdNo(), jumpUrl);
		return configService.getYunstPersonChangeBindPhoneUrl() + "?" + signedParams;
	}


	/**
	 * 同步认证信息
	 */
	public void syncRealInfo(Long uid, String name, String idNo) {
		try {
			SyncRealInfoRequest req = SyncRealInfoRequest.builder()
				.accessToken(appService.getAccessToken())
				.idNo(idNo)
				.name(name)
				.type(1)
				.uid(uid)
				.build();
			apiTemplate.execute(req);
			log.info("同步用户实名 {}, {}", uid, name);
		} catch (Exception e) {
			log.error("同步用户实名信息失败 " + uid + name, e);
		}
	}

	/**
	 * 个人实名认证
	 */
	public void personAudit(Long userId, WalletTunnel walletTunnel, String name, String idNo)
		throws Exception {

		// 渠道实名
		try {
			YunstPersonSetRealNameResult result = yunstUserHandler.personCertification(
				walletTunnel.getBizUserId(), name, EnumIdType.ID_CARD.getValue().longValue(),
				idNo);
			syncRealInfo(userId, result.getName(), idNo);
		} catch (CommonGatewayException e) {
			if (!EnumYunstResponse.ALREADY_REALNAME_AUTH.getValue().equals(e.getBankErrCode())) {
				throw e;
			}
		}
	}

	/**
	 * 更新个人验证信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatePersionAuditInfo(WalletTunnel walletTunnel, String name, String idNo,
		String mobile) {
		// 更新审核成功
		walletTunnel.setStatus(
			EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
		walletTunnel.setCheckTime(new Date());
		walletTunnelDao.updateByPrimaryKeySelective(walletTunnel);

		// 验证记录
		Date curDate = new Date();
		WalletVerifyHis walletVerifyHis = walletVerifyHisExtDao.selectByWalletIdAndRefIdAndType(
			walletTunnel.getWalletId(), walletTunnel.getId(),
			WalletVerifyRefType.PERSON.getValue().byteValue());
		if (null == walletVerifyHis) {
			WalletVerifyHis his = WalletVerifyHis.builder()
				.walletId(walletTunnel.getWalletId())
				.refId(walletTunnel.getId())
				.type(WalletVerifyRefType.PERSON.getValue().byteValue())
				.verifyChannel(WalletVerifyChannel.TONGLIAN.getValue().byteValue())
				.verifyType(WalletVerifyType.TWO_FACTOR.getValue().byteValue())
				.verifyTime(curDate)
				.createTime(curDate)
				.build();
			walletVerifyHisExtDao.insertSelective(his);
		}

		// 更新钱包信息
		WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletTunnel.getWalletId());
		if (walletPerson == null) {
			walletPerson = WalletPerson.builder()
				.walletId(walletTunnel.getWalletId())
				.idType(EnumIdType.ID_CARD.getValue().byteValue())
				.idNo(idNo)
				.name(name)
				.tel(mobile)
				.realLevel(EnumDef.EnumUserRealLevel.ID_CARD.getValue().byteValue())
				.lastUpdTime(curDate)
				.createTime(curDate)
				.build();
			walletPersonDao.insertSelective(walletPerson);
		} else {
			walletPerson.setIdType(EnumDef.EnumIdType.ID_CARD.getValue().byteValue());
			walletPerson.setIdNo(idNo);
			walletPerson.setName(name);
			walletPerson.setTel(mobile);
			walletPerson.setRealLevel(EnumDef.EnumUserRealLevel.ID_CARD.getValue().byteValue());
			walletPerson.setLastUpdTime(curDate);
			walletPersonDao.updateByPrimaryKeySelective(walletPerson);
		}

		Wallet wallet = walletDao.selectByPrimaryKey(walletTunnel.getWalletId());
		if (WalletStatus.WAIT_AUDIT.getValue().byteValue() == wallet.getStatus().byteValue()) {
			wallet.setStatus(WalletStatus.ACTIVE.getValue());
		}
		byte auditType = Optional.ofNullable(wallet.getAuditType()).orElse((byte) 0);
		auditType += EnumWalletAuditType.ALLINPAY.getValue().byteValue();
		wallet.setAuditType(auditType);
		wallet.setLevel(EnumDef.EnumWalletLevel.SENIOR.getValue());
		Integer progress = Optional.ofNullable(wallet.getProgress()).orElse(0)
			| WalletProgress.TUNNEL_VALIDATE.getValue();
		wallet.setProgress(progress);
		walletDao.updateByPrimaryKeySelective(wallet);

		// 发送钱包事件
		walletEventService.sendEventMq(EnumDef.WalletEventType.CHANGE, wallet.getId());
	}


	/**
	 * 高级钱包企业资料审核
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletTunnel seniorWalletCompanyAudit(Integer channelType, Long walletId,
		Integer auditType, YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) {
		WalletTunnel walletChannel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(channelType.byteValue(), walletId);
		walletChannel.setStatus(
			EnumDef.WalletTunnelAuditStatus.WAITING_AUDIT.getValue().byteValue());
		CardInfo cardInfo = CardInfo.builder()
			.cardNo(companyBasicInfo.getAccountNo())
			.parentBankName(companyBasicInfo.getParentBankName())
			.bankName(companyBasicInfo.getBankName())
			.build();
		if (channelType == TunnelType.YUNST.getValue().intValue()) {
			try {
				boolean isAuth =
					auditType == EnumDef.WalletTunnelAuditType.AUTO.getValue().intValue();
				YunstSetCompanyInfoResult yunstSetCompanyInfoResult = yunstUserHandler
					.setCompanyInfo(walletChannel.getBizUserId(),
						isAuth, companyBasicInfo);
				Objects.requireNonNull(yunstSetCompanyInfoResult);

				log.info("通联审核结果:{}", JsonUtil.toJSON(yunstSetCompanyInfoResult));
				Long result = yunstSetCompanyInfoResult.getResult();
				String failReason = yunstSetCompanyInfoResult.getFailReason();
				String remark = yunstSetCompanyInfoResult.getRemark();
				Date curDate = new Date();
				if (Objects.nonNull(result)) {
					if (YunstCompanyInfoAuditStatus.SUCCESS.getValue() == result.longValue()) {
						walletChannel.setStatus(
							EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue()
								.byteValue());
						walletChannel.setFailReason(null);
						walletChannel.setCheckTime(curDate);

						this.synchronizeCompanyTunnelInfo(walletId, walletChannel);
						this.synchronizePublicAccountToWalletCard(walletId, cardInfo, curDate);
					} else if (YunstCompanyInfoAuditStatus.FAIL.getValue() == result.longValue()) {
						walletChannel.setStatus(
							EnumDef.WalletTunnelAuditStatus.AUDIT_FAIL.getValue()
								.byteValue());
						walletChannel.setFailReason(failReason);
					}
					walletChannel.setCheckTime(curDate);
				} else {
					walletChannel.setStatus(
						EnumDef.WalletTunnelAuditStatus.WAITING_AUDIT.getValue()
							.byteValue());
					walletChannel.setFailReason(null);
					this.synchronizePublicAccountToWalletCard(walletId, cardInfo, null);
				}
				walletChannel.setRemark(remark);
				walletTunnelDao.updateByPrimaryKeySelective(walletChannel);

			} catch (CommonGatewayException cge) {
				log.error("高级钱包企业信息审核失败 msg:{}", cge.getBankErrMsg());
				throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
					cge.getBankErrMsg());
			} catch (Exception e) {
				log.error("高级钱包企业信息审核失败 msg:{}", e.getMessage());
				throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
					e.getMessage());
			}
		}
		log.info("高级钱包企业信息审核状态:{},walletId:{}", walletChannel.getStatus(), walletId);
		return walletChannel;
	}

	/**
	 * 高级钱包绑定申请绑定手机
	 */
	public void sendVerifyCode(Integer channelType, Long walletId,
		String telephone, Integer smsCodeType) throws Exception {

		WalletTunnel walletChannel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(channelType.byteValue(), walletId);
		if (channelType.intValue() == TunnelType.YUNST.getValue().intValue()) {
			yunstUserHandler.sendVerificationCode(walletChannel.getBizUserId(), telephone,
				smsCodeType);
		}
	}

	/**
	 * 高级钱包扣款协议地址
	 */
	public String signBalanceProtocol(Long walletId, String jumpUrl) {
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(TunnelType.YUNST.getValue(), walletId);
		Objects.requireNonNull(walletTunnel);
		Tuple<String, String> balanceProtocolReqResult = yunstUserHandler
			.generateBalanceProtocolUrl(walletTunnel.getBizUserId(), jumpUrl,
				walletTunnel.getBalanceProtocolReqSn());
		walletTunnel.setBalanceProtocolReqSn(balanceProtocolReqResult.left);
		walletTunnelDao.updateByPrimaryKeySelective(walletTunnel);
		return balanceProtocolReqResult.right;
	}

	/**
	 * 高级钱包会员协议地址
	 */
	public String signMemberProtocol(Long walletId, String jumpUrl) {
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(TunnelType.YUNST.getValue(), walletId);
		Objects.requireNonNull(walletTunnel);
		return yunstUserHandler.generateSignContractUrl(walletTunnel.getBizUserId(), jumpUrl);
	}

	/**
	 * 高级钱包个人会员这只支付密码地址
	 */
	public String setPersonPayPassword(Long walletId, String jumpUrl) throws Exception {
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(TunnelType.YUNST.getValue(), walletId);
		Objects.requireNonNull(walletTunnel);
		WalletPerson person = walletPersonDao.selectByWalletId(walletId);
		return yunstUserHandler
			.generatePersonSetPayPasswordUrl(walletTunnel.getBizUserId(),
				walletTunnel.getSecurityTel(), person.getName(),
				EnumDef.EnumIdType.ID_CARD.getValue().longValue(), person.getIdNo(), jumpUrl);
	}

	/**
	 * 高级钱包-修改支付密码
	 */
	public String updateTunnelPayPwd(Long walletId, String jumpUrl) {
		WalletPerson person = walletPersonDao.selectByWalletId(walletId);
		WalletTunnel channel = verifyService.checkChannel(walletId, TunnelType.YUNST);

		jumpUrl = configService.getYunstJumpUrlPrefix() + jumpUrl;
		String signedParam = yunstUserHandler.updatePayPwd(person, channel, jumpUrl);
		return configService.getYunstUpdatePayPasswordUrl() + "?" + signedParam;
	}

	/**
	 * 高级钱包-重置支付密码
	 */
	public String resetTunnelPayPwd(Long walletId, String jumpUrl) {
		WalletPerson person = walletPersonDao.selectByWalletId(walletId);
		WalletTunnel channel = verifyService.checkChannel(walletId, TunnelType.YUNST);

		jumpUrl = configService.getYunstJumpUrlPrefix() + jumpUrl;
		String signedParam = yunstUserHandler.resetPayPwd(person, channel, jumpUrl);
		return configService.getYunstResetPayPwdUrl() + "?" + signedParam;
	}

	/**
	 * 高级钱包企业会员信息
	 */
	public CompanyInfoResult seniorWalletGetCompanyInfo(Long walletId)
		throws Exception {
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(TunnelType.YUNST.getValue(), walletId);
		Objects.requireNonNull(walletTunnel);
		CompanyInfoResult memberInfo = (CompanyInfoResult) yunstUserHandler
			.getMemberInfo(walletTunnel.getBizUserId());
		if (YunstCompanyInfoAuditStatus.WAITING.getValue().longValue() != memberInfo.getStatus()
			.longValue()
			&& WalletTunnelAuditStatus.WAITING_AUDIT.getValue().byteValue() == walletTunnel
			.getStatus()) {
			if (YunstCompanyInfoAuditStatus.SUCCESS.getValue().longValue() == memberInfo
				.getStatus()) {
				walletTunnel.setStatus(
					EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue()
						.byteValue());
				walletTunnel.setFailReason(null);
				walletTunnel.setSecurityTel(memberInfo.getPhone());
				this.synchronizeCompanyTunnelInfo(walletId, walletTunnel);
				this.updateNonVerifyPublicAccountToWalletCard(walletId,
					walletTunnel.getCheckTime());
			} else if (YunstCompanyInfoAuditStatus.FAIL.getValue().longValue() == memberInfo
				.getStatus()) {
				walletTunnel.setStatus(
					EnumDef.WalletTunnelAuditStatus.AUDIT_FAIL.getValue()
						.byteValue());
				walletTunnel.setFailReason(memberInfo.getFailReason());
				walletTunnel.setRemark(memberInfo.getRemark());
			}
			walletTunnel.setCheckTime(
				DateUtil.parse(memberInfo.getCheckTime(), DateUtil.STANDARD_DTAETIME_PATTERN));
			walletTunnelDao.updateByPrimaryKey(walletTunnel);
		}
		return memberInfo;
	}


	public CompanyInfoResult seniorWalletCompanyAuditOffline(Long walletId,
		YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo)
		throws Exception {
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(TunnelType.YUNST.getValue(), walletId);
		Objects.requireNonNull(walletTunnel);
		CompanyInfoResult memberInfo = (CompanyInfoResult) yunstUserHandler
			.getMemberInfo(walletTunnel.getBizUserId());
		CardInfo cardInfo = CardInfo.builder()
			.cardNo(companyBasicInfo.getAccountNo())
			.parentBankName(companyBasicInfo.getParentBankName())
			.bankName(companyBasicInfo.getBankName())
			.build();
		if (YunstCompanyInfoAuditStatus.SUCCESS.getValue().longValue() == memberInfo.getStatus()
			&&
			EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue() == walletTunnel
				.getStatus() &&
			walletTunnel.getCheckTime().compareTo(
				DateUtil.parse(memberInfo.getCheckTime(), DateUtil.STANDARD_DTAETIME_PATTERN))
				== -1) {
			walletTunnel.setFailReason(null);
			walletTunnel.setSecurityTel(memberInfo.getPhone());
			walletTunnel.setCheckTime(
				DateUtil.parse(memberInfo.getCheckTime(), DateUtil.STANDARD_DTAETIME_PATTERN));
			walletTunnelDao.updateByPrimaryKey(walletTunnel);
			this.synchronizeCompanyTunnelInfo(walletId, walletTunnel);
			this.synchronizePublicAccountToWalletCard(walletId, cardInfo,
				walletTunnel.getCheckTime());
		}
		return memberInfo;
	}

	/**
	 * 高级钱包个人会员信息
	 */
	public PersonInfoResult seniorWalletGetPersonInfo(Long walletId) throws Exception {
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(TunnelType.YUNST.getValue(), walletId);
		Objects.requireNonNull(walletTunnel);
		PersonInfoResult memberInfo = (PersonInfoResult) yunstUserHandler
			.getMemberInfo(walletTunnel.getBizUserId());
		return memberInfo;
	}

	/**
	 * 获取钱包渠道信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletTunnel getWalletTunnelInfo(Byte tunnelType, Long walletId) {
		WalletTunnel walletTunnel = walletTunnelDao
			.selectByTunnelTypeAndWalletId(tunnelType, walletId);

		Optional.ofNullable(walletTunnel).orElseThrow(() ->
			new WalletResponseException(EnumWalletResponseCode.TUNNEL_INFO_NOT_EXISTS,
				tunnelType + " " + walletId));

		boolean needUpdate = !Optional.ofNullable(walletTunnel.getIsDirty()).isPresent()
			|| walletTunnel.getIsDirty() != DirtyType.NORMAL.getValue().byteValue();
		Wallet wallet = walletDao.selectByPrimaryKey(walletTunnel.getWalletId());
		if (!needUpdate) {
			needUpdate = !Optional.ofNullable(wallet.getBalanceUpdTime()).isPresent()
				|| DateUtil.addSecs(wallet.getBalanceUpdTime(), 1800).before(new Date());
		}

		if (needUpdate) {
			updateBalance(walletTunnel, wallet);
		}

		//特殊处理思力账号
		if (Objects.nonNull(configService.getAgentEntWalletId())
			&& configService.getAgentEntWalletId() == walletId.longValue()
			&& WalletTunnelSignContract.MEMBER.getValue().byteValue() == walletTunnel
			.getIsSignContact()) {
			walletTunnel.setIsSignContact(WalletTunnelSignContract.BALANCE.getValue().byteValue());
		}
		return walletTunnel;
	}

	public void updateBalance(WalletTunnel walletTunnel, Wallet wallet) {
		// 目前只支持通联
		if (walletTunnel.getTunnelType().byteValue() != TunnelType.YUNST.getValue().byteValue()) {
			return;
		}

		// 通联查余额
		YunstQueryBalanceResult result = yunstUserHandler.queryBalance(walletTunnel.getBizUserId());
		// 更新通道余额
		walletTunnel.setBalance(result.getAllAmount());
		walletTunnel.setFreezenAmount(result.getFreezenAmount());
		walletTunnel.setIsDirty(DirtyType.NORMAL.getValue());
		walletTunnelDao.updateByPrimaryKeySelective(walletTunnel);
		// 更新钱包余额
		wallet.setWalletBalance(walletTunnel.getBalance());
		wallet.setFreezeAmount(walletTunnel.getFreezenAmount());
		wallet.setBalanceUpdTime(new Date());
		walletDao.updateByPrimaryKeySelective(wallet);
	}


	private void synchronizeCompanyTunnelInfo(Long walletId, WalletTunnel walletTunnel) {
		if (EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue()
			.byteValue() == walletTunnel.getStatus()) {
			WalletVerifyHis walletVerifyHis = walletVerifyHisExtDao
				.selectByWalletIdAndRefIdAndType(walletId, walletTunnel.getId(),
					WalletVerifyRefType.COMPANY.getValue().byteValue());
			if (null == walletVerifyHis
				|| walletVerifyHis.getVerifyTime().compareTo(walletTunnel.getCheckTime()) == -1) {
				walletVerifyHisExtDao.insertSelective(
					WalletVerifyHis.builder().walletId(walletId)
						.refId(walletTunnel.getId()).type(
						WalletVerifyRefType.COMPANY.getValue().byteValue())
						.verifyChannel(
							WalletVerifyChannel.TONGLIAN.getValue().byteValue())
						.verifyType(
							WalletVerifyType.COMPANY_VERIFY.getValue().byteValue())
						.verifyTime(walletTunnel.getCheckTime())
						.createTime(walletTunnel.getCheckTime()).build());
			}
		}
	}

	public void synchronizePublicAccountToWalletCard(Long walletId, CardInfo cardInfo,
		Date verifyTime) {
		List<WalletCard> walletCards = walletCardDao
			.selectPubAccountByWalletId(walletId);
		if (walletCards != null && !walletCards.isEmpty()) {
			walletCardDao
				.updateWalletCard(walletId, EnumWalletCardStatus.UNBIND.getValue(),
					EnumWalletCardStatus.BIND.getValue(),
					EnumPublicAccount.YES.getValue(), EnumDefBankCard.NO.getValue());
		}
		walletCardDao.insertSelective(
			WalletCard.builder()
				.cardType(WalletCardType.DEPOSIT.getValue())
				.walletId(walletId)
				.bankCode(" ")
				.bankName(cardInfo.getParentBankName())
				.depositBank(cardInfo.getBankName())
				.bankAccount(cardInfo.getCardNo())
				.verifyChannel(VerifyChannel.YUNST.getValue())
				.verifyTime(verifyTime)
				.isPublic(EnumPublicAccount.YES.getValue().byteValue())
				.isDef(EnumDefBankCard.YES.getValue().byteValue())
				.status(EnumWalletCardStatus.BIND.getValue().byteValue())
				.build());
	}

	public void updateNonVerifyPublicAccountToWalletCard(Long walletId, Date verifyTime) {
		WalletCard walletCard = walletCardDao.selectNonVerifyPubAccountByWalletId(walletId);
		Objects.requireNonNull(walletCard);
		walletCard.setVerifyTime(verifyTime);
		int effectRows = walletCardDao.updateByPrimaryKeySelective(walletCard);
		if (effectRows != 1) {
			log.error("更新对公账号信息审核时间失败");
		}
	}


	@Getter
	@Setter
	@Builder
	@ToString
	static class CardInfo {

		private String cardNo;
		private String parentBankName;
		private String bankName;
	}


	public static void main(String[] args) {
		WalletTunnel walletTunnel = new WalletTunnel();
		walletTunnel.setIsDirty((byte) 1);
		boolean needUpdate = !Optional.ofNullable(walletTunnel.getIsDirty()).isPresent() || Optional
			.ofNullable(walletTunnel.getIsDirty())
			.filter(dirty -> dirty != DirtyType.NORMAL.getValue().byteValue()).isPresent();
		System.out.println(needUpdate);
	}
}
