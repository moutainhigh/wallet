package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.ChannelType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumVerifyCodeType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletAuditType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletChannelSignContract;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyRefType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletVerifyHis;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.PersonInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstQueryBalanceResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSetCompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletVerifyHisExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.EnumYunstResponse;
import com.rfchina.wallet.server.msic.EnumWallet.WalletStatus;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler.YunstMemberType;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
	private WalletChannelExtDao walletChannelDao;

	@Autowired
	private WalletVerifyHisExtDao walletVerifyHisExtDao;


	/**
	 * 升级高级钱包
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletChannel createSeniorWallet(Integer channelType, Long walletId, Byte source) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("开通高级钱包失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"开通高级钱包失败");
		}
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(channelType, walletId);
		if (walletChannel != null) {
			return walletChannel;
		}
		WalletChannel.WalletChannelBuilder builder = WalletChannel.builder()
			.channelType(channelType.byteValue())
			.status(EnumDef.WalletChannelAuditStatus.NOT_COMMIT.getValue().byteValue())
			.walletId(walletId)
			.createTime(new Date());
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			Tuple<YunstCreateMemberResult, YunstMemberType> member = null;
			try {
				member = yunstUserHandler.createMember(walletId, source);
			} catch (Exception e) {
				log.error("开通高级钱包失败, channelType: {}, walletId: {}, source:{}", channelType,
					walletId, source);
				throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
					"开通高级钱包失败");
			}
			builder.bizUserId(member.left.getBizUserId())
				.channelUserId(member.left.getUserId())
				.memberType(member.right.getValue().byteValue());
		}
		walletChannel = builder.build();
		int effectRows = walletChannelDao.insertSelective(walletChannel);
		if (effectRows != 1) {
			log.error("开通高级钱包失败, channelType: {}, walletId: {}, source:{}", channelType,
				walletId,
				source);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"开通高级钱包失败-插入渠道信息");
		}
		wallet.setLevel(EnumDef.EnumWalletLevel.SENIOR.getValue());
		effectRows = walletDao.updateByPrimaryKeySelective(wallet);
		if (effectRows != 1) {
			log.error("更新钱包等级失败, walletId: {}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"更新钱包等级失败");
		}
		return walletChannel;
	}

	/**
	 * 更新钱包等级
	 */
	public Wallet upgradeWalletLevel(Long walletId) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("更新钱包等级失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"更新钱包等级失败");
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void seniorWalletBindPhone(Integer channelType, Long walletId, String mobile,
		String verifyCode) throws Exception {
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			WalletChannel walletChannel = walletChannelDao
				.selectByChannelTypeAndWalletId(channelType, walletId);

			String transformBizUserId = walletChannel.getBizUserId();
			if (walletChannel == null) {
				log.error("未创建云商通用户: bizUserId:{}", transformBizUserId);
				throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
					"未创建云商通用户");
			}

			if (!StringUtils.isEmpty(walletChannel.getSecurityTel())) {
				log.error("已设置安全手机: bizUserId:{}", transformBizUserId);
				throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
					"已设置安全手机");
			}

			try {
				yunstUserHandler.bindPhone(walletChannel.getBizUserId(), mobile, verifyCode);
			} catch (CommonGatewayException e) {
				if (!EnumYunstResponse.ALREADY_BIND_PHONE.getValue()
					.equals(e.getBankErrCode())) {
					log.error("渠道绑定手机失败: bizUserId:{}", transformBizUserId);
					throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
						"渠道绑定手机失败");
				}
			}
			walletChannel.setSecurityTel(mobile);
			int effectRows = walletChannelDao.updateByPrimaryKeySelective(walletChannel);
			if (effectRows != 1) {
				log.error("更新高级钱包手机信息失败:effectRows:{},walletChannel: {}", effectRows,
					JsonUtil.toJSON(walletChannel));
				throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
					"更新高级钱包手机信息失败");
			}
		}
	}

	/**
	 * 高级钱包个人修改绑定手机
	 */
	public String seniorWalletPersonChangeBindPhone(Long walletId, String realName, String idNo,
		String mobile) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(ChannelType.YUNST.getValue().intValue(), walletId);
		if (walletChannel == null) {
			log.error("未创建云商通用户: walletId:{}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"未创建云商通用户");
		}
		return yunstUserHandler.modifyPhone(walletChannel.getBizUserId(), realName, mobile,
			EnumDef.EnumIdType.ID_CARD.getValue().longValue(), idNo);
	}

	/**
	 * 高级钱包个人认证
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void seniorWalletPersonAuth(Integer channelType, Long walletId, String realName,
		String idNo,
		String mobile, String verifyCode) throws Exception {
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			WalletChannel walletChannel = walletChannelDao
				.selectByChannelTypeAndWalletId(channelType, walletId);
			if (walletChannel == null) {
				log.error("未创建云商通用户: walletId:{}", walletChannel);
				throw new WalletResponseException(
					EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
			}

			if (EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue()
				== walletChannel.getStatus()) {
				return;
			}

			if (StringUtils.isEmpty(walletChannel.getSecurityTel())) {
				this.seniorWalletBindPhone(channelType, walletId, mobile, verifyCode);
			}
			Date curDate = new Date();
			yunstUserHandler.personCertification(walletChannel.getBizUserId(), realName,
				EnumDef.EnumIdType.ID_CARD.getValue().longValue(), idNo);
			walletChannel.setStatus(
				EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
			walletChannel.setCheckTime(new Date());
			int effectRows = walletChannelDao.updateByPrimaryKeySelective(walletChannel);
			if (effectRows != 1) {
				log.error("更新高级钱包审核状态信息失败:effectRows:{},walletChannel: {}", effectRows,
					JsonUtil.toJSON(walletChannel));
				throw new RfchinaResponseException(
					EnumResponseCode.COMMON_FAILURE, "更新高级钱包审核状态信息失败");
			}

			walletVerifyHisExtDao.insertSelective(
				WalletVerifyHis.builder().walletId(walletId)
					.refId(walletChannel.getId()).type(
					WalletVerifyRefType.PERSON.getValue().byteValue()).verifyType(
					WalletVerifyChannel.TONGLIAN.getValue().byteValue()).verifyType(
					WalletVerifyType.TWO_FACTOR.getValue().byteValue())
					.verifyTime(curDate).createTime(curDate).build());

			WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
			if (walletPerson == null) {
				effectRows = walletPersonDao.insertSelective(WalletPerson.builder()
					.walletId(walletId)
					.idType(EnumDef.EnumIdType.ID_CARD.getValue().byteValue())
					.idNo(idNo)
					.name(realName)
					.realLevel(EnumDef.EnumUserRealType.ID_CARD.getValue().byteValue())
					.tel(mobile)
					.createTime(curDate)
					.lastUpdTime(curDate)
					.build());
				if (effectRows != 1) {
					log.error("更新个人钱包个人信息表失败:channelType: {}, walletId:{}", channelType,
						walletId);
					throw new RfchinaResponseException(
						EnumResponseCode.COMMON_FAILURE, "更新个人钱包个人信息表失败");
				}
			}

			walletDao.updateActiveStatus(walletId,
				WalletStatus.ACTIVE.getValue(),
				EnumWalletAuditType.ALLINPAY.getValue().longValue());
		}
	}

	/**
	 * 高级钱包企业资料审核
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletChannel seniorWalletCompanyAudit(Integer channelType, Long walletId,
		Integer auditType, YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(channelType, walletId);
		walletChannel.setStatus(
			EnumDef.WalletChannelAuditStatus.WAITING_AUDIT.getValue().byteValue());
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			String transformBizUserId = walletChannel.getBizUserId();
			try {
				boolean isAuth =
					auditType == EnumDef.WalletChannelAuditType.AUTO.getValue().intValue();
				YunstSetCompanyInfoResult yunstSetCompanyInfoResult = yunstUserHandler
					.setCompanyInfo(walletChannel.getBizUserId(),
						isAuth, companyBasicInfo);

				if (Objects.nonNull(yunstSetCompanyInfoResult) && yunstSetCompanyInfoResult
					.getBizUserId()
					.equals(transformBizUserId)) {
					log.info("通联审核结果:{}", JsonUtil.toJSON(yunstSetCompanyInfoResult));
					Long result = yunstSetCompanyInfoResult.getResult();
					String failReason = yunstSetCompanyInfoResult.getFailReason();
					String remark = yunstSetCompanyInfoResult.getRemark();
					Date curDate = new Date();
					if (Objects.nonNull(result)) {
						if (2L == result.longValue()) {
							walletChannel.setStatus(
								EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue()
									.byteValue());
							walletChannel.setFailReason(null);
							walletChannel.setCheckTime(curDate);

							walletVerifyHisExtDao.insertSelective(
								WalletVerifyHis.builder().walletId(walletId)
									.refId(walletChannel.getId()).type(
									WalletVerifyRefType.COMPANY.getValue().byteValue())
									.verifyType(
										WalletVerifyChannel.TONGLIAN.getValue().byteValue())
									.verifyType(
										WalletVerifyType.COMPANY_VERIFY.getValue().byteValue())
									.verifyTime(curDate).createTime(curDate).build());
						} else if (3L == result.longValue()) {
							walletChannel.setStatus(
								EnumDef.WalletChannelAuditStatus.AUDIT_FAIL.getValue()
									.byteValue());
							walletChannel.setFailReason(failReason);
						}
						walletChannel.setCheckTime(curDate);
					} else {
						walletChannel.setStatus(
							EnumDef.WalletChannelAuditStatus.WAITING_AUDIT.getValue()
								.byteValue());
					}
					walletChannel.setRemark(remark);
					walletChannel.setFailReason(null);
					walletChannelDao.updateByPrimaryKeySelective(walletChannel);

				} else {
					log.error("高级钱包企业信息审核失败:channelType: {}, walletId:{}", channelType,
						walletId);
					throw new RfchinaResponseException(
						EnumResponseCode.COMMON_FAILURE, "高级钱包企业信息审核失败");
				}
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
	public void seniorWalletApplyBindPhone(Integer channelType, Long walletId,
		String telephone) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(channelType, walletId);
		if (channelType.intValue() == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			yunstUserHandler.sendVerificationCode(walletChannel.getBizUserId(), telephone,
				EnumVerifyCodeType.YUNST_BIND_PHONE.getValue());
		}
	}

	/**
	 * 高级钱包扣款协议地址
	 */
	public String signBalanceProtocol(Long walletId) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(ChannelType.YUNST.getValue().intValue(), walletId);
		if (walletChannel == null) {
			log.error("未创建云商通用户: walletId:{}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"未创建云商通用户");
		}
		Tuple<String, String> balanceProtocolReqResult = yunstUserHandler
			.generateBalanceProtocolUrl(walletChannel.getBizUserId());
		walletChannel.setBalanceProtocolReqSn(balanceProtocolReqResult.left);
		walletChannelDao.updateByPrimaryKeySelective(walletChannel);
		return balanceProtocolReqResult.right;
	}

	/**
	 * 高级钱包会员协议地址
	 */
	public String signMemberProtocol(Long walletId) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(ChannelType.YUNST.getValue().intValue(), walletId);
		if (walletChannel == null) {
			log.error("未创建云商通用户: walletId:{}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"未创建云商通用户");
		}
		return yunstUserHandler.generateSignContractUrl(walletChannel.getBizUserId());
	}

	/**
	 * 高级钱包会员协议地址
	 */
	public String setPersonPayPassword(Long walletId, String phone,
		String name, String identityNo) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(ChannelType.YUNST.getValue().intValue(), walletId);
		if (walletChannel == null) {
			log.error("未创建云商通用户: walletId:{}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"未创建云商通用户");
		}
		return yunstUserHandler
			.generatePersonSetPayPasswordUrl(walletChannel.getBizUserId(), phone, name,
				EnumDef.EnumIdType.ID_CARD.getValue().longValue(), identityNo);
	}

	/**
	 * 高级钱包企业会员信息
	 */
	public CompanyInfoResult seniorWalletGetCompanyInfo(Long walletId) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(ChannelType.YUNST.getValue().intValue(), walletId);
		if (walletChannel == null) {
			log.error("未创建云商通用户: walletId:{}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"未创建云商通用户");
		}
		CompanyInfoResult memberInfo = (CompanyInfoResult) yunstUserHandler
			.getMemberInfo(walletChannel.getBizUserId());
		return memberInfo;
	}

	/**
	 * 高级钱包个人会员信息
	 */
	public PersonInfoResult seniorWalletGetPersonInfo(Long walletId) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(ChannelType.YUNST.getValue().intValue(), walletId);
		if (walletChannel == null) {
			log.error("未创建云商通用户: walletId:{}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"未创建云商通用户");
		}
		PersonInfoResult memberInfo = (PersonInfoResult) yunstUserHandler
			.getMemberInfo(walletChannel.getBizUserId());
		return memberInfo;
	}

	/**
	 * 获取钱包渠道信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletChannel getWalletChannel(Integer channelType, Long walletId) throws Exception {
		WalletChannel walletChannel = walletChannelDao
			.selectByChannelTypeAndWalletId(channelType, walletId);
		if (walletChannel == null) {
			log.error("未创建云商通用户: walletId:{}", walletId);
			throw new RfchinaResponseException(EnumResponseCode.COMMON_FAILURE,
				"未创建云商通用户");
		}
		if (null != walletChannel.getIsSignContact()
			&& walletChannel.getIsSignContact() != WalletChannelSignContract.NONE.getValue()
			.byteValue()) {
			YunstQueryBalanceResult yunstQueryBalanceResult = yunstUserHandler
				.queryBalance(walletChannel.getBizUserId());

			Long allAmount = walletChannel.getBalance();
			Long freezenAmount = walletChannel.getFreezenAmount();

			if (null == allAmount || null == freezenAmount
				|| allAmount.longValue() != yunstQueryBalanceResult.getAllAmount()
				|| freezenAmount.longValue() != yunstQueryBalanceResult.getFreezenAmount()) {
				//金额不一致 更新
				walletChannel.setBalance(yunstQueryBalanceResult.getAllAmount());
				walletChannel.setFreezenAmount(yunstQueryBalanceResult.getFreezenAmount());

				walletChannelDao.updateByPrimaryKeySelective(walletChannel);

				Wallet wallet = walletDao.selectByPrimaryKey(walletId);
				wallet.setWalletBalance(walletChannel.getBalance());
				wallet.setFreezeAmount(walletChannel.getFreezenAmount());

				walletDao.updateByPrimaryKeySelective(wallet);
			}
		}

		return walletChannel;
	}
}
