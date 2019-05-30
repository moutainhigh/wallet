package com.rfchina.wallet.server.bank.pudong.domain.util;

import static org.junit.Assert.*;

import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ExceptionUtilTest {

	@Test
	public void explain() {
	}

	@Test
	public void getErrCode() {
		String note = "推荐成交金域中央 错误原因:EGG0346 收款人账号户名信息不符&gt;";
		String errCode = ExceptionUtil.extractErrCode(note);
		log.info(errCode);
		assertTrue("EGG0346".equals(errCode));
	}

	@Test
	public void testLog() {
		long consume = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			log.info("推荐成交金域中央" + "错误原因" + "EGG0346" + "收款人账号户名" + "名信息不符&gt;" + i);
//			log.info("{} {} {} {} {} {}", "推荐成交金域中央", "错误原因", "EGG0346", "收款人账号户名", "名信息不符&gt;", i);
		}
		consume = System.currentTimeMillis() - consume;
		log.info("" + consume);
	}

}