package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyType;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.NotifyService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class NotifyController {

	@Autowired
	private NotifyService notifyService;

	@RequestMapping(value = UrlConstant.YUNST_NOTIFY)
	@SuppressWarnings("unchecked")
	public ResponseValue<String> receiveYunstNotify(HttpServletRequest request) {
		Map<String, String> params = request.getParameterMap().entrySet().stream().collect(
			Collectors.toMap(entry -> entry.getKey(),
				entry -> entry.getValue().length > 0 ? entry.getValue()[0] : null));
		notifyService.yunstNotify(params);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS.getValue(),
			"Receive Yunst notify finished");
	}


	/**
	 * 云商通充值回调
	 */
	@RequestMapping(value = UrlConstant.YUNST_RECHARGE_RECALL)
	public ResponseValue<String> rechargeRecall(HttpServletRequest request){

		Map<String, String> params = request.getParameterMap().entrySet().stream().collect(
			Collectors.toMap(entry -> entry.getKey(),
				entry -> entry.getValue().length > 0 ? entry.getValue()[0] : null));

		ChannelNotify channelNotify = notifyService.yunstNotify(params);
		notifyService.handleOrderResult(channelNotify, WalletApplyType.RECHARGE);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS.getValue(),
			"Receive Yunst recharge recall");
	}

	/**
	 * 云商通代收回调
	 */
	@RequestMapping(value = UrlConstant.YUNST_COLLECT_RECALL)
	public ResponseValue<String> collectRecall(HttpServletRequest request){

		Map<String, String> params = request.getParameterMap().entrySet().stream().collect(
			Collectors.toMap(entry -> entry.getKey(),
				entry -> entry.getValue().length > 0 ? entry.getValue()[0] : null));

		ChannelNotify channelNotify = notifyService.yunstNotify(params);
		notifyService.handleOrderResult(channelNotify, WalletApplyType.COLLECT);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS.getValue(),
			"Receive Yunst collect recall");
	}

	/**
	 * 云商通代付回调
	 */
	@RequestMapping(value = UrlConstant.YUNST_AGENT_PAY_RECALL)
	public ResponseValue<String> agentPayRecall(HttpServletRequest request){

		Map<String, String> params = request.getParameterMap().entrySet().stream().collect(
			Collectors.toMap(entry -> entry.getKey(),
				entry -> entry.getValue().length > 0 ? entry.getValue()[0] : null));

		ChannelNotify channelNotify = notifyService.yunstNotify(params);
		notifyService.handleOrderResult(channelNotify, WalletApplyType.AGENT_PAY);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS.getValue(),
			"Receive Yunst agent pay recall");
	}

}
