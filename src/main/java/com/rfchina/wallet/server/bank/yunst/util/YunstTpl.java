package com.rfchina.wallet.server.bank.yunst.util;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.GatewayLogMapper;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.GatewayLog;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.request.YunstBaseReq;
import com.rfchina.wallet.server.bank.yunst.response.YunstBaseResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayInvokeStatus;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class YunstTpl {

	@Autowired
	private GatewayLogMapper gatewayLogDao;

	public static final String RESP_OK = "OK";

	public <T extends YunstBaseReq, R> R execute(T reqBody, Class<R> respClz) throws Exception {
		// 封装请求
		YunRequest reqPkg = this.wrapRequest(reqBody);

		log.info("【通联】发送请求 {}", reqPkg);
		// 发送请求
		String respPkg = YunClient.request(reqPkg);
		log.info("【通联】接收响应 {}", respPkg);

		// 解析结果
		YunstBaseResp resp = JsonUtil.toObject(respPkg, YunstBaseResp.class, getObjectMapper());
		if (RESP_OK.equals(resp.getStatus())) {
			log2db(reqPkg, respPkg, resp, GatewayInvokeStatus.SUCC);
			return respClz != YunstBaseResp.class ? JsonUtil.toObject(resp.getSignedValue(),
				respClz, getObjectMapper()) : (R) resp;
		} else {
			log2db(reqPkg, respPkg, resp, GatewayInvokeStatus.FAIL);
			throw new CommonGatewayException(EnumWalletResponseCode.PAY_IN_GATEWAY_RESPONSE_ERROR,
				resp.getErrorCode(),
				resp.getMessage());
		}
	}

	public <T extends YunstBaseReq> String signRequest(T reqBody) {
		// 封装请求
		YunRequest reqPkg = this.wrapRequest(reqBody);
		try {
			return YunClient.encodeOnce(reqPkg);
		} catch (Exception e) {
			log.error("签名封装请求错误, request = {}", reqBody);
			throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"签名封装请求错误");
		}
	}

	private <T extends YunstBaseReq> YunRequest wrapRequest(T reqBody) {
		YunRequest reqPkg = new YunRequest(reqBody.getServcieName(), reqBody.getMethodName());
		Map<String, Object> reqMap = JsonUtil.toMap(reqBody, false);
		reqMap.forEach((key, val) -> reqPkg.put(key, val));
		return reqPkg;
	}

	private ObjectSetter<ObjectMapper> getObjectMapper() {
		return objectMapper -> {
			objectMapper.setTimeZone(TimeZone.getDefault());
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		};
	}

	private void log2db(YunRequest reqPkg, String respBody, YunstBaseResp resp,
		GatewayInvokeStatus status) {
		try {
			GatewayLog gatewayLog = GatewayLog.builder()
				.tunnelType(TunnelType.YUNST.getValue())
				.bizUserId((String) Optional.ofNullable(reqPkg.get("bizUserId")).orElse(null))
				.bizOrderNo((String) Optional.ofNullable(reqPkg.get("bizOrderNo")).orElse(null))
				.serviceName(reqPkg.getService())
				.methodName(reqPkg.getMethod())
				.traceId(MDC.get("traceId"))
				.invokeStatus(status.getValue())
				.req(JsonUtil.toJSON(reqPkg))
				.resp(respBody)
				.build();
			gatewayLogDao.insertSelective(gatewayLog);
		} catch (Exception e) {
			log.error("保存网关日志失败", e);
		}
	}

}
