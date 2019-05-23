package com.rfchina.wallet.server.service;

import com.rfchina.api.ApiClient;
import com.rfchina.internal.api.request.app.GetAccessTokenRequest;
import com.rfchina.internal.api.request.app.RefreshAppTokenRequest;
import com.rfchina.internal.api.response.ResponseData;
import com.rfchina.internal.api.response.model.app.GetAccessTokenReponseModel;
import com.rfchina.internal.api.response.model.app.base.AccessTokenModel;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppService {

	@Autowired
	private com.rfchina.internal.api.ApiClient internalApiClient;

	@Autowired
	private ApiClient apiClient;

	@Autowired
	private ConfigService configService;

	private static String ACCESS_TOKEN = null;
	private static String REFRESH_TOKEN = null;

	private static String WALLET_ACCESS_TOKEN = null;
	private static String WALLET_REFRESH_TOKEN = null;

	@Value("${platform.app.id}")
	private Long platformAppId;

	@Value("${platform.app.secret}")
	private String platformSecret;

	public String getWalletAccessToken(){
		String resultAccessToken = WALLET_ACCESS_TOKEN;
		if (resultAccessToken == null) {
			synchronized (this) {
				resultAccessToken = WALLET_ACCESS_TOKEN;
				if (resultAccessToken == null) {
					com.rfchina.api.response.ResponseData<com.rfchina.api.response.model.app.GetAccessTokenReponseModel> responseData = apiClient
							.execute(new com.rfchina.api.request.app.GetAccessTokenRequest(configService.getAppId(), configService.getAppSecret()));
					if(responseData.getCode() != ResponseCode.EnumResponseCode.COMMON_SUCCESS.getValue()){
						throw new RfchinaResponseException(responseData.getCode(), responseData.getMsg());
					}
					WALLET_ACCESS_TOKEN = responseData.getData().getAccessToken();
					WALLET_REFRESH_TOKEN = responseData.getData().getRefreshToken();
				}
			}
		}
		return WALLET_ACCESS_TOKEN;
	}

	public String getAccessToken() {
		String resultAccessToken = ACCESS_TOKEN;
		if (resultAccessToken == null) {
			synchronized (this) {
				resultAccessToken = ACCESS_TOKEN;
				if (resultAccessToken == null) {
					ResponseData<GetAccessTokenReponseModel> responseData = internalApiClient
							.execute(new GetAccessTokenRequest(platformAppId, platformSecret));
					log.info("getAccessToken , response: {}", JsonUtil.toJSON(responseData));
					if(responseData.getCode() != ResponseCode.EnumResponseCode.COMMON_SUCCESS.getValue()){
						throw new RfchinaResponseException(responseData.getCode(), responseData.getMsg());
					}
						ACCESS_TOKEN = responseData.getData().getAccessToken();
						REFRESH_TOKEN = responseData.getData().getRefreshToken();
				}
			}
		}
		return ACCESS_TOKEN;
	}

	@Scheduled(cron = "0 30 */1 * * ?")
	@Async
	public void refreshAppToken() {
		ResponseData<AccessTokenModel> result = internalApiClient
				.execute(new RefreshAppTokenRequest(REFRESH_TOKEN));
		if (ResponseCode.EnumResponseCode.COMMON_SUCCESS.getValue() == result.getCode()) {
			ACCESS_TOKEN = result.getData().getAccessToken();
			REFRESH_TOKEN = result.getData().getRefreshToken();
		}

		com.rfchina.api.response.ResponseData<com.rfchina.api.response.model.app.RefreshAppTokenResponseModel> responseData =
				apiClient.execute(new com.rfchina.api.request.app.RefreshAppTokenRequest(WALLET_REFRESH_TOKEN));
		if (ResponseCode.EnumResponseCode.COMMON_SUCCESS.getValue() == responseData.getCode()) {
			WALLET_ACCESS_TOKEN = responseData.getData().getAccessToken();
			WALLET_REFRESH_TOKEN = responseData.getData().getRefreshToken();
		}
	}

}
