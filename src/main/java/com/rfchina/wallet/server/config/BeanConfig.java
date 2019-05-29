package com.rfchina.wallet.server.config;

import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.platform.spring.SpringContext;
import com.rfchina.wallet.server.bank.pudong.domain.exception.GatewayErrPredicate;

import com.rfchina.passport.misc.SessionThreadLocal;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SimpleExclusiveLock.class)
public class BeanConfig {

	@Bean
	public SpringContext springContext() {
		return new SpringContext();
	}

	@Bean
	public SessionThreadLocal sessionThreadLocal() {
		return new SessionThreadLocal();
	}

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
			.connectTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(60, TimeUnit.SECONDS)
			.build();
	}

	@Bean
	public GatewayErrPredicate gatewayErrPredicate() {
		return new GatewayErrPredicate();
	}

}
