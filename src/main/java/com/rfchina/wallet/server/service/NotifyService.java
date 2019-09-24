package com.rfchina.wallet.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.platform.common.utils.Valuable;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.server.mapper.ext.ChannelNotifyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
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
	public static final Byte YUNST_NOTIFY_SIGN_CONTRACT = (byte) 1;
	@Autowired
	private WalletChannelExtDao walletChannelExtDao;
	@Autowired
	private ChannelNotifyExtDao channelNotifyDao;

	public void yunstNotify(Map<String, String> params) {
		String json = JsonUtil.toJSON(params);
		log.info("Yunst notify: {}", json);

		channelNotifyDao.insertSelective(ChannelNotify.builder()
				.channelType(EnumDef.ChannelType.YUNST.getValue().intValue())
				.content(json)
				.createTime(new Date())
				.build());

		JSONObject rpsJsonObj = JSON.parseObject(params.get("rps"));
		String service = rpsJsonObj.getString("service");
		String methodName = rpsJsonObj.getString("method");

		if (StringUtils.isBlank(service) && StringUtils.isBlank(methodName)) {
			log.error("云商通回调参数有误,缺少service 或 method");
			return;
		}
		if (YunstServiceName.MEMBER.getValue().equals(service)) {
			if (YunstMethodName.VERIFY_RESULT.getValue().equals(methodName)) {
				this.handleVerfiyResult(rpsJsonObj);
			} else if (YunstMethodName.SIGN_CONTRACT.getValue().equals(methodName)) {
				this.handleSignContractResult(rpsJsonObj);
			} else {
				log.error("云商通回调,未知method参数:{}", methodName);
			}
		} else if (YunstServiceName.ORDER.getValue().equals(service)) {

		} else {
			log.error("云商通回调,service:{}", service);
		}

		return;
	}

	private void handleVerfiyResult(JSONObject rpsJsonObj) {
		log.info("处理企业信息审核结果通知");
	}

	private void handleSignContractResult(JSONObject rpsJsonObj) {
		log.info("处理会员电子签约通知");
		if (rpsJsonObj.containsKey("ContractNo") && YUNST_NOTIFY_SUCCESS.equals(rpsJsonObj.getString("status"))) {
			String bizUserId = rpsJsonObj.getJSONObject("returnValue").getString("bizUserId");

			WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
					EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

			walletChannel.setIsSignContact(YUNST_NOTIFY_SIGN_CONTRACT);

			int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
			if (effectRows != 1) {
				log.error("更新电子签约状态失败:bizUserId:{},contractNo:{}", bizUserId, rpsJsonObj.getString("ContractNo"));
			}
		}
	}

	public enum YunstServiceName implements Valuable<String> {
		MEMBER("MemberService"), ORDER("OrderService");

		private String value;

		YunstServiceName(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	public enum YunstMethodName implements Valuable<String> {
		VERIFY_RESULT("verifyResult"), SIGN_CONTRACT("signContract");

		private String value;

		YunstMethodName(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

}
