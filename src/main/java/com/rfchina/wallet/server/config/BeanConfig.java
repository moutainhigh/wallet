package com.rfchina.wallet.server.config;

import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.spring.SpringContext;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.UserRedoPredicate;
import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * 项目配置
 *
 * @author nzm
 */
@Slf4j
@Configuration
@Import(SimpleExclusiveLock.class)
public class BeanConfig {

	@Autowired
	@Qualifier("exactErrPredicate")
	private ExactErrPredicate exactErrPredicate;

	@Autowired
	@Qualifier("userRedoPredicate")
	private UserRedoPredicate userRedoPredicate;

	@Bean
	public SpringContext springContext() {
		return new SpringContext();
	}

	@Bean
	public SessionThreadLocal sessionThreadLocal() {
		return new SessionThreadLocal();
	}

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
			.connectTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(60, TimeUnit.SECONDS)
			.build();
	}

	@Bean(name = "exactErrPredicate")
	public ExactErrPredicate exactErrPredicate(
		@Value("${wlpay.pudong.exactErr}") String exactErr) {
		ExactErrPredicate predicate = new ExactErrPredicate();
		predicate.parseText(exactErr);
		return predicate;
	}

	@OneKeyListener(key = "wlpay.pudong.exactErr")
	public void onExactErrChange(ConfigEvent event) {
		log.info("change key wlpay.pudong.exactErr, event : {}", event);
		exactErrPredicate.parseText(event.getValue());
	}

	@Bean(name = "userRedoPredicate")
	public UserRedoPredicate userRedoPredicate(
		@Value("${wlpay.pudong.userRedoErr}") String userRedoErr) {
		UserRedoPredicate predicate = new UserRedoPredicate();
		predicate.parseText(userRedoErr);
		return predicate;
	}

	@OneKeyListener(key = "wlpay.pudong.userRedoErr")
	public void onUserRedoErrChange(ConfigEvent event) {
		log.info("change key wlpay.pudong.userRedoErr, event : {}", event);
		userRedoPredicate.parseText(event.getValue());
	}

}
