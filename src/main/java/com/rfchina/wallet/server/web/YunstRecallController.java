package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.server.bank.yunst.response.RecallResp;
import com.rfchina.wallet.server.bank.yunst.response.RpsResp;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.NotifyService;
import com.rfchina.wallet.server.service.SeniorPayService;
import com.rfchina.wallet.server.service.handler.yunst.YunstNotifyHandler;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YunstRecallController {

	@Autowired
	private NotifyService notifyService;
	@Autowired
	private YunstNotifyHandler yunstNotifyHandler;
	@Autowired
	private SeniorPayService seniorPayService;

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
	@RequestMapping(value = UrlConstant.YUNST_ORDER_RECALL)
	public ResponseValue<String> orderRecall(HttpServletRequest request) {

		Map<String, String> params = request.getParameterMap().entrySet().stream().collect(
			Collectors.toMap(entry -> entry.getKey(),
				entry -> entry.getValue().length > 0 ? entry.getValue()[0] : null));

		ChannelNotify channelNotify = notifyService.yunstNotify(params);
		RecallResp recallResp = JsonUtil.toObject(channelNotify.getContent(), RecallResp.class,
			objectMapper -> {
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			});
		RpsResp rpsResp = JsonUtil.toObject(recallResp.getRps(), RpsResp.class, objectMapper -> {
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		});

		seniorPayService.updateOrderStatusWithMq(rpsResp.getReturnValue().getBizOrderNo(), false);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS.getValue(),
			"Receive Yunst order recall");
	}


}
