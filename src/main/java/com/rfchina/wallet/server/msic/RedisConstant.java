package com.rfchina.wallet.server.msic;

public class RedisConstant {

	/* 锁 */

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
	 * 定时同步通道
	 */
	public static String LOCK_QUARTZ_SYNC_TUNNEL = "quartzSyncTunnel";
	/** 更新银行卡锁 */
	public static String LOCK_CARD_UPDATE = "UPDATE_COMPANY_CARD";
	/**
	 * 定时同步余额
	 */
	public static String LOCK_QUARTZ_SYNC_BALANCE = "quartzSyncBalance";
	/**
	 * 定时通联通道手续费报表统计
	 */
	public static String LOCK_QUARTZ_YUNST_FEE_REPORT = "quartzYunstFeeReprot";
	/**
	 * 订单锁
	 */
	public static String LOCK_PAY_ORDER = "order:";

	/**
	 * 定时通知结算不成功订单
	 */
	public static String LOCK_QUARTZ_ORDER_SETTLE_FAILED = "quartzOrderSettleFailed";

	/* 缓存 */
	public final static String PREX_MANAGER_DOWNLOAD_KEY = "platform:report:download";

	public final static String PREX_WALLET_REPORT_DOWNLOAD = "platform:wallet:report:download";

	/* 缓存 */
	public final static String DOWNLOAD_OBJECT_KEY = "platform:report:download";
}
