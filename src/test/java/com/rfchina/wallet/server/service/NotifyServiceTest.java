package com.rfchina.wallet.server.service;

import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.ChannelNotifyExtDao;
import org.springframework.beans.factory.annotation.Autowired;

public class NotifyServiceTest extends SpringBaseTest {

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private ChannelNotifyExtDao channelNotifyDao;

}