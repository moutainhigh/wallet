package com.rfchina.wallet.server;

import com.alibaba.fastjson.JSON;
import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.security.SecurityCoder;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.SignUtil;
import com.rfchina.wallet.server.service.AppService;
import com.rfchina.wallet.server.service.ConfigService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	public void initYunst() {
		YunClient.configure(new YunConfig(configService.getYstServerUrl(),
			configService.getYstSysId(),
			configService.getYstPassword(), configService.getYstAlias(),
			configService.getYstVersion(),
			"/data/env/config/dev-key/yunst/1902271423530473681.pfx",
			"/data/env/config/dev-key/yunst/TLCert-test.cer"));
	}


}
