package com.rfchina.wallet.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.platform.common.json.ObjectSetter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.WalletChannelSignContract;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.server.bank.yunst.response.YunstNotify;
import com.rfchina.wallet.server.bank.yunst.response.RecallResp;
import com.rfchina.wallet.server.bank.yunst.response.RpsResp;
import com.rfchina.wallet.server.bank.yunst.response.RpsResp.RpsValue;
import com.rfchina.wallet.server.mapper.ext.ChannelNotifyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyType;
import com.rfchina.wallet.server.msic.EnumWallet.YunstMethodName;
import com.rfchina.wallet.server.msic.EnumWallet.YunstServiceName;
import java.util.TimeZone;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class NotifyService {

	public static final String YUNST_NOTIFY_SUCCESS = "OK";
	@Autowired
	private WalletChannelExtDao walletChannelExtDao;
	@Autowired
	private ChannelNotifyExtDao channelNotifyDao;
	@Autowired
	private YunstBizHandler yunstBizHandler;

	public ChannelNotify yunstNotify(Map<String, String> params) {
		String json = JsonUtil.toJSON(params);
		log.info("Yunst notify: {}", json);

		ChannelNotify channelNotify = ChannelNotify.builder()
			.channelType(EnumDef.ChannelType.YUNST.getValue().intValue())
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
				if (YUNST_NOTIFY_SUCCESS.equals(yunstNotify.getStatus())) {
					String rtnValJson = JsonUtil.toJSON(yunstNotify.getReturnValue());
					YunstNotify.CompanyAuditResult rtnVal = JsonUtil
						.toObject(rtnValJson, YunstNotify.CompanyAuditResult.class,
							getObjectMapper());
					this.handleVerfiyResult(channelNotify, rtnVal);
				}

			} else if (YunstMethodName.SIGN_CONTRACT.getValue().equals(methodName)) {
				if (YUNST_NOTIFY_SUCCESS.equals(yunstNotify.getStatus()) && StringUtils
					.isNotBlank(yunstNotify.getContractNo())) {
					String rtnValJson = JsonUtil.toJSON(yunstNotify.getReturnValue());
					YunstNotify.SignContractResult rtnVal = JsonUtil
						.toObject(rtnValJson, YunstNotify.SignContractResult.class,
							getObjectMapper());
					this.handleSignContractResult(channelNotify, rtnVal);
				}

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
					this.handleChangeBindPhoneResult(channelNotify, rtnVal);
				}

			}
		} else {
			log.error("云商通回调,service:{}", service);
		}
		channelNotifyDao.updateByPrimaryKeySelective(channelNotify);
		return channelNotify;
	}

	private void handleVerfiyResult(ChannelNotify channelNotify,
		YunstNotify.CompanyAuditResult rtnVal) {
		log.info("处理企业信息审核结果通知");

		String bizUserId = rtnVal.getBizUserId();
		String checkTime = rtnVal.getCheckTime();
		long result = rtnVal.getResult();
		String failReason = rtnVal.getFailReason();
		;
		String remark = rtnVal.getRemark();

		channelNotify.setBizUserId(bizUserId);

		WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
			EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

		if (result == 2L) {
			walletChannel.setStatus(
				EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
			walletChannel.setFailReason(null);
		} else if (result == 3L) {
			walletChannel
				.setStatus(EnumDef.WalletChannelAuditStatus.AUDIT_FAIL.getValue().byteValue());
			walletChannel.setFailReason(failReason);
		}
		walletChannel.setRemark(remark);
		walletChannel
			.setCheckTime(DateUtil.parse(checkTime, DateUtil.STANDARD_DTAETIME_PATTERN));

		int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理企业信息审核结果通知-更新审核状态状态失败:bizUserId:{}", bizUserId);
		}

	}

	private void handleSignContractResult(ChannelNotify channelNotify,
		YunstNotify.SignContractResult rtnVal) {
		log.info("处理会员电子签约通知");
		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);
		WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
			EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setIsSignContact(WalletChannelSignContract.MEMBER.getValue().byteValue());

		int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("更新电子签约状态失败:bizUserId:{}", bizUserId);
		}
	}


	private void handleChangeBindPhoneResult(ChannelNotify channelNotify,
		YunstNotify.UpdatePhoneResult rtnVal) {
		log.info("处理个人会员修改绑定手机通知");
		String bizUserId = rtnVal.getBizUserId();
		String newPhone = rtnVal.getNewPhone();
		channelNotify.setBizUserId(bizUserId);
		WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
			EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setSecurityTel(newPhone);

		int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理个人会员修改绑定手机失败:bizUserId:{},newPhone:{}", bizUserId, newPhone);
		}
	}

	private ObjectSetter<ObjectMapper> getObjectMapper() {
		return objectMapper -> {
			objectMapper.setTimeZone(TimeZone.getDefault());
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		};
	}

//	public static void main(String[] args) {
//		String s = "{\"returnValue\":{\"result\":2,\"checkTime\":\"2019-09-25 14:56:09\",\"bizUserId\":\"SILI001\"},\"method\":\"verifyResult\",\"service\":\"MemberService\"}";
//
//		System.out.println(s);
//		YunstNotify yunstNotify = JsonUtil.toObject(s, YunstNotify.class, objectMapper -> {
//			objectMapper.setTimeZone(TimeZone.getDefault());
//			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		});
//		System.out.println(yunstNotify);
//		String s2 = JsonUtil.toJSON(yunstNotify.getReturnValue());
//		System.out.println(s2);
//		YunstNotify.CompanyAuditResult result = JsonUtil
//			.toObject(s2, YunstNotify.CompanyAuditResult.class, objectMapper -> {
//				objectMapper.setTimeZone(TimeZone.getDefault());
//				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			});
//		System.out.println(result);
//	}
	public void handleOrderResult(ChannelNotify channelNotify, WalletApplyType type) {

		RecallResp recallResp = JsonUtil.toObject(channelNotify.getContent(), RecallResp.class,
			objectMapper -> {
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			});
		RpsResp rpsResp = JsonUtil.toObject(recallResp.getRps(), RpsResp.class, objectMapper -> {
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		});

		if (WalletApplyType.RECHARGE == type) {
			yunstBizHandler.updateRechargeStatus(rpsResp.getReturnValue().getBizOrderNo());
		} else if (WalletApplyType.COLLECT == type) {
			yunstBizHandler.updateCollectStatus(rpsResp.getReturnValue().getBizOrderNo());
		} else if (WalletApplyType.AGENT_PAY == type) {
			yunstBizHandler.updateAgentPayStatus(rpsResp.getReturnValue().getBizOrderNo());
		}
	}


}
