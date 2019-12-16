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

		log.info("scheduler: 开始执行订单状态更新任务");

		scheduleApi.quartzUpdateJunior();

		log.info("scheduler: 完成执行订单状态更新任务");
		return "success";
	}

	@RequestMapping(value = UrlConstant.QUARTZ_UPDATE_SENIOR_STATUS, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzUpdateSenior(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行订单状态更新任务");

		scheduleApi.quartzUpdateSenior();

		log.info("scheduler: 完成执行订单状态更新任务");
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
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzBalance");

		scheduleApi.quartzBalance();

		log.info("scheduler: 完成任务[{}]", "quartzBalance");

		return "success";
	}



	@RequestMapping(value = UrlConstant.QUARTZ_YUNST_FEE_REPORT, method = RequestMethod.POST)
	@FuScheduleTaskReporter
	public String quartzYunstFeeReport(
		@RequestParam("schedule_id") String scheduleId,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("sign") String sign) {

		log.info("scheduler: 开始执行任务[{}]", "quartzYunstFeeReport");

		scheduleApi.quartzYunstFeeReport();

		log.info("scheduler: 完成任务[{}]", "quartzYunstFeeReport");

		return "success";
	}
}
