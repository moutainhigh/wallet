package com.rfchina.wallet.server.service.handler.yunst;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstChangeBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSMSVerificationCodeReq;
import com.rfchina.wallet.server.bank.yunst.request.*;
import com.rfchina.wallet.server.bank.yunst.response.result.*;
import com.rfchina.wallet.server.bank.yunst.util.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.TimeZone;
import java.util.UUID;

@Slf4j
@Component
@Import(YunstTpl.class)
public class YunstUserHandler extends YunstBaseHandler {

	@Autowired
	private YunstTpl yunstTpl;

	/**
	 * 创建会员
	 */
	public Tuple<YunstCreateMemberResult,YunstMemberType> createMember(Long walletId, Byte source) throws Exception {
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

		return new Tuple<>(yunstTpl.execute(req, YunstCreateMemberResult.class),memberType);
	}

	/**
	 * 会员电子协议签约(生成前端H5 url)
	 */
	public String generateSignContractUrl(Long walletId, Byte source) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstSignContractReq req = YunstSignContractReq.builder$()
				.bizUserId(bizUserId)
				.jumpUrl(configService.getResultJumpUrl())
				.backUrl(configService.getYunstNotifybackUrl())
				.source(TERMINAL_TYPE)
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
				.protocolReqSn(UUID.randomUUID().toString().replaceAll("-",""))
				.payerId(bizUserId)
				.receiverId(configService.getYunstReceiverId())
				.protocolName(configService.getYunstBalanceProtocolName())
				.jumpUrl(configService.getResultJumpUrl())
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
	public String sendVerificationCode(Long walletId, Byte source, String phone, Integer bizType)
			throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstSMSVerificationCodeReq req = YunstSMSVerificationCodeReq.builder$()
				.bizUserId(bizUserId)
				.phone(phone)
				.verificationCodeType(bizType.longValue())
				.build();

		YunstSendVerificationCodeResult result = null;
		try {
			result = yunstTpl.execute(req, YunstSendVerificationCodeResult.class);
		} catch (CommonGatewayException e) {
			log.error("请求发送短信验证码失败,bizUserId:{},phone:{},source:{}", bizUserId, phone, source);
			return null;
		}
		return result.getBizUserId();
	}

	/**
	 * 绑定手机
	 */
	public boolean bindPhone(Long walletId, Byte source, String phone, String verificationCode) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstBindPhoneReq req = YunstBindPhoneReq.builder$()
				.bizUserId(bizUserId)
				.phone(phone)
				.verificationCode(verificationCode)
				.build();

		YunstBindPhoneResult result = null;
		try {
			result = yunstTpl.execute(req, YunstBindPhoneResult.class);
		} catch (CommonGatewayException e) {
			log.error("绑定手机失败,bizUserId:{},phone:{}", bizUserId, phone);
			return false;
		}
		return true;
	}

	/**
	 * 修改绑定手机
	 */
	public boolean modifyPhone(Long walletId, Byte source, String oldPhone, String newPhone,
			String verificationCode) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstChangeBindPhoneReq req = YunstChangeBindPhoneReq.builder$()
				.bizUserId(bizUserId)
				.oldPhone(oldPhone)
				.newPhone(newPhone)
				.newVerificationCode(verificationCode)
				.build();

		YunstModifyPhoneResult result = null;
		try {
			result = yunstTpl.execute(req, YunstModifyPhoneResult.class);
		} catch (CommonGatewayException e) {
			log.error("修改绑定手机失败,bizUserId:{},oldPhone:{},newPhone:{}", bizUserId, oldPhone, newPhone);
			return false;
		}
		return true;
	}

	/**
	 * 获取会员信息
	 */
	public Object getMemberInfo(Long walletId, Byte source) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstGetMemberInfoReq req = YunstGetMemberInfoReq.builder$().bizUserId(bizUserId).build();

		YunstMemberInfoResult memberInfoResult = null;
		try {
			memberInfoResult = yunstTpl.execute(req, YunstMemberInfoResult.class);

			long memberType = memberInfoResult.getMemberType();
			if (memberType == YunstMemberType.COMPANY.getValue()) {
				return JsonUtil.toObject(JsonUtil.toJSON(memberInfoResult.getMemberInfo()),
						YunstMemberInfoResult.CompanyInfoResult.class, objectMapper -> {
							objectMapper.setTimeZone(TimeZone.getDefault());
							objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						});
			} else {
				return JsonUtil.toObject(JsonUtil.toJSON(memberInfoResult.getMemberInfo()),
						YunstMemberInfoResult.PersonInfoResult.class, objectMapper -> {
							objectMapper.setTimeZone(TimeZone.getDefault());
							objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						});
			}
		} catch (CommonGatewayException e) {
			log.error("获取会员信息,bizUserId:{}", bizUserId);
			return null;
		}
	}

	/**
	 * 个人实名认证
	 */
	public boolean personCertification(Long walletId, Byte source, String realName, Long identityType,
			String identityNo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstPersonSetRealNameReq req = YunstPersonSetRealNameReq.builder$()
				.bizUserId(bizUserId)
				.isAuth(true)
				.name(realName)
				.identityType(identityType)
				.identityNo(identityNo)
				.build();

		YunstPersonSetRealNameResult result = null;
		try {
			result = yunstTpl.execute(req, YunstPersonSetRealNameResult.class);
		} catch (CommonGatewayException e) {
			log.error("个人实名验证失败,bizUserId:{}", bizUserId);
			return false;
		}
		return true;
	}

	/**
	 * 设置企业信息
	 */
	public YunstSetCompanyInfoResult setCompanyInfo(Long walletId, Byte source, Boolean isAuth,
			YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstSetCompanyInfoReq.YunstSetCompanyInfoReqBuilder builder = YunstSetCompanyInfoReq.builder$()
				.bizUserId(bizUserId)
				.isAuth(isAuth)
				.companyBasicInfo(companyBasicInfo);
		if (!isAuth){
			builder.backUrl(configService.getYunstNotifybackUrl());
		}

		return yunstTpl.execute(builder.build(), YunstSetCompanyInfoResult.class);
	}

	/**
	 * 申请绑定银行卡
	 */
	public YunstApplyBindBankCardResult applyBindBankCard(Long walletId, Byte source, String cardNo, String name,
			String phone, Long identityType, String identityNo, String validate, String cvv2) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstApplyBindBankCardReq.YunstApplyBindBankCardReqBuilder buider = YunstApplyBindBankCardReq.builder$()
				.bizUserId(bizUserId)
				.cardNo(cardNo)
				.phone(phone)
				.name(name)
				.identityType(identityType)
				.identityNo(identityNo)
				.cardCheck(BIND_CARD_TYPE)
				.validate(validate);
		if (StringUtils.isNotBlank(cvv2)) {
			buider.cvv2(cvv2);
		} else {
			buider.isSafeCard(false);
		}

		return yunstTpl.execute(buider.build(), YunstApplyBindBankCardResult.class);
	}

	/**
	 * 确认绑定银行卡
	 */
	public YunstBindBankCardResult bindBankCard(Long walletId, Byte source, String tranceNum, String phone,
			String validate, String cvv2, String verificationCode) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstBindBankCardReq.YunstBindBankCardReqBuilder buider = YunstBindBankCardReq.builder$()
				.bizUserId(bizUserId)
				.tranceNum(tranceNum)
				.phone(phone)
				.validate(validate)
				.verificationCode(verificationCode);
		if (StringUtils.isNotBlank(cvv2)) {
			buider.cvv2(cvv2);
		}

		return yunstTpl.execute(buider.build(), YunstBindBankCardResult.class);
	}

	/**
	 * 解除绑定银行卡
	 */
	public YunstUnBindBankCardResult unbindBankCard(Long walletId, Byte source, String cardNo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstUnBindBankCardReq req = YunstUnBindBankCardReq.builder$().bizUserId(bizUserId).cardNo(cardNo).build();

		return yunstTpl.execute(req, YunstUnBindBankCardResult.class);
	}

}
