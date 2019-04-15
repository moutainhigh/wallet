package com.rfchina.wallet.server.config;

import io.github.xdiamond.client.spring.XDiamondConfigFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class XDiamondConfig {

	@Bean
	public static XDiamondConfigFactoryBean xDiamondConfigFactoryBean() {
		XDiamondConfigFactoryBean bean = new XDiamondConfigFactoryBean();
		bean.setServerHost("${xdiamond.server.url:config.thinkinpower.net}");
		bean.setServerPort("${xdiamond.server.port:5678}");
		bean.setGroupId("com.rfchina");
		bean.setArtifactId("wallet_super");
		bean.setVersion("${xdiamond.project.version:1.0.0}");
		bean.setProfile("${xdiamond.project.profile:dev}");
		bean.setSecretKey("${xdiamond.project.secretkey:}");
		bean.setbPrintConfigWhenBoot("false");

		return bean;
	}


	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(
		XDiamondConfigFactoryBean bean)
		throws Exception {
		PropertyPlaceholderConfigurer config = new PropertyPlaceholderConfigurer();
		config.setProperties(bean.getObject().getProperties());
		return config;
	}
}
