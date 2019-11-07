package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.bank.yunst.exception.UnknownException;
import com.rfchina.wallet.server.bank.yunst.request.ResetPayPwdReq;
import com.rfchina.wallet.server.bank.yunst.request.UpdatePhoneByPayPwdReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstApplyBindBankCardReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBalanceProtocolReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindBankCardReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstGetMemberInfoReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstPersonSetRealNameReq;
import com.rfchina.wallet.server.bank.yunst.request.QueryBalanceReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSMSVerificationCodeReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetPayPwdReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSignContractReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstUnBindBankCardReq;
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
import com.rfchina.wallet.server.msic.EnumWallet.YunstIdType;
import java.util.TimeZone;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Import(YunstTpl.class)
public class YunstUserHandler extends YunstBaseHandler {

	@Autowired
	private YunstTpl yunstTpl;

	/**
	 * 创建会员
	 */
	public Tuple<YunstCreateMemberResult, YunstMemberType> createMember(Long walletId, Byte source)
		throws Exception {
		YunstMemberType memberType = YunstMemberType.PERSON;
		if (source == 1) {
			memberType = YunstMemberType.COMPANY;
		}
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstCreateMemberReq req = YunstCreateMemberReq.builder$()
			.bizUserId(bizUserId)
			.memberType(memberType.getValue())
			.source(TERMINAL_TYPE)
			.build();

		return new Tuple<>(yunstTpl.execute(req, YunstCreateMemberResult.class), memberType);
	}

	/**
	 * 会员电子协议签约(生成前端H5 url)
	 */
	public String generateSignContractUrl(String bizUserId, String jumpUrl) {
		YunstSignContractReq req = YunstSignContractReq.builder$()
			.bizUserId(bizUserId)
			.jumpUrl(configService.getYunstResultJumpUrl() + jumpUrl)
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
			.jumpUrl(configService.getYunstResultJumpUrl() + jumpUrl)
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
			.jumpUrl(configService.getYunstJumpUrlPrefix() + jumpUrl)
			.backUrl(configService.getYunstNotifybackUrl())
			.build();
		return yunstTpl.signRequest(req);
	}

	/**
	 * 委托扣款协议签约(生成前端H5 url)
	 */
	public Tuple<String, String> generateBalanceProtocolUrl(String bizUserId, String jumpUrl) {
		String protocolReqSn = UUID.randomUUID().toString().replaceAll("-", "");
		YunstBalanceProtocolReq req = YunstBalanceProtocolReq.builder$()
			.protocolReqSn(protocolReqSn)
			.payerId(bizUserId)
			.receiverId(configService.getYunstReceiverId())
			.protocolName(configService.getYunstBalanceProtocolName())
			.jumpUrl(configService.getYunstResultJumpUrl() + jumpUrl)
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
		String verificationCode)
		throws Exception {
		YunstBindPhoneReq req = YunstBindPhoneReq.builder$()
			.bizUserId(bizUserId)
			.phone(phone)
			.verificationCode(verificationCode)
			.build();
		YunstBindPhoneResult result = yunstTpl.execute(req, YunstBindPhoneResult.class);
		return result;
	}

	/**
	 * 修改绑定手机
	 */
	public String resetSecurityTel(String bizUserId, String realName, String oldPhone,
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
			.jumpUrl(configService.getYunstJumpUrlPrefix() + jumpUrl)
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
		String realName,
		Long identityType,
		String identityNo) throws Exception {
		YunstPersonSetRealNameReq req = YunstPersonSetRealNameReq.builder$()
			.bizUserId(bizUserId)
			.isAuth(true)
			.name(realName)
			.identityType(identityType)
			.identityNo(RSAUtil.encrypt(identityNo))
			.build();

		YunstPersonSetRealNameResult result = yunstTpl
			.execute(req, YunstPersonSetRealNameResult.class);
		return result;
	}

	/**
	 * 设置企业信息
	 */
	public YunstSetCompanyInfoResult setCompanyInfo(String bizUserId, Boolean isAuth,
		YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) throws Exception {
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
