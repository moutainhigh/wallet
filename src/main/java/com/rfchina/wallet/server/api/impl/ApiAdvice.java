package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.common.ApiInvoke;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.passport.token.TokenUtil;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamVerify;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.annotation.validator.ParamValidateUtil;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.utils.SignUtil;
import com.rfchina.platform.spring.AopLogUtil;
import java.lang.reflect.Method;

import com.rfchina.wallet.server.service.ConfigService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ApiAdvice {
	private final static Logger LOGGER = LoggerFactory.getLogger(ApiAdvice.class);

	@Autowired
	private SessionThreadLocal sessionThreadLocal = null;

	@Autowired
	private ConfigService configService;

	/**
	 * 参数验证
	 */
	@Before(value = "execution(public * com.rfchina.wallet.server.api..*.*(..))  && @annotation"
		+ "(paramVerify)")
	public void verify(JoinPoint jp, ParamVerify paramVerify) {
		if (paramVerify.verifyParam()) {
			//请求参数检测
			Method method;
			try {
				method = jp.getTarget().getClass().getMethod((jp.getSignature()).getName(),
					((MethodSignature) jp.getSignature()).getParameterTypes());
			} catch (NoSuchMethodException e) {
				LOGGER.error("wallet mch doesn't have such method error.", e);
				throw new RfchinaResponseException(
					ResponseCode.EnumResponseCode.COMMON_RESOURCE_NOT_FOUND);
			}
			ParamValidateUtil.validate(method, jp.getArgs());
		}
	}

	@Before(value =
			"execution(public * com.rfchina.wallet.server.api..*.*(String,..)) && args(accessToken,..) && "
					+ "@annotation" + "(signVerify)")
	public void verify(SignVerify signVerify, String accessToken) {
		//验证签名
		if (configService.isSignEnable() && signVerify.verifySign()) {
			if (sessionThreadLocal.getApp() == null) {
				sessionThreadLocal.addApp(ApiInvoke.queryApp(configService.getAppBaseUrl(),
						TokenUtil.decryptToken(accessToken).getSourceAppId()).getData());
			}
			//验证请求参数签名, 比较时间戳是否过期，目前定义为2分钟内有效
			SignUtil.verifySign(sessionThreadLocal.getApp().getSecret(),
					sessionThreadLocal.getTimestamp(),
					sessionThreadLocal.getSign(), sessionThreadLocal.getRequestParameters(),
					120);
		}
	}

	/**
	 * 打印日志
	 */
	@Around(value = "execution(public * com.rfchina.wallet.server.api..*.*(..)) && @annotation(log)")
	public Object logAroundApiInvoke(ProceedingJoinPoint jp, Log log) throws Throwable {
		//写日志
		return log.log() ? AopLogUtil.logWithAround(sessionThreadLocal.getRequestPath(), jp)
			: jp.proceed();
	}
}
