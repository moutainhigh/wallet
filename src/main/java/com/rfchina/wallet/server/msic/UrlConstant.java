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
	 * 高级钱包-代收
	 */
	public static final String SENIOR_WALLET_COLLECT_ASYNC = VERSION + "/m/senior/collect_async";
	/**
	 * 高级钱包-代收
	 */
	public static final String SENIOR_WALLET_COLLECT_SYNC = VERSION + "/m/senior/collect_sync";
	/**
	 * 高级钱包-代收查询
	 */
	public static final String SENIOR_WALLET_COLLECT_QUERY = VERSION + "/m/senior/collect_query";

	/**
	 * 高级钱包-清算
	 */
	public static final String SENIOR_WALLET_AGENT_PAY = VERSION + "/m/senior/agent_pay";

	/**
	 * 高级钱包-清算结果查询
	 */
	public static final String SENIOR_WALLET_AGENT_PAY_QUERY = VERSION + "/m/senior/agent_pay_query";

	/**
	 * 高级钱包-退款
	 */
	public static final String SENIOR_WALLET_REFUND = VERSION + "/m/senior/refund";
	/**
	 * 高级钱包-退款结果查询
	 */
	public static final String SENIOR_WALLET_REFUND_QUERY = VERSION + "/m/senior/refund_query";

	/**
	 * 高级钱包-充值
	 */
	public static final String SENIOR_WALLET_RECHARGE = VERSION + "/m/senior/recharge";

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
	 * 升级高级钱包渠道信息
	 */
	public static final String WALLET_CHANNEL_INFO = VERSION + "/m/wallet/channel_info";
	/**
	 * 升级高级钱包
	 */
	public static final String WALLET_UPGRADE = VERSION + "/m/wallet/upgrade_wallet";
	/**
	 * 高级钱包认证验证码
	 */
	public static final String WALLET_SENIOR_SMS_VERIFY_CODE = VERSION + "/m/wallet/senior_sms_verify_code";
	/**
	 * 高级钱包个人认证
	 */
	public static final String WALLET_SENIOR_PERSON_AUTHENTICATION = VERSION + "/m/wallet/senior_person_authentication";
	/**
	 * 高级钱包绑定手机
	 */
	public static final String WALLET_SENIOR_BIND_PHONE = VERSION + "/m/wallet/senior_bind_phone";
	/**
	 * 高级钱包个人修改绑定手机
	 */
	public static final String WALLET_SENIOR_PERSON_CHANGE_BIND_PHONE = VERSION + "/m/wallet/senior_person_change_bind_phone";
	/**
	 * 高级钱包商家资料审核（通道）
	 */
	public static final String WALLET_SENIOR_COMPANY_INFO_AUDIT = VERSION + "/m/wallet/senior_company_info_audit";

	/**
	 * 高级钱包会员协议
	 */
	public static final String WALLET_SENIOR_MEMBER_PROTOCOL = VERSION + "/m/wallet/senior_member_protocol";
	/**
	 * 高级钱包代扣协议（企业,个人商家）
	 */
	public static final String WALLET_SENIOR_BANLACE_PROTOCOL = VERSION + "/m/wallet/senior_balance_protocol";
	/**
	 * 高级钱包个人设置支付密码
	 */
	public static final String WALLET_SENIOR_PERSON_SET_PAY_PASSWORD = VERSION + "/m/wallet/senior_person_set_paypassword";
	/**
	 * 高级钱包-预绑定银行卡
	 */
	public static final String WALLET_SENIOR_PRE_BIND_BANK_CARD = VERSION + "/m/senior/pre_bind_bank_card";
	/**
	 * 高级钱包-确认绑定银行卡
	 */
	public static final String WALLET_SENIOR_CONFIRM_BIND_CARD = VERSION + "/m/senior/confirm_bind_card";
	/**
	 * 高级钱包-解绑银行卡
	 */
	public static final String WALLET_SENIOR_UNBIND_CARD = VERSION + "/m/senior/unbind_card";
	/**
	 * 高级钱包个人信息
	 */
	public static final String WALLET_SENIOR_PERSON_INFO = VERSION + "/m/wallet/senior_person_info";
	/**
	 * 高级钱包企业信息
	 */
	public static final String WALLET_SENIOR_COMPANY_INFO= VERSION + "/m/wallet/senior_company_info";

	/**
	 * 云商通统一回调
	 */
	public static final String YUNST_NOTIFY = VERSION + "/yunst/notify";

	/**
	 * 云商通充值回调
	 */
	public static final String YUNST_RECHARGE_RECALL = VERSION + "/yunst/recharge_recall";

	/**
	 * 云商通代收回调
	 */
	public static final String YUNST_COLLECT_RECALL = VERSION + "/yunst/collect_recall";

	/**
	 * 云商通代付回调
	 */
	public static final String YUNST_AGENT_PAY_RECALL = VERSION + "/yunst/agent_pay_recall";

	/**
	 * 云商通退款回调
	 */
	public static final String YUNST_REFUND_RECALL = VERSION + "/yunst/refund_recall";
}
