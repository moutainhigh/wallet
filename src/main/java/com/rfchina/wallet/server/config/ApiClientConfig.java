package com.rfchina.wallet.server.config;

import com.rfchina.internal.api.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * APIClient配置
 *
 * @author nzm
 */
@Configuration
public class ApiClientConfig {

	@Bean(name = "apiClient")
	public com.rfchina.api.ApiClient apiClient(@Value("${app.base.url}") String serverUrl,
		@Value("${app.secret}") String platformSecret, @Value("${app.id}") Long appId) {
		return new com.rfchina.api.ApiClient(serverUrl, appId, platformSecret);
	}

	@Bean(name = "internalApiClient")
	public ApiClient internalApiClient(@Value("${app.base.url}") String serverUrl,
		@Value("${platform.app.secret}") String platformSecret, @Value("${platform.app.id}") String appId) {
		return new ApiClient(serverUrl, appId, platformSecret);
	}

}
