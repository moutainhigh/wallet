package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.ChannelNotifyExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.OrderType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NotifyServiceTest extends SpringBaseTest {

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private ChannelNotifyExtDao channelNotifyDao;

	@Test
	public void handleRechargeResult() {
		ChannelNotify channelNotify = channelNotifyDao.selectByPrimaryKey(11L);
		notifyService.handleOrderResult(channelNotify, OrderType.RECHARGE);
	}
}