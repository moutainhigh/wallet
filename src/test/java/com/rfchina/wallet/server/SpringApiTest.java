package com.rfchina.wallet.server;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.rfchina.internal.api.util.SecurityCoder;
import com.rfchina.internal.api.util.SignUtil;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.server.service.AppService;
import com.rfchina.wallet.server.service.ConfigService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class SpringApiTest extends SpringBaseTest {

	@Value("${test.app.secret}")
	protected String appSecret;
	@Value("${test.app.id}")
	protected String appId;

	protected String accessToken;

	@Autowired
	private ConfigService configService;

	@Autowired
	private AppService appService;

	@Autowired
	private SessionThreadLocal sessionThreadLocal;

	@Before
	public void autoSign() {
		sign(null);
		initYunst();
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

	public void initYunst() {
		try {
			Process process = Runtime.getRuntime().exec("git config user.name");
			process.waitFor();
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
			String name = reader.readLine();
			//		env-test
			if (name.startsWith("niezengming")) {
				YunClient.configure(new YunConfig(configService.getYstServerUrl(),
					configService.getYstSysId(),
					configService.getYstPassword(), configService.getYstAlias(),
					configService.getYstVersion(),
					"/data/support/dev-key/yunst3/2001081503374814494.pfx",
					"/data/support/dev-key/yunst3/TLCert.cer"));
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
