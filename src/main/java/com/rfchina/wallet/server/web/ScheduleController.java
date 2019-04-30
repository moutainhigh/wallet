package com.rfchina.wallet.server.web;

import com.rfchina.scheduler.annotation.FuScheduleTaskReporter;
import com.rfchina.wallet.server.api.JuniorWalletApi;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.msic.UrlConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ScheduleController {

	@Autowired
	private WalletApi walletApi;


	@RequestMapping(value = UrlConstant.WALLET_UPDATE_PAY_STATUS, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzUpdatePayStatus(@
		RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzUpdatePayStatus");

		walletApi.quartzUpdate();

		log.info("scheduler: 完成任务[{}]", "quartzUpdatePayStatus");
		return "success";
	}
}
