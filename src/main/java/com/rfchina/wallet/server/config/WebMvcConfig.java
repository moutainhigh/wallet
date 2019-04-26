package com.rfchina.wallet.server.config;

import com.rfchina.platform.spring.exception.RfchinaExceptionResolver;
import com.rfchina.wallet.server.interceptor.AllInterceptor;
import com.rfchina.wallet.server.interceptor.BasicInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
		registry.addInterceptor(allInterceptor).addPathPatterns("/**");
		registry.addInterceptor(basicInterceptor).addPathPatterns("/**");
	}

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter();
	}

//	@Bean
//	@Qualifier(value = "jsonResponseObjectMapper")
//	@ConditionalOnMissingBean(ObjectMapper.class)
//	public ObjectMapper jsonResponseObjectMapper(Jackson2ObjectMapperBuilder builder) {
//		ObjectMapper om = builder.build();
//		om.setDateFormat(new SimpleDateFormat(DateUtil.STANDARD_DTAETIME_PATTERN));
//		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//		return om;
//	}
//
//	@Bean
//	@ConditionalOnBean(value = ObjectMapper.class)
//	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
//		@Qualifier(value = "jsonResponseObjectMapper") ObjectMapper objectMapper) {
//		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
//			new MappingJackson2HttpMessageConverter(
//				objectMapper);
//		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
//			Lists.newArrayList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json")));
//		return mappingJackson2HttpMessageConverter;
//	}

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
