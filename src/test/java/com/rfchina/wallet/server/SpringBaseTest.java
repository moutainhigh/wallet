package com.rfchina.wallet.server;

import com.alibaba.fastjson.JSON;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.rfchina.app.model.App;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.wallet.server.service.ConfigService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class SpringBaseTest {

	@Autowired
	private ConfigService configService;

	@Autowired
	private SessionThreadLocal sessionThreadLocal;

	private StackTraceElement currMethod() {
		return Thread.currentThread().getStackTrace()[3];
	}

	protected void logStack(Object obj) {
		StackTraceElement stack = currMethod();
		log.info("method {},result = {}",
			stack.getClassName() + "." + stack.getMethodName(),
			JSON.toJSON(obj));
	}

	@Before
	public void initYunst() throws Exception {
		Process process = Runtime.getRuntime().exec("git config user.name");
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
//		env-product
//		if (name.startsWith("niezengming")) {
//			YunClient.configure(new YunConfig(configService.getYstServerUrl(),
//				configService.getYstSysId(),
//				configService.getYstPassword(), configService.getYstAlias(),
//				configService.getYstVersion(),
//				"/data/support/dev-key/yunst-pro/2002041713537320330.pfx",
//				"/data/support/dev-key/yunst-pro/TLCert.cer"));
//		}
	}

	@Before
	public void initSess() {
		App app = new App();
		app.setId(configService.getAppId());
		sessionThreadLocal.addApp(app);
	}
}
