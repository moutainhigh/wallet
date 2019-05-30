package com.rfchina.wallet.server.config;

import com.alibaba.fastjson.JSON;
import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.platform.spring.SpringContext;

import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import io.github.xdiamond.client.annotation.AllKeyListener;
import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import(SimpleExclusiveLock.class)
public class BeanConfig {

	@Autowired
	private ExactErrPredicate gatewayErrPredicate;

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
	public ExactErrPredicate gatewayErrPredicate(
		@Value("${wlpay.pudong.exactErr}") String exactErr) {
		ExactErrPredicate predicate = new ExactErrPredicate();
		predicate.parseText(exactErr);
		return predicate;
	}

	@OneKeyListener(key = "wlpay.pudong.exactErr")
	public void onExactErrChange(ConfigEvent event) {
		log.info("change key wlpay.pudong.exactErr, event : {}", event);
		gatewayErrPredicate.parseText(event.getValue());
	}

}
