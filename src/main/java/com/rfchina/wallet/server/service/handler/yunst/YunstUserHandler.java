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

@Slf4j
@Component
@Import(YunstTpl.class)
public class YunstUserHandler extends YunstBaseHandler {

	@Autowired
	private YunstTpl yunstTpl;

	/**
	 * 创建会员
	 */
	public Tuple<YunstCreateMemberResult,YunstMemberType> createMember(String bizUserId, Integer type) throws Exception {
		YunstMemberType memberType = YunstMemberType.COMPANY;
		if (type == 2) {
			memberType = YunstMemberType.PERSON;
		}
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
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
	public String generateSignContractUrl(String bizUserId, Integer type) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstSignContractReq req = YunstSignContractReq.builder$()
				.bizUserId(bizUserId)
				.jumpUrl(configService.getResultJumpUrl())
				.backUrl(configService.getYunstNotifybackUrl())
				.source(TERMINAL_TYPE)
				.build();
		String res = yunstTpl.signRequest(req);
		String webParamUrl = configService.getSignContractUrl() + "?" + res;
		log.info("webParamUrl: " + webParamUrl);
		return webParamUrl;
	}

	/**
	 * 发送短信验证码
	 */
	public boolean sendVerificationCode(String bizUserId, Integer type, String phone, Integer bizType)
			throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstSMSVerificationCodeReq req = YunstSMSVerificationCodeReq.builder$()
				.bizUserId(bizUserId)
				.phone(phone)
				.verificationCodeType(bizType.longValue())
				.build();

		YunstSendVerificationCodeResult result = null;
		try {
			result = yunstTpl.execute(req, YunstSendVerificationCodeResult.class);
		} catch (CommonGatewayException e) {
			log.error("请求发送短信验证码失败,bizUserId:{},phone:{},bizType:{}", bizUserId, phone, bizType);
			return false;
		}
		return true;
	}

	/**
	 * 绑定手机
	 */
	public boolean bindPhone(String bizUserId, Integer type, String phone, String verificationCode) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
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
	public boolean modifyPhone(String bizUserId, Integer type, String oldPhone, String newPhone,
			String verificationCode) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
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
	public Object getMemberInfo(String bizUserId, Integer type) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
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
	public boolean personCertification(String bizUserId, Integer type, String realName, Long identityType,
			String identityNo) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
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
	public YunstSetCompanyInfoResult setCompanyInfo(String bizUserId, Integer type, Boolean isAuth,
			YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstSetCompanyInfoReq req = YunstSetCompanyInfoReq.builder$()
				.bizUserId(bizUserId)
				.isAuth(isAuth)
				.backUrl(configService.getYunstNotifybackUrl())
				.companyBasicInfo(companyBasicInfo)
				.build();

		return yunstTpl.execute(req, YunstSetCompanyInfoResult.class);
	}

	/**
	 * 申请绑定银行卡
	 */
	public YunstApplyBindBankCardResult applyBindBankCard(String bizUserId, Integer type, String cardNo, String name,
			String phone, Long identityType, String identityNo, String validate, String cvv2) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
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
	public YunstBindBankCardResult bindBankCard(String bizUserId, Integer type, String tranceNum, String phone,
			String validate, String cvv2, String verificationCode) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
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
	public YunstUnBindBankCardResult unbindBankCard(String bizUserId, Integer type, String cardNo) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstUnBindBankCardReq req = YunstUnBindBankCardReq.builder$().bizUserId(bizUserId).cardNo(cardNo).build();

		return yunstTpl.execute(req, YunstUnBindBankCardResult.class);
	}

}
