package com.rfchina.wallet.server.bank.yunst.util;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
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
import com.rfchina.wallet.server.msic.EnumYunst.YunstMethodName;
import com.rfchina.wallet.server.service.CacheService;
import com.rfchina.wallet.server.service.ConfigService;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class YunstTpl {

	public static final String RESP_OK = "OK";

	@Autowired
	private GatewayLogMapper gatewayLogDao;

	@Autowired
	private CacheService cacheService;

	@Autowired
	ConfigService configService;

	private YunConfig config;

	@PostConstruct
	public void tryInit() {
		if (config == null) {
			synchronized (YunstTpl.class) {
				if (config == null) {
					forceInit();
				}
			}
		}
	}

	public void forceInit() {

		YunConfig newCfg = new YunConfig(
			configService.getYstServerUrl(),
			configService.getYstSysId(),
			configService.getYstPassword(),
			configService.getYstAlias(),
			configService.getYstVersion(),
			configService.getYstPfxPath(),
			configService.getYstTlCertPath());

		try {
			log.info("初始化Yunst serverUrl={}, sysId={}, version={}, pfxPath={}, tlCertPath={} ",
				configService.getYstServerUrl(), configService.getYstSysId(),
				configService.getYstVersion(), configService.getYstPfxPath(),
				configService.getYstTlCertPath());
			YunClient.configure(newCfg);
		} catch (Exception e) {
			log.error("", e);
		}

		config = newCfg;
	}


	public <T extends YunstBaseReq, R> R execute(T reqBody, Class<R> respClz) throws Exception {
		tryInit();
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
		tryInit();
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
			Boolean isAuth = reqPkg.get("isAuth") != null ? (Boolean) reqPkg.get("isAuth") : null;
			GatewayLog gatewayLog = GatewayLog.builder()
				.tunnelType(TunnelType.YUNST.getValue())
				.bizUserId((String) Optional.ofNullable(reqPkg.get("bizUserId")).orElse(null))
				.bizOrderNo((String) Optional.ofNullable(reqPkg.get("bizOrderNo")).orElse(null))
				.serviceName(reqPkg.getService())
				.methodName(reqPkg.getMethod())
				.traceId(MDC.get("traceId"))
				.invokeStatus(status.getValue())
				.invokeTime(new Date())
				.isAuth(isAuth != null ? Integer.valueOf(isAuth ? 1 : 0).byteValue() : null)
				.req(JsonUtil.toJSON(reqPkg))
				.resp(respBody)
				.build();
			gatewayLogDao.insertSelective(gatewayLog);

			if (YunstMethodName.PERSON_VERIFY.getValue().equals(gatewayLog.getMethodName())
				||
				YunstMethodName.COMPANY_VERIFY.getValue().equals(gatewayLog.getMethodName())
					&& gatewayLog.getIsAuth() == (byte) 1) {
				cacheService.statisticsYunstVerify(gatewayLog.getMethodName());
			}
		} catch (Exception e) {
			log.error("保存网关日志失败", e);
		}
	}

}
