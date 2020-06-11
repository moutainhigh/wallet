package com.rfchina.wallet.server.web;

import com.rfchina.scheduler.annotation.FuScheduleTaskReporter;
import com.rfchina.wallet.server.api.ScheduleApi;
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
	private ScheduleApi scheduleApi;


	@RequestMapping(value = UrlConstant.QUARTZ_UPDATE_PAY_STATUS, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzUpdateJunior(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzUpdateJunior");

		scheduleApi.quartzUpdateJunior();

		log.info("scheduler:  完成任务[{}]", "quartzUpdateJunior");
		return "success";
	}

	@RequestMapping(value = UrlConstant.QUARTZ_UPDATE_SENIOR_STATUS, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzUpdateSenior(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzUpdateSenior");

		scheduleApi.quartzUpdateSenior();

		log.info("scheduler:  完成任务[{}]", "quartzUpdateSenior");
		return "success";
	}

	@RequestMapping(value = UrlConstant.QUARTZ_WALLET_PAY, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzPay(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzPay");

		scheduleApi.quartzPay();

		log.info("scheduler: 完成任务[{}]", "quartzPay");

		return "success";
	}

	@RequestMapping(value = UrlConstant.QUARTZ_NOTIFY, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzNotify(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzNotify");

		scheduleApi.quartzNotify();

		log.info("scheduler: 完成任务[{}]", "quartzNotify");

		return "success";
	}

	@RequestMapping(value = UrlConstant.QUARTZ_BALANCE, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzBalance(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam(value = "balance_date", required = false) String balanceDate,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzBalance");

		scheduleApi.quartzBalance(balanceDate);

		log.info("scheduler: 完成任务[{}]", "quartzBalance");

		return "success";
	}


	@RequestMapping(value = UrlConstant.QUARTZ_CHARGING, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzCharging(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam(value = "balance_date", required = false) String balanceDate,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzCharging");

		scheduleApi.quartzCharging(balanceDate);

		log.info("scheduler: 完成任务[{}]", "quartzCharging");

		return "success";
	}

	@RequestMapping(value = UrlConstant.QUARTZ_WITHDRAW, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzWithdraw(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzWithdraw");

		scheduleApi.quartzWithdraw();

		log.info("scheduler: 完成任务[{}]", "quartzWithdraw");

		return "success";
	}

	@RequestMapping(value = UrlConstant.QUARTZ_SYNC_TUNNEL, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzSyncTunnel(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign
	){
		log.info("scheduler: 开始执行任务[{}]", "quartzSyncTunnel");

		scheduleApi.quartzSyncTunnel();

		log.info("scheduler: 完成任务[{}]", "quartzSyncTunnel");

		return "success";
	}


	@RequestMapping(value = UrlConstant.QUARTZ_SYNC_BALANCE, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzSyncBalance(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign
	){
		log.info("scheduler: 开始执行任务[{}]", "quartzSyncBalance");

		scheduleApi.quartzSyncBalance();

		log.info("scheduler: 完成任务[{}]", "quartzSyncBalance");

		return "success";
	}
}
