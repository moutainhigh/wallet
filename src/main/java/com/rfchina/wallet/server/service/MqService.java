package com.rfchina.wallet.server.service;

import com.rfchina.api.util.StringUtils;
import com.rfchina.biztools.mq.SimpleMqMsg;
import com.rfchina.platform.common.utils.JsonUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
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

	@Value("${mq.exchange_name}")
	private String exchangeName;

	/**
	 * 订阅-发布消息
	 *
	 * @param routingKey 路由
	 */
	public void publish(Object obj, String routingKey) {
		SimpleMqMsg msg = SimpleMqMsg.builder().routingKey(routingKey).msgObj(obj).build();
		log.info("发送MQ {}", JsonUtil.toJSON(msg));
		try {
			rabbitTemplate.convertAndSend(
					StringUtils.isEmpty(msg.getExchageName()) ? exchangeName : msg.getExchageName(),
					msg.getRoutingKey(), msg.getMessage(null));
		} catch (Exception e) {
			log.error("发送MQ消息失败, error: ", e);
		}

	}

	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@Setter
	@Getter
	public static class MqApplyStatusChange {
		private Long applyId;
		private String tradeNo;
		private String orderNo;
		private Integer oldStatus;
		private Integer newStatus;
		private Date changeTime;
		private String msg;
	}
}
