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
	void quartzBalance(String balanceDate);

	/**
	 * 定时计费
	 */
	void quartzCharging(String balanceDate);

	/**
	 * 定时提现
	 */
	void quartzWithdraw();

	/** 定时同步通道信息 */
	void quartzSyncTunnel();

	/** 定时同步通道余额 */
	void quartzSyncBalance();

	/**
	 * 定时通知结算不成功订单
	 */
	void quartzOrderSettleFailed();
}
