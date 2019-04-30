package com.rfchina.wallet.server.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.spring.exception.RfchinaExceptionResolver;
import com.rfchina.wallet.server.interceptor.AllInterceptor;
import com.rfchina.wallet.server.interceptor.BasicInterceptor;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private StringHttpMessageConverter stringHttpMessageConverter;

	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private AllInterceptor allInterceptor;
	@Autowired
	private BasicInterceptor basicInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(allInterceptor)
			.addPathPatterns("/**");
		registry.addInterceptor(basicInterceptor)
			.addPathPatterns("/**");
	}

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter();
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		resolvers.add(new RfchinaExceptionResolver());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		converters.add(stringHttpMessageConverter);

		ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.STANDARD_DTAETIME_PATTERN));
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
			Lists.newArrayList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json")));

		converters.add(mappingJackson2HttpMessageConverter);
	}
}
