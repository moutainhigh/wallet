package com.rfchina.wallet.server.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ConfigService {
	@Value(value = "${zipkin.service.enable}")
	private Boolean zipKinEnable;

	@Value(value = "${zipkin.collector.url}")
	private String zipkinUrl;

	@Value(value = "${zipkin.service.name}")
	private String zipkinName;

	@Value(value = "${app.base.url}")
	private String appBaseUrl;

	@Value(value = "${app.id}")
	private Long appId;

	@Value(value = "${app.secret}")
	private String appSecret;

	@Value(value = "${platform.api.secure.key}")
	private String platformApiSecureKey;

	@Value(value = "${wallet.web.home}")
	private String walletWebHome;

	@Value(value = "${wallet.verify.limit}")
	private Integer verifyLimitCount;

	@Value(value = "${sign.enable}")
	private boolean signEnable;
}
