package com.rfchina.wallet.server.bank.yunst.util;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.server.bank.yunst.request.YunstBaseReq;
import com.rfchina.wallet.server.bank.yunst.response.YunstBaseResp;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YunstTpl {

	public static final String RESP_OK = "OK";

	public <T extends YunstBaseReq, R> R execute(T reqBody, Class<R> respClz)
		throws Exception {
		// 封装请求
		YunRequest reqPkg = new YunRequest(reqBody.getServcieName(), reqBody.getMethodName());
		Map<String, Object> reqMap = JsonUtil.toMap(reqBody, false);
		reqMap.forEach((key, val) -> reqPkg.put(key, val));

		// 发送请求
		String respPkg = YunClient.request(reqPkg);

		// 解析结果
		YunstBaseResp resp = JsonUtil
			.toObject(respPkg, YunstBaseResp.class, getObjectMapper());
		if (RESP_OK.equals(resp.getStatus())) {
			return respClz != YunstBaseResp.class ? JsonUtil
				.toObject(resp.getSignedValue(), respClz, getObjectMapper()) : (R) resp;
		} else {
			log.error("通联接口错误, request = {} , response = {}", reqBody, resp);
			throw new CommonGatewayException(EnumWalletResponseCode.PAY_IN_GATEWAY_RESPONSE_ERROR,
				resp.getErrorCode(), resp.getMessage());
		}
	}

	private ObjectSetter<ObjectMapper> getObjectMapper() {
		return objectMapper -> {
			objectMapper.setTimeZone(TimeZone.getDefault());
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		};
	}

}
