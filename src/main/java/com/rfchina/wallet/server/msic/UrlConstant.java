package com.rfchina.wallet.server.msic;

public class UrlConstant {

	private static final String VERSION = "v1";

	public static final String JUNIOR_WALLET_PAY_IN = VERSION + "/junior/sl_pay_in";

	public static final String JUNIOR_WALLET_BATCH_PAY_IN = VERSION + "/junior/sl_batch_pay_in";

	public static final String JUNIOR_WALLET_QUERY = VERSION + "/junior/sl_query";

	public static final String WALLET_UPDATE_PAY_STATUS = VERSION + "/wallet/quartz_update_pay_status";

	public static final String WALLET_QUERY_INFO = VERSION + "/wallet/query_wallet_info";

	/**
	 * 开通未审核的钱包
	 */
	public static final String CREATE_WALLET = VERSION + "/wallet/create_wallet";

	/**
	 * 查询用户信息
	 */
	public static final String WALLET_USER_LIST = VERSION + "/wallet/user/list";

	/**
	 * 查询交易流水
	 */
	public static final String WALLET_LOG_LIST = VERSION + "/wallet/log/list";

	public static final String WALLET_BANK_CARD_LIST = VERSION + "/wallet/log/list";

}
