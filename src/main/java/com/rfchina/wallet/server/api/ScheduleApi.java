package com.rfchina.wallet.server.api;

import java.util.Date;

public interface ScheduleApi {
	/**
	 * 定时更新支付状态
	 */
	void quartzUpdateJunior();
	void quartzUpdateSenior();

	/**
	 * 定时支付
	 */
	void quartzPay();

	/**
	 * 定时通知
	 */
	void quartzNotify();

	/**
	 * 定时对账
	 */
	void quartzBalance(String balanceDate);

	/**
	 * 定时计费
	 */
	void quartzCharging(String balanceDate);

}
