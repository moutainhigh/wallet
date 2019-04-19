package com.rfchina.wallet.server.config;

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {


	@Bean("mqExecutor")
	public ExecutorService mqExecutor(){
		return Executors.newFixedThreadPool(2,
			new BasicThreadFactory.Builder().namingPattern("MqExec_%d").build());
	}

}
