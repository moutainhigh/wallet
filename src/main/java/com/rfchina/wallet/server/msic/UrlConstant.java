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
	 * 查询支付状态
	 */
	public static final String WALLET_APPLY_QUERY = VERSION + "/m/wallet/query_wallet_apply";

	/**
	 * 重做问题单
	 */
	public static final String WALLET_APPLY_REDO = VERSION + "/m/wallet/redo_pay";

	/**
	 * 定时支付
	 */
	public static final String QUARTZ_WALLET_PAY = VERSION + "/schedule/quartz_pay";

	/**
	 * 定时更新支付状态
	 */
	public static final String QUARTZ_UPDATE_PAY_STATUS = VERSION + "/schedule/quartz_update_pay_status";

	/**
	 * 定时支付
	 */
	public static final String QUARTZ_NOTIFY = VERSION + "/schedule/quartz_notify";

	/**
	 * 查询钱包信息（企业or个人）
	 */
	public static final String WALLET_QUERY_INFO = VERSION + "/m/wallet/query_wallet_info";

	/**
	 * 查询钱包信息（企业or个人）
	 */
	public static final String WALLET_QUERY_INFO_BY_UID = VERSION + "/m/wallet/query_wallet_info_by_uid";

	/**
	 * 开通未审核的钱包
	 */
	public static final String CREATE_WALLET = VERSION + "/m/wallet/create_wallet";

	/**
	 * 富慧通审核企业商家钱包
	 */
	public static final String ACTIVE_WALLET_COMPANY = VERSION + "/m/wallet/active_wallet_company";

	/**
	 * 富慧通审核个人商家钱包
	 */
	public static final String ACTIVE_WALLET_PERSON = VERSION + "/m/wallet/active_wallet_person";
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
	public static final String WALLET_BANK_CARD_BIND = VERSION + "/m/wallet/bank_card/bind";

	/**
	 * 银行类别列表
	 */
	public static final String WALLET_BANK_CLASS_LIST = VERSION + "/wallet/bank/class_list";

	/**
	 * 银行地区列表
	 */
	public static final String WALLET_BANK_AREA_LIST = VERSION + "/wallet/bank/area_list";

	/**
	 * 银行支行列表
	 */
	public static final String WALLET_BANK_LIST = VERSION + "/wallet/bank/bank_list";

	/**
	 * 银行支行信息
	 */
	public static final String WALLET_BANK = VERSION + "/wallet/bank/bank";

	/**
	 * 发送短信验证码
	 */
	public static final String WALLET_SEND_VERIFY_CODE = VERSION + "/m/wallet/send_verify_code";

	/**
	 * 短信验证码登录
	 */
	public static final String WALLET_LOGIN_WITH_VERIFY_CODE = VERSION + "/m/wallet/login_with_verify_code";

	/**
	 * 创建云商通会员
	 */
	public static final String YUNST_CREATE_MEMBER = VERSION + "/m/yunst/create_member";
	/**
	 * 云商通发送短信验证码
	 */
	public static final String YUNST_SMS_VERIFY_CODE = VERSION + "/m/yunst/sms_verify_code";
	/**
	 * 云商通绑定手机
	 */
	public static final String YUNST_BIND_PHONE = VERSION + "/m/yunst/bind_phone";
	/**
	 * 云商通修改手机
	 */
	public static final String YUNST_MODIFY_PHONE = VERSION + "/m/yunst/modify_phone";
	/**
	 * 云商通查询会员信息
	 */
	public static final String YUNST_MEMBER_INFO = VERSION + "/m/yunst/member_info";
	/**
	 * 云商通统一回调
	 */
	public static final String YUNST_NOTIFY = VERSION + "/yunst/notify";
}
