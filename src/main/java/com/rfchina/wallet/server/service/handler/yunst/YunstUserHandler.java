package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.bank.yunst.request.YunstApplyBindBankCardReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBalanceProtocolReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindBankCardReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstChangeBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstGetMemberInfoReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstPersonSetRealNameReq;
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
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSendVerificationCodeResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSetCompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstUnBindBankCardResult;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
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
	public String generateSignContractUrl(Long walletId, Byte source) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstSignContractReq req = YunstSignContractReq.builder$()
			.bizUserId(bizUserId)
			.jumpUrl(configService.getYunstResultJumpUrl())
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
	public String generatePersonSetPayPasswordUrl(Long walletId, Byte source, String phone,
		String name, Long identityType, String identityNo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstSetPayPwdReq req = YunstSetPayPwdReq.builder$()
			.bizUserId(bizUserId)
			.name(name)
			.phone(phone)
			.identityType(identityType)
			.identityNo(RSAUtil.encrypt(identityNo))
			.jumpUrl(configService.getYunstResultJumpUrl())
			.backUrl(configService.getYunstNotifybackUrl())
			.build();
		String res = yunstTpl.signRequest(req);
		String webParamUrl = configService.getYunstSignContractUrl() + "?" + res;
		log.info("webParamUrl: " + webParamUrl);
		return webParamUrl;
	}

	/**
	 * 委托扣款协议签约(生成前端H5 url)
	 */
	public String generateBalanceProtocolUrl(Long walletId, Byte source) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstBalanceProtocolReq req = YunstBalanceProtocolReq.builder$()
			.protocolReqSn(UUID.randomUUID().toString().replaceAll("-", ""))
			.payerId(bizUserId)
			.receiverId(configService.getYunstReceiverId())
			.protocolName(configService.getYunstBalanceProtocolName())
			.jumpUrl(configService.getYunstResultJumpUrl())
			.backUrl(configService.getYunstNotifybackUrl())
			.source(TERMINAL_TYPE)
			.build();
		String res = yunstTpl.signRequest(req);
		String webParamUrl = configService.getYunstSignBalanceProtocolUrl() + "?" + res;
		log.info("webParamUrl: " + webParamUrl);
		return webParamUrl;
	}

	/**
	 * 发送短信验证码
	 */
	public YunstSendVerificationCodeResult sendVerificationCode(Long walletId, Byte source, String phone, Integer bizType)
		throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
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
	public YunstBindPhoneResult bindPhone(Long walletId, Byte source, String phone,
		String verificationCode)
		throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
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
	public String modifyPhone(Long walletId, Byte source, String realName, String oldPhone,
		Long identityType,
		String identityNo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstChangeBindPhoneReq req = YunstChangeBindPhoneReq.builder$()
			.bizUserId(bizUserId)
			.name(realName)
			.identityType(identityType)
			.identityNo(RSAUtil.encrypt(identityNo))
			.oldPhone(oldPhone)
			.jumpUrl(configService.getYunstResultJumpUrl())
			.backUrl(configService.getYunstNotifybackUrl())
			.build();

		String res = yunstTpl.signRequest(req);
		String webParamUrl = configService.getYunstPersonChangeBindPhoneUrl() + "?" + res;
		log.info("webParamUrl: " + webParamUrl);
		return webParamUrl;
	}

	/**
	 * 获取会员信息
	 */
	public Object getMemberInfo(Long walletId, Byte source) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
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
	public YunstPersonSetRealNameResult personCertification(Long walletId, Byte source,
		String realName,
		Long identityType,
		String identityNo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
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
	public YunstSetCompanyInfoResult setCompanyInfo(Long walletId, Byte source, Boolean isAuth,
		YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
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
	public ApplyBindBankCardResp applyBindBankCard(Long walletId, Byte source, String cardNo,
		String name,
		String phone, Long identityType, String identityNo, String validate, String cvv2)
		throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
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
	public YunstBindBankCardResult bindBankCard(Long walletId, Byte source, String transNum,
		String transDate,
		String phone, String validate, String cvv2, String verificationCode) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
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
	public YunstUnBindBankCardResult unbindBankCard(Long walletId, Byte source, String cardNo)
		throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstUnBindBankCardReq req = YunstUnBindBankCardReq.builder$()
			.bizUserId(bizUserId)
			.cardNo(RSAUtil.encrypt(cardNo))
			.build();

		return yunstTpl.execute(req, YunstUnBindBankCardResult.class);
	}

}
