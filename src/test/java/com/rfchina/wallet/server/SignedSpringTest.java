package com.rfchina.wallet.server;

import com.rfchina.internal.api.util.SecurityCoder;
import com.rfchina.internal.api.util.SignUtil;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.server.service.AppService;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class SignedSpringTest extends SpringBaseTest{


	@Value("${test.app.secret}")
	protected String appSecret;
	@Value("${test.app.id}")
	protected String appId;

	protected String accessToken;

	@Autowired
	private AppService appService;

	@Autowired
	private SessionThreadLocal sessionThreadLocal;

	@Before
	public void autoSign() {
		sign(null);
	}

	public void sign(Map<String, String> custom) {
		accessToken = appService.getAccessToken();
		sessionThreadLocal.addTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
		Map<String, String> params = BeanUtil.asPureStringMap(
			"access_token", accessToken,
			"timestamp", String.valueOf(System.currentTimeMillis() / 1000)
		);
		if (custom != null) {
			params.putAll(custom);
		}
		Map<String, String[]> reqParams = new HashMap<>();
		for (String key : params.keySet()) {
			String value = params.get(key);
			reqParams.put(key, new String[]{value});
		}
		sessionThreadLocal.addRequestParameters(reqParams);
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		sessionThreadLocal.addSign(sign);
	}
}
