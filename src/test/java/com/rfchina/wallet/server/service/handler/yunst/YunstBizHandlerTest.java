package com.rfchina.wallet.server.service.handler.yunst;

import static org.junit.Assert.*;

import com.rfchina.wallet.server.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class YunstBizHandlerTest extends SpringBaseTest {

	@Autowired
	private YunstBizHandler yunstBizHandler;

	@Test
	public void updateOrderStatus(){
		yunstBizHandler.updateOrderStatus("WC20191105682221394");
	}


}