package com.rfchina.wallet.server.config;

import com.rfchina.biztool.verify.StringVerify;
import com.rfchina.biztools.xdiamond.DynamicConfigAdvice;
import com.rfchina.biztools.xdiamond.XDiamondDynamicConfiguration;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.service.ConfigService;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Autowired
	private YunstTpl yunstTpl;

	@Autowired
	@Qualifier("exactErrPredicate")
	private ExactErrPredicate exactErrPredicate;

	@Bean
	public XDiamondDynamicConfiguration xDiamondDynamicConfiguration() {
		log.info("开启xDiamond动态配置");

		DynamicConfigAdvice yunstAdvice = DynamicConfigAdvice.builder()
			.predicate(event -> StringVerify.matchAny(event.getKey(), "yunst.serverUrl",
				"yunst.sysId", "yunst.pfxPath", "yunst.password", "yunst.alias", "yunst.version",
				"yunst.tlCertPath")
			).consumer(event -> yunstTpl.forceInit())
			.build();
		DynamicConfigAdvice pupdongAdvice = DynamicConfigAdvice.builder()
			.predicate(event -> StringVerify.matchAny(event.getKey(), "wlpay.pudong.exactErr"))
			.consumer(event -> {
				log.info("change key wlpay.pudong.exactErr, event : {}", event);
				exactErrPredicate.parseText(event.getValue());
			})
			.build();

		return XDiamondDynamicConfiguration.builder()
			.advisors(
				// 此处为@Value所在的类，配置更新时自动扫描、变更@Value内部变量;平台配置统一在ConfigService，其他分散配置的项目，请换成对应的Service
				Arrays.asList(configService, yunstAdvice, pupdongAdvice))
			.build();
	}

}
