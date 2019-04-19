package com.rfchina.wallet.server.service;

import java.util.concurrent.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by zhouhairong on 2016/7/20.
 */
@Service(value = "mqService")
public class MqService {

	@Autowired
	@Qualifier("mqExecutor")
	private ExecutorService mqExecutorService;

	@Value(value = "${mq.virtual_host}")
	private String VIRTUAL_HOST = null;
	@Value(value = "${mq.exchange_name}")
	private String EXCHANGE_NAME = null;
	@Value(value = "${mq.queue_name}")
	private String QUEUE_NAME = null;
	@Value(value = "${mq.host}")
	private String HOST = null;
	@Value(value = "${mq.port}")
	private Integer PORT = null;
	@Value(value = "${mq.username}")
	private String USERNAME = null;
	@Value(value = "${mq.password}")
	private String PASSWORD = null;

}
