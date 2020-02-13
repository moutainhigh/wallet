package com.rfchina.wallet.server.msic;

public class UrlConstant {

	private final static String PREFIX = "/wallet_server/";

	private static final String VERSION = PREFIX + "v1";

	/**
	 * 初级钱包-思力出钱
	 */
	public static final String M_JUNIOR_WALLET_PAY_IN = VERSION + "/m/junior/sl_pay_in";

	/**
	 * 初级钱包-思力批量出钱（最多20笔）
	 */
	public static final String M_JUNIOR_WALLET_BATCH_PAY_IN = VERSION + "/m/junior/sl_batch_pay_in";

	/**
	 * 高级钱包-代收
	 */
	public static final String SENIOR_WALLET_COLLECT_SYNC = VERSION + "/m/senior/pay/collect_sync";

	/**
	 * 高级钱包-清算
	 */
	public static final String SENIOR_WALLET_AGENT_PAY = VERSION + "/m/senior/pay/agent_pay";

	/**
	 * 高级钱包-退款
	 */
	public static final String SENIOR_WALLET_REFUND = VERSION + "/m/senior/pay/refund";

	/**
	 * 高级钱包-代扣
	 */
	public static final String SENIOR_WALLET_DEDUCTION = VERSION + "/m/senior/pay/deduction";

	/**
	 * 高级钱包-订单结果查询
	 */
	public static final String SENIOR_WALLET_ORDER_QUERY = VERSION + "/m/senior/pay/order_query";

	/**
	 * 高级钱包-充值
	 */
	public static final String SENIOR_WALLET_RECHARGE = VERSION + "/m/senior/pay/recharge";

	/**
	 * 高级钱包-短信确认
	 */
	public static final String SENIOR_WALLET_SMS_CONFIRM = VERSION + "/m/senior/pay/sms_confirm";

	/**
	 * 高级钱包-重发短信
	 */
	public static final String SENIOR_WALLET_SMS_RETRY = VERSION + "/m/senior/pay/sms_retry";

	/**
	 * 高级钱包-提现
	 */
	public static final String SENIOR_WALLET_WITHDRAW = VERSION + "/m/senior/pay/withdraw";

	/**
	 * 高级钱包-对账文件
	 */
	public static final String SENIOR_WALLET_BALANCE_FILE = VERSION + "/m/senior/pay/balance_file";

	/**
	 * 查询支付状态
	 */
	public static final String M_WALLET_APPLY_QUERY = VERSION + "/m/wallet/query_wallet_apply";

	/**
	 * 重做问题单
	 */
	public static final String M_WALLET_APPLY_REDO = VERSION + "/m/wallet/redo_pay";

	/**
	 * 定时支付
	 */
	public static final String QUARTZ_WALLET_PAY = VERSION + "/schedule/quartz_pay";

	/**
	 * 定时更新支付状态
	 */
	public static final String QUARTZ_UPDATE_PAY_STATUS = VERSION + "/schedule/quartz_update_pay_status";
	public static final String QUARTZ_UPDATE_SENIOR_STATUS = VERSION + "/schedule/quartz_update_senior_status";

	/**
	 * 定时邮件通知挂起单
	 */
	public static final String QUARTZ_NOTIFY = VERSION + "/schedule/quartz_notify";

	/**
	 * 定时支付
	 */
	public static final String QUARTZ_BALANCE = VERSION + "/schedule/quartz_balance";

	/**
	 * 定时计费
	 */
	public static final String QUARTZ_CHARGING = VERSION + "/schedule/quartz_charging";

	/**
	 * 查询钱包信息（企业or个人）
	 */
	public static final String M_WALLET_QUERY_INFO = VERSION + "/m/wallet/query_wallet_info";

	/**
	 * 查询钱包信息（企业or个人）
	 */
	public static final String M_WALLET_QUERY_INFO_BY_UID = VERSION + "/m/wallet/query_wallet_info_by_uid";

	/**
	 * 查询钱包列表
	 */
	public static final String WALLET_LIST = VERSION + "/m/wallet/wallet_list";

	/**
	 * 开通未审核的商家钱包
	 */
	public static final String M_CREATE_MCH_WALLET = VERSION + "/m/wallet/create_mch_wallet";

	/**
	 * 设置出款申请单状态为失败
	 */
	public static final String M_APPLY_BILL_SET_STATUS_FAIL = VERSION + "/m/wallet/apply_bill/set_status_fail";

	/**
	 * 富慧通审核企业商家钱包
	 */
	public static final String M_AUDIT_WALLET_COMPANY = VERSION + "/m/wallet/audit_wallet_company";

	/**
	 * 富慧通审核个人商家钱包
	 */
	public static final String M_ACTIVE_WALLET_PERSON = VERSION + "/m/wallet/active_wallet_person";
	/**
	 * 查询交易流水
	 */
	public static final String M_WALLET_LOG_LIST = VERSION + "/m/wallet/log/list";

	/**
	 * 查询钱包绑定的银行卡
	 */
	public static final String M_WALLET_BANK_CARD_LIST = VERSION + "/m/wallet/bank_card/list";

	/**
	 * 钱包绑定银行卡
	 */
	public static final String M_WALLET_BANK_CARD_BIND = VERSION + "/m/wallet/bank_card/bind";

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
	public static final String M_WALLET_SEND_VERIFY_CODE = VERSION + "/m/wallet/send_verify_code";

	/**
	 * 短信验证码登录
	 */
	public static final String M_WALLET_LOGIN_WITH_VERIFY_CODE = VERSION + "/m/wallet/login_with_verify_code";
	/**
	 * 高级钱包渠道信息
	 */
	public static final String WALLET_TUNNEL_INFO = VERSION + "/m/senior/wallet/tunnel_info";
	/**
	 * 升级高级钱包
	 */
	public static final String WALLET_UPGRADE = VERSION + "/m/wallet/upgrade_wallet";
	/**
	 * 高级钱包认证验证码
	 */
	public static final String WALLET_SENIOR_SMS_VERIFY_CODE = VERSION + "/m/senior/wallet/sms_verify_code";
	/**
	 * 高级钱包个人认证
	 */
	public static final String WALLET_SENIOR_PERSON_AUTHENTICATION = VERSION + "/m/senior/wallet/person_authentication";
	/**
	 * 高级钱包-用户绑定钱包身份
	 * */
	public static final String WALLET_SENIOR_PERSON_IDBIND = VERSION + "/m/senior/wallet/person_idbind";
	/**
	 * 高级钱包绑定手机
	 */
	public static final String WALLET_SENIOR_BIND_PHONE = VERSION + "/m/senior/wallet/bind_phone";

	/**
	 * 高级钱包商家资料审核（通道）
	 */
	public static final String WALLET_SENIOR_COMPANY_INFO_AUDIT = VERSION + "/m/senior/wallet/company_info_audit";

	/**
	 * 高级钱包会员协议
	 */
	public static final String WALLET_SENIOR_MEMBER_PROTOCOL = VERSION + "/m/senior/wallet/member_protocol";
	/**
	 * 高级钱包代扣协议（企业,个人商家）
	 */
	public static final String WALLET_SENIOR_BANLACE_PROTOCOL = VERSION + "/m/senior/wallet/balance_protocol";
	/**
	 * 高级钱包个人设置支付密码
	 */
	public static final String WALLET_SENIOR_PERSON_SET_PAY_PASSWORD = VERSION + "/m/senior/wallet/person_set_paypassword";
	/**
	 * 高级钱包-修改支付密码
	 */
	public static final String WALLET_SENIOR_UPDATE_PAY_PWD = VERSION + "/m/senior/wallet/update_pay_pwd";
	/**
	 * 高级钱包-修改绑定手机
	 */
	public static final String WALLET_SENIOR_UPDATE_SECURITY_TEL = VERSION + "/m/senior/wallet/update_security_tel";
	/**
	 * 高级钱包-预绑定银行卡
	 */
	public static final String WALLET_SENIOR_PRE_BIND_BANK_CARD = VERSION + "/m/senior/card/pre_bind_bank_card";
	/**
	 * 高级钱包-确认绑定银行卡
	 */
	public static final String WALLET_SENIOR_CONFIRM_BIND_CARD = VERSION + "/m/senior/card/confirm_bind_card";
	/**
	 * 高级钱包-解绑银行卡
	 */
	public static final String WALLET_SENIOR_UNBIND_CARD = VERSION + "/m/senior/card/unbind_card";
	/**
	 * 高级钱包-银行卡列表
	 */
	public static final String WALLET_SENIOR_BANK_CARD_LIST = VERSION + "/m/senior/card/list";
	/**
	 * 高级钱包个人信息
	 */
	public static final String WALLET_SENIOR_PERSON_INFO = VERSION + "/m/senior/wallet/person_info";
	/**
	 * 高级钱包企业信息
	 */
	public static final String WALLET_SENIOR_COMPANY_INFO= VERSION + "/m/senior/wallet/company_info";
	/**
	 * 高级钱包线下确认更新企业信息
	 */
	public static final String WALLET_SENIOR_UPDATE_COMPANY_INFO_OFFLINE= VERSION + "/m/senior/wallet/update_company_info_offline";
	/**
	 * 高级钱包余额明细
	 */
	public static final String WALLET_SENIOR_ORDER_DETAIL= VERSION + "/m/senior/wallet/order_detail";

	/**
	 * 云商通统一回调
	 */
	public static final String YUNST_NOTIFY = VERSION + "/yunst/notify";

	/**
	 * 云商通订单回调
	 */
	public static final String YUNST_ORDER_RECALL = VERSION + "/yunst/order_recall";

	/**
	 * 业务查询银行卡信息
	 */
	public static final String WALLET_CARD_QUERY = VERSION + "/u/wallet/query_wallet_card";

	/**
	 * 手续费报表
	 */
	public static final String REPORT_CHARGING_QUERY = VERSION + "/m/report/charging_query";

	/**
	 * 手续费明细
	 */
	public static final String REPORT_CHARGING_DETAIL = VERSION + "/m/report/charging_detail";

	/**
	 * 手续费重做
	 */
	public static final String REPORT_CHARGING_REDO = VERSION + "/m/report/charging_redo";

	/**
	 * 当月手续费报表
	 */
	public static final String REPORT_CUR_CHARGING_QUERY = VERSION + "/m/report/cur_charging_query";

}
