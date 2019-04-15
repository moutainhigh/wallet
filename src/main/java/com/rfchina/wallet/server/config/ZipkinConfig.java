package com.rfchina.wallet.server.config;

import com.rfchina.platform.zipkin.ZipkinContext;
import com.rfchina.platform.zipkin.ZipkinReport;
import com.rfchina.platform.zipkin.handler.ServletTracingFilter;
import com.rfchina.platform.zipkin.handler.WebMvcInterceptor;
import com.rfchina.platform.zipkin.injector.spring.MysqlInjector;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// 标明为配置类
@Configuration
@EnableWebMvc
//// 此处引用zipkin-support的组件
@Import({WebMvcInterceptor.class, ServletTracingFilter.class,MysqlInjector.class,ZipkinReport.class})
public class ZipkinConfig {

	@PostConstruct
	public void init(){
		// Zipkin的总开关
		ZipkinContext.setEnable(true);
		// Zipkin收集器地址
		ZipkinContext.setZipkinUrl("http://192.168.197.205:9411/api/v2/spans");
		// 本地服务名称
		ZipkinContext.setServiceName("wallet-server");

		// 初始化上下文
		ZipkinContext.init();
	}

}