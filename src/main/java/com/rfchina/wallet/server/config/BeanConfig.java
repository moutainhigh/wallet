package com.rfchina.wallet.server.config;

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.utils.DateUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class BeanConfig {
	@Bean
	public SessionThreadLocal sessionThreadLocal() {
		return new SessionThreadLocal();
	}

	@Bean
	@Qualifier(value = "jsonResponseObjectMapper")
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper jsonResponseObjectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper om = builder.build();
		om.setDateFormat(new SimpleDateFormat(DateUtil.STANDARD_DTAETIME_PATTERN));
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		return om;
	}

	@Bean
	@ConditionalOnBean(value = ObjectMapper.class)
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
			@Qualifier(value = "jsonResponseObjectMapper") ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
				new MappingJackson2HttpMessageConverter(
						objectMapper);
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
				Lists.newArrayList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json")));
		return mappingJackson2HttpMessageConverter;
	}

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter();
	}

	@Bean("mqExecutor")
	public ExecutorService mqExecutor(){
		return Executors.newFixedThreadPool(2,
			new BasicThreadFactory.Builder().namingPattern("MqExec_%d").build());
	}

}
