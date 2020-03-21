package com.rfchina.wallet.server.msic;

public class LockConstant {

	/**
	 * 定时更新支付状态
	 */
	public static String LOCK_QUARTZ_UPDATE_JUNIOR = "quartzUpdateJunior";
	/**
	 * 定时更新支付状态
	 */
	public static String LOCK_QUARTZ_UPDATE_SENIOR = "quartzUpdateSenior";
	/**
	 * 定时支付
	 */
	public static String LOCK_QUARTZ_PAY = "quartzPay";
	/**
	 * 定时通知
	 */
	public static String LOCK_QUARTZ_Notify = "quartzNotify";
	/**
	 * 定时对账
	 */
	public static String LOCK_QUARTZ_BALANCE = "quartzBalance";
	/**
	 * 定时提现
	 */
	public static String LOCK_QUARTZ_WITHDRAW = "quartzWithdraw";
	/**
	 * 定时通联通道手续费报表统计
	 */
	public static String LOCK_QUARTZ_YUNST_FEE_REPORT = "quartzYunstFeeReprot";
	/**
	 * 订单锁
	 */
	public static String LOCK_PAY_ORDER = "order:";
}
