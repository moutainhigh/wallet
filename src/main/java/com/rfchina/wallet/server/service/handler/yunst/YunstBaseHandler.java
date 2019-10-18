package com.rfchina.wallet.server.service.handler.yunst;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.utils.Valuable;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public abstract class YunstBaseHandler {
	public static final Long TERMINAL_TYPE = 2L; // 终端类型 2-PC
	public static final Long BIND_CARD_TYPE = 7L; // 绑卡方式 收银宝快捷支付签约（有银行范围） —支持收银宝快捷支付 —支持提现
	public static final String MEMBER_TYPE_PREFIX_PERSON = "WU";//个人
	public static final String MEMBER_TYPE_PREFIX_MCH = "WM";//个人商户
	public static final String MEMBER_TYPE_PREFIX_COMPANY = "WC";//企业商户

	@Autowired
	ConfigService configService;

	@PostConstruct
	public void init() {
		YunClient.configure(new YunConfig(configService.getYstServerUrl(), configService.getYstSysId(),
				configService.getYstPassword(), configService.getYstAlias(), configService.getYstVersion(),
				configService.getYstPfxPath(), configService.getYstTlCertPath()));
	}

	public static String transferToYunstBizUserFormat(Long walletId, Byte type) {
		switch (type){
		case 1:
			return MEMBER_TYPE_PREFIX_COMPANY + walletId;
		case 2:
			return MEMBER_TYPE_PREFIX_MCH + walletId;
		case 3:
			return MEMBER_TYPE_PREFIX_PERSON + walletId;
		}
		throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_INVALID_PARAMS, "type");
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
}