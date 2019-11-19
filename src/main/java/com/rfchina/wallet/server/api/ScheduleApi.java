package com.rfchina.wallet.server.api;

public interface ScheduleApi {
	/**
	 * 定时更新支付状态
	 */
	void quartzUpdate();

	/**
	 * 定时支付
	 */
	void quartzDealApply();

	/**
	 * 定时通知
	 */
	void quartzNotify();

	/**
	 * 定时对账
	 */
	void quartzBalance();
}
