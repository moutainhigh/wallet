package com.rfchina.wallet.server.config;

import com.rfchina.biztools.xdiamond.XDiamondDynamicConfiguration;
import com.rfchina.wallet.server.service.ConfigService;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * xdiamond配置变化自动更新
 *
 * @author nzm
 */
@Configuration
@Slf4j
public class PropertyConfig {

	@Autowired
	private ConfigService configService;

	@Bean
	public XDiamondDynamicConfiguration xDiamondDynamicConfiguration(){
		log.info("开启xDiamond动态配置");
		return XDiamondDynamicConfiguration.builder()
			.advisors(Arrays.asList(configService))  // 此处为@Value所在的类，配置更新时自动扫描、变更@Value内部变量;平台配置统一在ConfigService，其他分散配置的项目，请换成对应的Service
			.build();
	}

}
