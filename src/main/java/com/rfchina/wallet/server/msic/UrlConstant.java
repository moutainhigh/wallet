package com.rfchina.wallet.server.msic;

public class UrlConstant {

	private final static String PREFIX = "/wallet_server/";

	private static final String VERSION = PREFIX + "v1";

	/**
	 * 初级钱包-思力出钱
	 */
	public static final String JUNIOR_WALLET_PAY_IN = VERSION + "/m/junior/sl_pay_in";

	/**
	 * 初级钱包-思力批量出钱（最多20笔）
	 */
	public static final String JUNIOR_WALLET_BATCH_PAY_IN = VERSION + "/m/junior/sl_batch_pay_in";

	/**
	 * 初级钱包-查询支付状态
	 */
	public static final String JUNIOR_WALLET_QUERY = VERSION + "/m/junior/sl_query";

	public static final String WALLET_UPDATE_PAY_STATUS = VERSION + "/wallet/quartz_update_pay_status";

	/**
	 * 查询钱包信息（企业or个人）
	 */
	public static final String WALLET_QUERY_INFO = VERSION + "/m/wallet/query_wallet_info";

	/**
	 * 开通未审核的钱包
	 */
	public static final String CREATE_WALLET = VERSION + "/m/wallet/create_wallet";

	/**
	 * 查询用户信息
	 */
	public static final String WALLET_USER_LIST = VERSION + "/wallet/user/list";

	/**
	 * 查询交易流水
	 */
	public static final String WALLET_LOG_LIST = VERSION + "/m/wallet/log/list";

	/**
	 * 查询钱包绑定的银行卡
	 */
	public static final String WALLET_BANK_CARD_LIST = VERSION + "/m/wallet/bank_card/list";

	/**
	 * 钱包绑定银行卡
	 */
	public static final String WALLET_BANK_CARD_BIND = VERSION + "/wallet/bank_card/bind";

	/**
	 * 银行类别列表
	 */
	public static final String WALLET_BANK_CLASS_LIST = "/wallet/bank/class_list";

	/**
	 * 银行地区列表
	 */
	public static final String WALLET_BANK_AREA_LIST = "/wallet/bank/area_list";

	/**
	 * 银行支行列表
	 */
	public static final String WALLET_BANK_LIST = "/wallet/bank/bank_list";
}
