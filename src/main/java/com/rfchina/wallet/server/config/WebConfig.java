package com.rfchina.wallet.server.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rfchina.platform.common.json.PrettyObjectMapper;
import com.rfchina.platform.spring.exception.RfchinaExceptionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 实现WebMvcConfigurer
 *
 * @author huangtiande@rfchina.com
 * create_date: 2018-9-7
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private StringHttpMessageConverter stringHttpMessageConverter;
	@Autowired
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		resolvers.add(new RfchinaExceptionResolver());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(stringHttpMessageConverter);
		converters.add(mappingJackson2HttpMessageConverter);
	}


}
