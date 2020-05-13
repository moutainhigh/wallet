package com.rfchina.wallet.server.config;

import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.mch.sdk.api.ChargingGetConfigRequest;
import com.rfchina.mch.sdk.model.ChargingConfig;
import com.rfchina.mch.sdk.model.ListChargingConfig;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.biztools.CacheHashMap;
import com.rfchina.platform.biztools.fileserver.FileServerAutoConfig;
import com.rfchina.platform.sdk2.ApiClient;
import com.rfchina.platform.sdk2.response.ResponseData;
import com.rfchina.platform.spring.SpringContext;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.msic.EnumWallet.FeeConfigKey;
import com.rfchina.wallet.server.service.AppService;
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
	@Qualifier("apiTemplate")
	private ApiClient apiTemplate;

	@Autowired
	private AppService appService;

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

	@Bean
	@Qualifier(value = "feeMap")
	public CacheHashMap<String, ChargingConfig> feeMap() {

		return new CacheHashMap<>(120L, (obj) -> {
			updateFeeConfig(obj, FeeConfigKey.YUNST_WITHDRAW.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_COMPANY_AUDIT.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_PERSON_AUDIT.getValue());
		});
	}

	private void updateFeeConfig(CacheHashMap<String, ChargingConfig> map, String key) {
		try {
			ChargingConfig config = getFeeConfig(key);
			if (config != null) {
				map.put(key, config);
			}
		} catch (Exception e) {
			log.error("加载手续费配置错误", e);
		}
	}

	private ChargingConfig getFeeConfig(String chargingKey) {
		ChargingGetConfigRequest req = ChargingGetConfigRequest.builder()
			.accessToken(appService.getAccessToken())
			.chargingKey(chargingKey)
			.build();
		ResponseData<ListChargingConfig> resp = apiTemplate.execute(req);
		return (!resp.getData().isEmpty()) ? resp.getData().get(0) : null;
	}
}
