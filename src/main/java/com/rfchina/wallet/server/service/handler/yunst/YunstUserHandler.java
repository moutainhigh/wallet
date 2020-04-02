package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.exception.UnknownException;
import com.rfchina.wallet.server.bank.yunst.request.QueryBalanceReq;
import com.rfchina.wallet.server.bank.yunst.request.ResetPayPwdReq;
import com.rfchina.wallet.server.bank.yunst.request.UnBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.UpdatePayPwdReq;
import com.rfchina.wallet.server.bank.yunst.request.UpdatePhoneByPayPwdReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstApplyBindBankCardReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBalanceProtocolReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindBankCardReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstGetMemberInfoReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstPersonSetRealNameReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSMSVerificationCodeReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetPayPwdReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSignContractReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstUnBindBankCardReq;
import com.rfchina.wallet.server.bank.yunst.response.UnBindPhoneResp;
import com.rfchina.wallet.server.bank.yunst.response.result.ApplyBindBankCardResp;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstBindBankCardResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstBindPhoneResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstPersonSetRealNameResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstQueryBalanceResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSendVerificationCodeResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSetCompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstUnBindBankCardResult;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.msic.EnumYunst.EnumYunstResponse;
import com.rfchina.wallet.server.msic.EnumYunst.YunstIdType;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.util.IdNumValidUtil;
import java.util.TimeZone;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class YunstUserHandler extends YunstBaseHandler {

	@Autowired
	private ConfigService configService;

	@Autowired
	private YunstTpl yunstTpl;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	/**
	 * 创建会员
	 */
	public Tuple<YunstCreateMemberResult, YunstMemberType> createMember(Long walletId, Byte source)
		throws Exception {
		YunstMemberType memberType =
			(source == 1) ? YunstMemberType.COMPANY : YunstMemberType.PERSON;
		String bizUserId = transferToYunstBizUserFormat(walletId, source, configService.getEnv());
		YunstCreateMemberReq req = YunstCreateMemberReq.builder$()
			.bizUserId(bizUserId)
			.memberType(memberType.getValue())
			.source(TERMINAL_TYPE)
			.build();

		YunstCreateMemberResult resp = yunstTpl.execute(req, YunstCreateMemberResult.class);
		return new Tuple<>(resp, memberType);
	}

	/**
	 * 会员电子协议签约(生成前端H5 url)
	 */
	public String generateSignContractUrl(String bizUserId, String jumpUrl) {
		YunstSignContractReq req = YunstSignContractReq.builder$()
			.bizUserId(bizUserId)
			.jumpUrl(configService.getYunstJumpUrlPrefix() + jumpUrl)
			.backUrl(configService.getYunstNotifybackUrl())
			.source(TERMINAL_TYPE)
			.build();
		String res = yunstTpl.signRequest(req);
		String webParamUrl = configService.getYunstSignContractUrl() + "?" + res;
		log.info("webParamUrl: " + webParamUrl);
		return webParamUrl;
	}

	/**
	 * 设置支付密码(个人)
	 */
	public String generatePersonSetPayPasswordUrl(String bizUserId, String phone,
		String name, Long identityType, String identityNo, String jumpUrl) throws Exception {
		YunstSetPayPwdReq req = YunstSetPayPwdReq.builder$()
			.bizUserId(bizUserId)
			.name(name)
			.phone(phone)
			.identityType(identityType)
			.identityNo(RSAUtil.encrypt(identityNo))
			.jumpUrl(configService.getYunstJumpUrlPrefix() + jumpUrl)
			.backUrl(configService.getYunstNotifybackUrl())
			.build();
		String res = yunstTpl.signRequest(req);
		String webParamUrl = configService.getYunstSetPayPasswordUrl() + "?" + res;
		log.info("webParamUrl: " + webParamUrl);
		return webParamUrl;
	}

	/**
	 * 设置支付密码(个人)
	 */
	public String updatePayPwd(WalletPerson person, WalletTunnel channel, String jumpUrl) {

		String encryptIdNo = null;
		try {
			encryptIdNo = RSAUtil.encrypt(person.getIdNo());
		} catch (Exception e) {
			log.error("身份证加密错误", e);
		}
		UpdatePayPwdReq req = UpdatePayPwdReq.builder()
			.bizUserId(channel.getBizUserId())
			.name(person.getName())
			.identityType(YunstIdType.ID_CARD.getValue())
			.identityNo(encryptIdNo)
			.jumpUrl(jumpUrl)
			.backUrl(configService.getYunstNotifybackUrl())
			.build();
		return yunstTpl.signRequest(req);
	}

	/**
	 * 设置支付密码(个人)
	 */
	public String resetPayPwd(WalletPerson person, WalletTunnel channel, String jumpUrl) {

		String encryptIdNo = null;
		try {
			encryptIdNo = RSAUtil.encrypt(person.getIdNo());
		} catch (Exception e) {
			log.error("身份证加密错误", e);
		}
		ResetPayPwdReq req = ResetPayPwdReq.builder()
			.bizUserId(channel.getBizUserId())
			.name(person.getName())
			.phone(channel.getSecurityTel())
			.identityType(YunstIdType.ID_CARD.getValue())
			.identityNo(encryptIdNo)
			.jumpUrl(jumpUrl)
			.backUrl(configService.getYunstNotifybackUrl())
			.build();
		return yunstTpl.signRequest(req);
	}

	/**
	 * 委托扣款协议签约(生成前端H5 url)
	 */
	public Tuple<String, String> generateBalanceProtocolUrl(String bizUserId, String jumpUrl,
		String protocolReqSn) {

		WalletTunnel agentEnt = walletTunnelDao
			.selectByWalletId(configService.getAgentEntWalletId(), TunnelType.YUNST.getValue());

		if (StringUtils.isBlank(protocolReqSn)) {
			protocolReqSn = UUID.randomUUID().toString().replaceAll("-", "");
		}
		YunstBalanceProtocolReq req = YunstBalanceProtocolReq.builder$()
			.protocolReqSn(protocolReqSn)
			.payerId(bizUserId)
			.receiverId(agentEnt.getBizUserId())
			.protocolName(configService.getYunstBalanceProtocolName())
			.jumpUrl(configService.getYunstJumpUrlPrefix() + jumpUrl)
			.backUrl(configService.getYunstNotifybackUrl())
			.source(TERMINAL_TYPE)
			.build();
		String res = yunstTpl.signRequest(req);
		String webParamUrl = configService.getYunstSignBalanceProtocolUrl() + "?" + res;
		log.info("webParamUrl: " + webParamUrl);
		return new Tuple<>(protocolReqSn, webParamUrl);
	}

	/**
	 * 发送短信验证码
	 */
	public YunstSendVerificationCodeResult sendVerificationCode(String bizUserId,
		String phone, Integer bizType)
		throws Exception {
		YunstSMSVerificationCodeReq req = YunstSMSVerificationCodeReq.builder$()
			.bizUserId(bizUserId)
			.phone(phone)
			.verificationCodeType(bizType.longValue())
			.build();

		YunstSendVerificationCodeResult result = yunstTpl
			.execute(req, YunstSendVerificationCodeResult.class);
		return result;
	}

	/**
	 * 绑定手机
	 */
	public YunstBindPhoneResult bindPhone(String bizUserId, String phone,
		String verificationCode) throws Exception {

		YunstBindPhoneReq req = YunstBindPhoneReq.builder$()
			.bizUserId(bizUserId)
			.phone(phone)
			.verificationCode(verificationCode)
			.build();
		try {
			return yunstTpl.execute(req, YunstBindPhoneResult.class);
		} catch (CommonGatewayException e) {
			if (EnumYunstResponse.ALREADY_BIND_PHONE.getValue().equals(e.getBankErrCode())) {
				log.warn("高级钱包-通道已绑定手机: bizUserId:{}", bizUserId);
			}
			if (EnumYunstResponse.VERIFYCODE_ERROR.getValue().equals(e.getBankErrCode())) {
				log.warn("高级钱包-验证码错误: bizUserId:{}", bizUserId);
			}
			throw e;
		} catch (Exception e) {
			log.error("未定义异常", e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}

	/**
	 * 解绑手机
	 */
	public UnBindPhoneResp unBindPhone(String bizUserId, String phone,
		String verificationCode) {
		UnBindPhoneReq req = UnBindPhoneReq.builder()
			.bizUserId(bizUserId)
			.phone(phone)
			.verificationCode(verificationCode)
			.build();
		try {
			return yunstTpl.execute(req, UnBindPhoneResp.class);
		} catch (Exception e) {
			log.error("[解绑手机] 异常",e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}

	}

	/**
	 * 修改绑定手机
	 */
	public String updateSecurityTel(String bizUserId, String realName, String oldPhone,
		String identityNo, String jumpUrl) {

		String encryptIdNo = null;
		try {
			encryptIdNo = RSAUtil.encrypt(identityNo);
		} catch (Exception e) {
			log.error("身份证加密错误", e);
		}
		UpdatePhoneByPayPwdReq req = UpdatePhoneByPayPwdReq.builder()
			.bizUserId(bizUserId)
			.name(realName)
			.identityType(YunstIdType.ID_CARD.getValue())
			.identityNo(encryptIdNo)
			.oldPhone(oldPhone)
			.jumpUrl(jumpUrl)
			.backUrl(configService.getYunstNotifybackUrl())
			.build();

		return yunstTpl.signRequest(req);
	}

	/**
	 * 获取会员信息
	 */
	public Object getMemberInfo(String bizUserId) throws Exception {
		YunstGetMemberInfoReq req = YunstGetMemberInfoReq.builder$().bizUserId(bizUserId).build();

		YunstMemberInfoResult memberInfoResult = null;
		memberInfoResult = yunstTpl.execute(req, YunstMemberInfoResult.class);

		long memberType = memberInfoResult.getMemberType();
		if (memberType == YunstMemberType.COMPANY.getValue()) {
			return JsonUtil.toObject(JsonUtil.toJSON(memberInfoResult.getMemberInfo()),
				YunstMemberInfoResult.CompanyInfoResult.class, objectMapper -> {
					objectMapper.setTimeZone(TimeZone.getDefault());
					objectMapper
						.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				});
		} else {
			return JsonUtil.toObject(JsonUtil.toJSON(memberInfoResult.getMemberInfo()),
				YunstMemberInfoResult.PersonInfoResult.class, objectMapper -> {
					objectMapper.setTimeZone(TimeZone.getDefault());
					objectMapper
						.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				});
		}
	}

	/**
	 * 个人实名认证
	 */
	public YunstPersonSetRealNameResult personCertification(String bizUserId,
		String realName, Long identityType, String idNo) throws Exception {
		String identityNo;
		try {
			if ("H04496326".equals(idNo)) {
				identityType = 99L;
			}
			identityNo = RSAUtil.encrypt(idNo);
		} catch (Exception e) {
			log.error("身份证加密失败", e);
			throw e;
		}
		YunstPersonSetRealNameReq req = YunstPersonSetRealNameReq.builder$()
			.bizUserId(bizUserId)
			.isAuth(true)
			.name(realName)
			.identityType(identityType)
			.identityNo(identityNo)
			.build();

		try {
			YunstPersonSetRealNameResult result = yunstTpl.execute(req,
				YunstPersonSetRealNameResult.class);
			return result;
		} catch (CommonGatewayException e) {
			if (EnumYunstResponse.ALREADY_REALNAME_AUTH.getValue().equals(e.getBankErrCode())) {
				log.warn("高级钱包-通道已实名, bizUserId:{}", bizUserId);
			} else {
				log.error("高级钱包-实名验证失败, bizUserId:{}", bizUserId);
			}
			throw e;
		} catch (Exception e) {
			log.error("未定义异常", e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}

	/**
	 * 设置企业信息
	 */
	public YunstSetCompanyInfoResult setCompanyInfo(String bizUserId, Boolean isAuth,
		YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) throws Exception {
		if (!IdNumValidUtil.isIDNumber(companyBasicInfo.getLegalIds())) {
			companyBasicInfo.setIdentityType(99L);
		}
//		if ("H04496326".equals(companyBasicInfo.getLegalIds())){
//			companyBasicInfo.setIdentityType(99L);
//		}
		companyBasicInfo.setLegalIds(RSAUtil.encrypt(companyBasicInfo.getLegalIds()));
		companyBasicInfo.setAccountNo(RSAUtil.encrypt(companyBasicInfo.getAccountNo()));
		YunstSetCompanyInfoReq.YunstSetCompanyInfoReqBuilder builder = YunstSetCompanyInfoReq
			.builder$()
			.bizUserId(bizUserId)
			.isAuth(isAuth)
			.companyBasicInfo(companyBasicInfo);
		if (!isAuth) {
			builder.backUrl(configService.getYunstNotifybackUrl());
		}

		return yunstTpl.execute(builder.build(), YunstSetCompanyInfoResult.class);
	}

	/**
	 * 申请绑定银行卡
	 */
	public ApplyBindBankCardResp applyBindBankCard(String bizUserId, String cardNo,
		String name,
		String phone, Long identityType, String identityNo, String validate, String cvv2)
		throws Exception {
		YunstApplyBindBankCardReq.YunstApplyBindBankCardReqBuilder buider = YunstApplyBindBankCardReq
			.builder$()
			.bizUserId(bizUserId)
			.cardNo(RSAUtil.encrypt(cardNo))
			.phone(phone)
			.name(name)
			.identityType(identityType)
			.identityNo(RSAUtil.encrypt(identityNo))
			.cardCheck(BIND_CARD_TYPE);
		if (StringUtils.isNoneBlank(validate, cvv2)) {
			buider.cvv2(RSAUtil.encrypt(cvv2)).validate(RSAUtil.encrypt(validate));
		}
		return yunstTpl.execute(buider.build(), ApplyBindBankCardResp.class);
	}

	/**
	 * 确认绑定银行卡
	 */
	public YunstBindBankCardResult bindBankCard(String bizUserId, String transNum,
		String transDate,
		String phone, String validate, String cvv2, String verificationCode) throws Exception {
		YunstBindBankCardReq.YunstBindBankCardReqBuilder buider = YunstBindBankCardReq.builder$()
			.bizUserId(bizUserId)
			.tranceNum(transNum)
			.transDate(transDate)
			.phone(phone)
			.verificationCode(verificationCode);
		if (StringUtils.isNoneBlank(validate, cvv2)) {
			buider.cvv2(RSAUtil.encrypt(cvv2)).validate(RSAUtil.encrypt(validate));
		}

		return yunstTpl.execute(buider.build(), YunstBindBankCardResult.class);
	}

	/**
	 * 解除绑定银行卡
	 */
	public YunstUnBindBankCardResult unbindBankCard(String bizUserId, String cardNo)
		throws Exception {
		YunstUnBindBankCardReq req = YunstUnBindBankCardReq.builder$()
			.bizUserId(bizUserId)
			.cardNo(RSAUtil.encrypt(cardNo))
			.build();

		return yunstTpl.execute(req, YunstUnBindBankCardResult.class);
	}

	/**
	 * 查询余额
	 */
	public YunstQueryBalanceResult queryBalance(String bizUserId) {
		QueryBalanceReq req = QueryBalanceReq.builder()
			.bizUserId(bizUserId)
			.accountSetNo(configService.getUserAccSet())
			.build();

		try {
			return yunstTpl.execute(req, YunstQueryBalanceResult.class);
		} catch (Exception e) {
			log.error("查询余额错误 " + bizUserId, e);
			throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}

}
