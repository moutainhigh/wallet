package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.wallet.server.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class YunstBizHandlerTest extends SpringBaseTest {

	@Autowired
	private YunstBizHandler yunstBizHandler;

	@Test
	public void collect() {
//		yunstBizHandler.collect(1L);
	}

	@Test
	public void updateWithdrawStatus() {
		yunstBizHandler.updateOrderStatus("WW20191029975021377");
	}
}