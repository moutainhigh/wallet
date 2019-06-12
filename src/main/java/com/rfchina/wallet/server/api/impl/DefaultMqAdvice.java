package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.mq.AbstractMqAdvice;
import com.rfchina.biztools.mq.SimpleMqMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class DefaultMqAdvice extends AbstractMqAdvice {

	@Value("${mq.exchange_name}")
	private String exchangeName;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public void publish(SimpleMqMsg msg) {
		if (msg == null) {
			return;
		}
		log.info("发送MQ {}", msg);
		amqpTemplate.convertAndSend(
			StringUtils.isBlank(msg.getExchageName()) ? exchangeName : msg.getExchageName(),
			msg.getRoutingKey(),
			msg.getMessage(null));
	}
}
