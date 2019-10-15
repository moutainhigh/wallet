package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.NotifyService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NotifyController {

	@Autowired
	private NotifyService notifyService;

	@RequestMapping(value = UrlConstant.YUNST_NOTIFY)
	@SuppressWarnings("unchecked")
	public ResponseValue<String> receiveYunstNotify(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>();
		request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
		notifyService.yunstNotify(params);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS.getValue(), "Receive Yunst notify finished");
	}
}
