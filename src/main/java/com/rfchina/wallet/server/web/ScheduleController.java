package com.rfchina.wallet.server.web;

import com.rfchina.scheduler.annotation.FuScheduleTaskReporter;
import com.rfchina.wallet.server.api.JuniorWalletApi;
import com.rfchina.wallet.server.msic.UrlConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ScheduleController {

	@Autowired
	private JuniorWalletApi juniorWalletApi;


	@RequestMapping(value = UrlConstant.WALLET_UPDATE_PAY_STATUS, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzUpdatePayStatus() {

		log.info("scheduler: 开始执行任务[{}]", "quartzUpdatePayStatus");

		juniorWalletApi.quartzUpdate();

		log.info("scheduler: 完成任务[{}]", "quartzUpdatePayStatus");
		return "success";
	}
}
