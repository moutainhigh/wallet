package com.rfchina.wallet.server.service;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.server.bank.yunst.response.YunstNotify;
import com.rfchina.wallet.server.mapper.ext.ChannelNotifyExtDao;
import com.rfchina.wallet.server.msic.EnumYunst.YunstMethodName;
import com.rfchina.wallet.server.msic.EnumYunst.YunstServiceName;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
import com.rfchina.wallet.server.service.handler.yunst.YunstNotifyHandler;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class NotifyService {

	public static final String YUNST_NOTIFY_SUCCESS = "OK";
	@Autowired
	private ChannelNotifyExtDao channelNotifyDao;
	@Autowired
	private YunstNotifyHandler yunstNotifyHandler;
	@Autowired
	private YunstBizHandler yunstBizHandler;


	public ChannelNotify yunstNotify(Map<String, String> params) {
		String json = JsonUtil.toJSON(params);
		log.info("Yunst notify: {}", json);

		if (!veryfySign(params)) {
			throw new WalletResponseException(EnumResponseCode.COMMON_FAILURE, "verify sign");
		}
		ChannelNotify channelNotify = ChannelNotify.builder()
			.channelType(TunnelType.YUNST.getValue().intValue())
			.content(json)
			.createTime(new Date())
			.build();
		channelNotifyDao.insertSelective(channelNotify);

		YunstNotify yunstNotify = JsonUtil
			.toObject(params.get("rps"), YunstNotify.class, getObjectMapper());
		String service = yunstNotify.getService();
		String methodName = yunstNotify.getMethod();

		if (StringUtils.isBlank(service) && StringUtils.isBlank(methodName)) {
			log.error("云商通回调参数有误,缺少service 或 method");
			return channelNotify;
		}
		channelNotify.setYunstServiceName(service);
		channelNotify.setYunstMethodName(methodName);
		if (YunstServiceName.MEMBER.getValue().equals(service)) {
			if (YunstMethodName.VERIFY_RESULT.getValue().equals(methodName)) {
				String rtnValJson = JsonUtil.toJSON(yunstNotify.getReturnValue());
				YunstNotify.CompanyAuditResult rtnVal = JsonUtil
					.toObject(rtnValJson, YunstNotify.CompanyAuditResult.class,
						getObjectMapper());
				yunstNotifyHandler.handleVerfiyResult(channelNotify, rtnVal);

			} else if (YunstMethodName.SIGN_CONTRACT.getValue().equals(methodName)) {
				if (YUNST_NOTIFY_SUCCESS.equals(yunstNotify.getStatus()) && StringUtils
					.isNotBlank(yunstNotify.getContractNo())) {
					String rtnValJson = JsonUtil.toJSON(yunstNotify.getReturnValue());
					YunstNotify.SignContractResult rtnVal = JsonUtil
						.toObject(rtnValJson, YunstNotify.SignContractResult.class,
							getObjectMapper());
					yunstNotifyHandler.handleSignContractResult(channelNotify, rtnVal);
				}
			} else if (YunstMethodName.SIGN_BALANCE_PROTOCOL.getValue().equals(methodName)) {
				String rtnValJson = JsonUtil.toJSON(yunstNotify.getReturnValue());
				YunstNotify.BalanceContractResult rtnVal = JsonUtil
					.toObject(rtnValJson, YunstNotify.BalanceContractResult.class,
						getObjectMapper());
				yunstNotifyHandler.handleSignBalanceContractResult(channelNotify, rtnVal);

			} else {
				log.error("云商通回调,未知method参数:{}", methodName);
			}
		} else if (YunstServiceName.ORDER.getValue().equals(service)) {
			
		} else if (YunstServiceName.MEMBER_PWD.getValue().equals(service)) {
			if (YunstMethodName.CHANGE_BIND_PHONE.getValue().equals(methodName)) {
				if (YUNST_NOTIFY_SUCCESS.equals(yunstNotify.getStatus())) {
					String rtnValJson = JsonUtil.toJSON(yunstNotify.getReturnValue());
					YunstNotify.UpdatePhoneResult rtnVal = JsonUtil
						.toObject(rtnValJson, YunstNotify.UpdatePhoneResult.class,
							getObjectMapper());
					yunstNotifyHandler.handleChangeBindPhoneResult(channelNotify, rtnVal);

				}
			} else if (YunstMethodName.SET_PAY_PWD.getValue().equals(methodName)) {
				if (YUNST_NOTIFY_SUCCESS.equals(yunstNotify.getStatus())) {
					String rtnValJson = JsonUtil.toJSON(yunstNotify.getReturnValue());
					YunstNotify.SetPayPwd rtnVal = JsonUtil
						.toObject(rtnValJson, YunstNotify.SetPayPwd.class,
							getObjectMapper());
					yunstNotifyHandler.handleSetPayPwdResult(channelNotify, rtnVal);
				}
			}
		} else {
			log.error("云商通回调,service:{}", service);
		}
		channelNotifyDao.updateByPrimaryKeySelective(channelNotify);
		return channelNotify;
	}


	private ObjectSetter<ObjectMapper> getObjectMapper() {
		return objectMapper -> {
			objectMapper.setTimeZone(TimeZone.getDefault());
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		};
	}

	//
	private boolean veryfySign(Map<String, String> params) {
		String sysid = params.get("sysid");
		String rps = params.get("rps");
		String timestamp = params.get("timestamp");
		String sign = params.get("sign");
		try {
			return RSAUtil.verify(sysid + rps + timestamp, sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
