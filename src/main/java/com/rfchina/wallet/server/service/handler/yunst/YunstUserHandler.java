package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.platform.common.utils.Valuable;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.server.bank.yunst.request.YunstBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstChangeBindPhoneReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstCreateMemberReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstSMSVerificationCodeReq;
import com.rfchina.wallet.server.bank.yunst.request.*;
import com.rfchina.wallet.server.bank.yunst.response.YunstMemberInfoResp;
import com.rfchina.wallet.server.bank.yunst.response.YunstBaseResp;
import com.rfchina.wallet.server.bank.yunst.response.YunstCreateMemberResp;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Import(YunstTpl.class)
public class YunstUserHandler {

	public static final Long TERMINAL_TYPE = 2L; // 终端类型 2-PC
	public static final String MEMBER_TYPE_PREFIX_PERSON = "U";
	public static final String MEMBER_TYPE_PREFIX_COMPANY = "C";

	@Autowired
	private ConfigService configService;

	@Autowired
	private YunstTpl yunstTpl;

	@PostConstruct
	public void init() {
		YunClient.configure(new YunConfig(configService.getYstServerUrl(), configService.getYstSysId(),
				configService.getYstPassword(), configService.getYstAlias(), configService.getYstVersion(),
				configService.getYstPfxPath(), configService.getYstTlCertPath()));
	}

	/**
	 * 创建会员
	 */
	public YunstCreateMemberResp createMember(String bizUserId, Integer type) throws Exception {
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

		return YunstCreateMemberResp.builder()
				.data(yunstTpl.execute(req, YunstCreateMemberResp.CreateMemeberResult.class))
				.build();
	}

	/**
	 * 发送短信验证码
	 */
	public Tuple<Boolean, String> sendVerificationCode(String bizUserId, Integer type, String phone, Integer bizType)
			throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstSMSVerificationCodeReq req = YunstSMSVerificationCodeReq.builder$()
				.bizUserId(bizUserId)
				.phone(phone)
				.verificationCodeType(bizType.longValue())
				.build();

		YunstBaseResp resp = yunstTpl.execute(req, YunstBaseResp.class);
		if (YunstBaseRespStatus.SUCCESS.getValue().equals(resp.status)) {
			return new Tuple<>(true, null);
		}
		return new Tuple<>(false, resp.getMessage());
	}

	/**
	 * 绑定手机
	 */
	public Tuple<Boolean, String> bindPhone(String bizUserId, Integer type, String phone, String verificationCode)
			throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstBindPhoneReq req = YunstBindPhoneReq.builder$()
				.bizUserId(bizUserId)
				.phone(phone)
				.verificationCode(verificationCode)
				.build();

		YunstBaseResp resp = yunstTpl.execute(req, YunstBaseResp.class);
		if (YunstBaseRespStatus.SUCCESS.getValue().equals(resp.status)) {
			return new Tuple<>(true, null);
		}
		return new Tuple<>(false, resp.getMessage());
	}

	/**
	 * 修改绑定手机
	 */
	public Tuple<Boolean, String> modifyPhone(String bizUserId, Integer type, String oldPhone, String newPhone,
			String verificationCode) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstChangeBindPhoneReq req = YunstChangeBindPhoneReq.builder$()
				.bizUserId(bizUserId)
				.oldPhone(oldPhone)
				.newPhone(newPhone)
				.newVerificationCode(verificationCode)
				.build();

		YunstBaseResp resp = yunstTpl.execute(req, YunstBaseResp.class);
		if (YunstBaseRespStatus.SUCCESS.getValue().equals(resp.status)) {
			return new Tuple<>(true, null);
		}
		return new Tuple<>(false, resp.getMessage());
	}

	/**
	 * 获取会员信息
	 */
	public YunstMemberInfoResp getMemberInfo(String bizUserId, Integer type) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstGetMemberInfoReq req = YunstGetMemberInfoReq.builder$().bizUserId(bizUserId).build();

		return yunstTpl.execute(req, YunstMemberInfoResp.class);
	}

	public enum YunstBaseRespStatus implements Valuable<String> {
		SUCCESS("OK"), ERROR("error");

		private String value;

		YunstBaseRespStatus(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	public enum YunstMemberType implements Valuable<Long> {
		COMPANY(2L, "企业会员"), PERSON(3L, "个人会员");

		private Long value;
		private String valueName;

		YunstMemberType(Long value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Long getValue() {
			return value;
		}

		public String getValueName() {
			return valueName;
		}
	}

	private String transferToYunstBizUserFormat(String bizUserId, Integer type) {
		if (type != 1 && type != 2) {
			throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_INVALID_PARAMS, "type");
		}
		if (type == 2) {
			return MEMBER_TYPE_PREFIX_PERSON + bizUserId;
		} else {
			return MEMBER_TYPE_PREFIX_COMPANY + bizUserId;
		}
	}
}
