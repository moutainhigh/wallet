package com.rfchina.wallet.server.config;

import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.service.ConfigService;
import io.github.xdiamond.client.annotation.AllKeyListener;
import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PropertyConfig {

	@Autowired
	private ConfigService configService;


	@AllKeyListener
	public void onPropertyChange(ConfigEvent event) {

		Field[] fields = ConfigService.class.getDeclaredFields();
		for (Field field : fields) {
			Value anno = field.getDeclaredAnnotation(Value.class);
			String value = anno.value();
			if (value.equals("${" + event.getKey() + "}")) {
				try {
					field.setAccessible(true);
					field.set(configService, event.getValue());
					log.info("set key {} to {}", event.getKey(), event.getValue());
				} catch (IllegalAccessException e) {
					log.error("更新配置失败", e);
				}
				break;
			}
		}
	}


}
