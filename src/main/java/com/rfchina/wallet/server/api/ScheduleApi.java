package com.rfchina.wallet.server.api;

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
	void quartzBalance();

	/**
	 * 定时统计通联通道手续费
	 */
	void quartzYunstFeeReport();

}
