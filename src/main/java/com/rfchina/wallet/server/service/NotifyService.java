package com.rfchina.wallet.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.WalletChannelSignContract;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.server.mapper.ext.ChannelNotifyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.YunstMethodName;
import com.rfchina.wallet.server.msic.EnumWallet.YunstServiceName;
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

	public void yunstNotify(Map<String, String> params) {
		String json = JsonUtil.toJSON(params);
		log.info("Yunst notify: {}", json);

		ChannelNotify channelNotify = ChannelNotify.builder()
			.channelType(EnumDef.ChannelType.YUNST.getValue().intValue())
			.content(json)
			.createTime(new Date())
			.build();
		channelNotifyDao.insertSelective(channelNotify);

		JSONObject rpsJsonObj = JSON.parseObject(params.get("rps"));
		String service = rpsJsonObj.getString("service");
		String methodName = rpsJsonObj.getString("method");

		if (StringUtils.isBlank(service) && StringUtils.isBlank(methodName)) {
			log.error("云商通回调参数有误,缺少service 或 method");
			return;
		}
		channelNotify.setYunstServiceName(service);
		channelNotify.setYunstMethodName(methodName);
		if (YunstServiceName.MEMBER.getValue().equals(service)) {
			if (YunstMethodName.VERIFY_RESULT.getValue().equals(methodName)) {
				this.handleVerfiyResult(channelNotify, rpsJsonObj);
			} else if (YunstMethodName.SIGN_CONTRACT.getValue().equals(methodName)) {
				this.handleSignContractResult(channelNotify, rpsJsonObj);
			} else {
				log.error("云商通回调,未知method参数:{}", methodName);
			}
		} else if (YunstServiceName.ORDER.getValue().equals(service)) {

		} else if (YunstServiceName.MEMBER_PWD.getValue().equals(service)) {
			if (YunstMethodName.CHANGE_BIND_PHONE.getValue().equals(methodName)) {
				this.handleChangeBindPhoneResult(channelNotify, rpsJsonObj);
			}
		} else {
			log.error("云商通回调,service:{}", service);
		}
		channelNotifyDao.updateByPrimaryKeySelective(channelNotify);
		return;
	}

	private void handleVerfiyResult(ChannelNotify channelNotify, JSONObject rpsJsonObj) {
		log.info("处理企业信息审核结果通知");
		if (YUNST_NOTIFY_SUCCESS.equals(rpsJsonObj.getString("status"))) {
			String bizUserId = rpsJsonObj.getJSONObject("returnValue").getString("bizUserId");
			String checkTime = rpsJsonObj.getJSONObject("returnValue").getString("checkTime");
			long result = rpsJsonObj.getJSONObject("returnValue").getLongValue("result");
			String failReason = rpsJsonObj.getJSONObject("returnValue").getString("failReason");
			String remark = rpsJsonObj.getJSONObject("returnValue").getString("remark");

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
	}

	private void handleSignContractResult(ChannelNotify channelNotify, JSONObject rpsJsonObj) {
		log.info("处理会员电子签约通知");
		if (rpsJsonObj.containsKey("ContractNo") && YUNST_NOTIFY_SUCCESS
			.equals(rpsJsonObj.getString("status"))) {
			String bizUserId = rpsJsonObj.getJSONObject("returnValue").getString("bizUserId");
			channelNotify.setBizUserId(bizUserId);
			WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
				EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

			walletChannel.setIsSignContact(WalletChannelSignContract.MEMBER.getValue().byteValue());

			int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
			if (effectRows != 1) {
				log.error("更新电子签约状态失败:bizUserId:{},contractNo:{}", bizUserId,
					rpsJsonObj.getString("ContractNo"));
			}
		}
	}

	private void handleChangeBindPhoneResult(ChannelNotify channelNotify, JSONObject rpsJsonObj) {
		log.info("处理个人会员修改绑定手机通知");
		if (YUNST_NOTIFY_SUCCESS.equals(rpsJsonObj.getString("status"))) {
			String bizUserId = rpsJsonObj.getJSONObject("returnValue").getString("bizUserId");
			String newPhone = rpsJsonObj.getJSONObject("returnValue").getString("newPhone");
			channelNotify.setBizUserId(bizUserId);
			WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
				EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

			walletChannel.setSecurityTel(newPhone);

			int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
			if (effectRows != 1) {
				log.error("处理个人会员修改绑定手机失败:bizUserId:{},newPhone:{}", bizUserId, newPhone);
			}
		}
	}


}
