package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.mq.SimpleMqMsg;
import com.rfchina.platform.spring.SpringContext;
import com.rfchina.wallet.server.SpringBaseTest;
import org.junit.Test;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultMqAdviceTest extends SpringBaseTest {

	@Autowired
	private DefaultMqAdvice defaultMqAdvice;

	@Autowired
	private SimpleRabbitListenerContainerFactory factory;

	@Test
	public void publish() {
		SpringContext.getBean(SimpleRabbitListenerContainerFactory.class);
		defaultMqAdvice.publish(SimpleMqMsg.builder().build());
	}

}