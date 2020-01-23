package com.rfchina.wallet.server.config;

import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.biztools.fileserver.FileServerAutoConfig;
import com.rfchina.platform.spring.SpringContext;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
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
@Import({SimpleExclusiveLock.class, FileServerAutoConfig.class})
public class BeanConfig {

	@Autowired
	@Qualifier("exactErrPredicate")
	private ExactErrPredicate exactErrPredicate;

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

	@Bean
	@Qualifier(value = "cacheExec")
	public ExecutorService cacheSrvExec() {
		return Executors.newFixedThreadPool(2,
			new BasicThreadFactory.Builder().namingPattern("CacheExec_%d").build());
	}

	@Bean
	@Qualifier(value = "walletApiExecutor")
	public ExecutorService walletApiExecutor() {
		return Executors.newFixedThreadPool(4,
				new BasicThreadFactory.Builder().namingPattern("WalletApiExec").build());
	}
}
