package com.rfchina.wallet.server.config;

import com.rfchina.platform.spring.SpringContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rfchina.passport.misc.SessionThreadLocal;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;

@Configuration
public class BeanConfig {




	@Bean("mqExecutor")
	public ExecutorService mqExecutor() {
		return Executors.newFixedThreadPool(2,
			new BasicThreadFactory.Builder().namingPattern("MqExec_%d").build());
	}

	@Bean
	public SpringContext springContext() {
		return new SpringContext();
	}

	@Bean
	public SessionThreadLocal sessionThreadLocal() {
		return new SessionThreadLocal();
	}

}
