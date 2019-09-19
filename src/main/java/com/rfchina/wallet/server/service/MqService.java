package com.rfchina.wallet.server.service;

import com.alibaba.fastjson.JSON;
import com.rfchina.biztools.mq.SimpleMqMsg;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zhouhairong on 2016/7/20.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MqService {

	@NonNull
	private RabbitTemplate rabbitTemplate;

	@NonNull
	private ConfigService configService;

	@Value("${mq.exchange_name}")
	private String exchangeName;

	/**
	 * 订阅-发布消息
	 *
	 * @param routingKey 路由
	 */
	public void publish(Object obj, String routingKey) {
		SimpleMqMsg msg = SimpleMqMsg.builder().routingKey(routingKey).msgObj(obj).build();
		log.info("发送MQ {}", JSON.toJSONString(msg));
		rabbitTemplate.convertAndSend(StringUtils.isBlank(msg.getExchageName()) ? exchangeName : msg.getExchageName(),
				msg.getRoutingKey(), msg.getMessage(null));

	}

	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public class MqApplyStatusChange {
		private Long applyId;
		private Integer oldStatus;
		private Integer newStatus;
		private Date changeTime;
	}
}
