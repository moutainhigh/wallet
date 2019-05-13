package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.server.SpringBaseTest;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class AmqpServiceTest extends SpringBaseTest {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Test
	public void publish() {
		amqpTemplate.convertAndSend("rfchina.platform", MqConstant.WALLET_PAY_RESULT, "TESTUNIT");
	}

}
