package com.rfchina.wallet.server.config;

import com.rfchina.platform.zipkin.ZipkinContext;
import com.rfchina.platform.zipkin.ZipkinReport;
import com.rfchina.platform.zipkin.handler.ServletTracingFilter;
import com.rfchina.platform.zipkin.handler.WebMvcInterceptor;
import com.rfchina.platform.zipkin.injector.spring.MysqlInjector;
import com.rfchina.platform.zipkin.injector.spring.RabbitMqInjector;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * ZipkinSupport配置
 *
 * @author nzm
 */
// 标明为配置类
@Configuration
@EnableWebMvc
//// 此处引用zipkin-support的组件
@Import({WebMvcInterceptor.class, ServletTracingFilter.class, MysqlInjector.class,
	RabbitMqInjector.class,ZipkinReport.class})
public class ZipkinConfig {

	@Value("${zipkin.collector.url}")
	private String zipkinUrl;

	@Value("${zipkin.service.enable}")
	private Boolean zipkinEnable;

	@PostConstruct
	public void init() {
		// Zipkin的总开关
		ZipkinContext.setEnable(zipkinEnable);
		// Zipkin收集器地址
		ZipkinContext.setZipkinUrl(zipkinUrl);
		// 本地服务名称
		ZipkinContext.setServiceName("wallet-server");
		// 初始化上下文
		ZipkinContext.init();
	}

}