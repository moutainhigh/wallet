package com.rfchina.wallet.server.msic;

import com.rfchina.platform.common.utils.Valuable;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;

public class EnumYunst {


	/**
	 * 文件类型
	 */
	public enum YunstFileType implements Valuable<Long> {
		/**
		 * 身份证
		 */
		DETAIL(1L, "明细"),
		SUMMARY(2L, "汇总"),
		;

		private Long value;
		private String valueName;

		YunstFileType(Long value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Long getValue() {
			return this.value;
		}
	}


	/**
	 * 证件类型
	 */
	public enum YunstIdType implements Valuable<Long> {
		/**
		 * 身份证
		 */
		ID_CARD(1L),
		;

		private Long value;

		YunstIdType(Long value) {
			this.value = value;
		}

		@Override
		public Long getValue() {
			return value;
		}
	}

	/**
	 * 通联提现方式 D0：D+0 到账 D1：D+1 到账 T1customized：T+1 到账，仅工作日代付 D0customized：D+0 到账，根据平台资金头寸付款 默认为 D0
	 */
	public enum EnumYunstWithdrawType implements Valuable<String> {
		D0("D0", "D+0 到账"),
		D1("D1", "D+1 到账"),
		T1_CUSTOMIZED("T1customized", "T+1 到账，仅工作日代付"),
		D0_CUSTOMIZED("D0customized", "D+0 到账，根据平台资金头寸付款");

		private String value;
		private String valueName;

		EnumYunstWithdrawType(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return this.value;
		}

	}


	/**
	 * 通联sdk返回枚举
	 */
	public enum EnumYunstResponse implements Valuable<String> {
		ALREADY_EXISTS_MEMEBER("30000", "用户已存在"),
		ALREADY_BIND_PHONE("30024", "已绑定手机"),
		ALREADY_REALNAME_AUTH("30007", "已实名验证"),
		VERIFYCODE_ERROR("20011", "验证码错误"),
		;

		private String value;
		private String valueName;

		EnumYunstResponse(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return this.value;
		}

	}

	/**
	 * 通联设备类型
	 */
	public enum EnumYunstDeviceType implements Valuable<Long> {
		MOBILE(1L, "移动"),
		PC(2L, "PC");

		private Long value;
		private String valueName;

		EnumYunstDeviceType(Long value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Long getValue() {
			return this.value;
		}

	}

	/**
	 * 订单状态 1:未支付 3:交易失败 4:交易成功 5:交易成功-发生退款 6:关闭 99:进行中
	 */
	public enum YunstOrderStatus implements Valuable<Long> {
		NO_PAY(1L, "未支付"),
		FAIL(3L, "交易失败"),
		SUCC(4L, "交易成功"),
		SUCC_REFUND(5L, "交易成功-发生退款"),
		CLOSED(6L, "关闭"),
		RUNNING(99L, "进行中"),
		;

		private Long value;
		private String valueName;

		YunstOrderStatus(Long value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Long getValue() {
			return this.value;
		}

		public OrderStatus toUniStatus() {
			switch (this) {
				case SUCC:
					return OrderStatus.SUCC;
				case SUCC_REFUND:
					return OrderStatus.SUCC;
				case FAIL:
					return OrderStatus.FAIL;
				case CLOSED:
					return OrderStatus.CLOSED;
				default:
					return OrderStatus.WAITTING;
			}
		}
	}

	public enum YunstServiceName implements Valuable<String> {
		MEMBER("MemberService"),
		ORDER("OrderService"),
		MEMBER_PWD("MemberPwdService"),
		ORDER_SERVICE(" OrderService "),
		;

		private String value;

		YunstServiceName(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	public enum YunstMethodName implements Valuable<String> {
		VERIFY_RESULT("verifyResult", ""),
		SIGN_CONTRACT("signContract", ""),
		SIGN_BALANCE_PROTOCOL("signBalanceProtocol", ""),
		CHANGE_BIND_PHONE("updatePhoneByPayPwd", ""),
		SET_PAY_PWD("setPayPwd", ""),
		PAY("pay", ""),
		PERSON_VERIFY("setRealName", "个人实名"),
		COMPANY_VERIFY("setCompanyInfo", "企业实名"),
		RECHARGE("depositApply", "充值"),
		WITHDRAW("withdrawApply", "提现"),
		CONSUME("consumeApply", "消费"),
		COLLECT("agentCollectApply", "代收"),
		AGENT_PAY("signalAgentPay", "代付"),
		REFUND("refund", "退款"),
		DEDUCTION("consumeProtocolApply", "协议消费"),
		;

		private String value;
		private String valueName;

		YunstMethodName(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}

		public String getValueName() {
			return valueName;
		}
	}

	public enum IsAuth implements Valuable<Boolean> {
		TRUE(true, "系统自动审核"),
		FALSE(false, "false：需人工审核");

		private Boolean value;
		private String valueName;

		IsAuth(Boolean value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		public Boolean getValue() {
			return value;
		}
	}

	/**
	 * 短信验证码类型
	 */
	public enum EnumVerifyCodeType implements Valuable<Integer> {

		/**
		 * 云商通绑定手机
		 */
		BIND_PHONE(9),
		UNBIND_PHONE(6);

		private Integer value;

		EnumVerifyCodeType(Integer value) {
			this.value = value;
		}

		@Override
		public Integer getValue() {
			return value;
		}
	}

	/**
	 * 终端状态： 0：未绑定，1：已绑定，2：已解绑
	 */
	public enum EnumTerminalStatus implements Valuable<Byte> {

		NULL((byte) 0),
		BIND((byte) 1),
		UNBIND((byte) 2),
		;

		private Byte value;

		EnumTerminalStatus(Byte value) {
			this.value = value;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 通道交易类型 收银宝渠道返回的交易类型
	 */
	public enum EnumTrxCode implements Valuable<String> {
		WEIXIN("VSP501", "微信支付"),
		QQ("VSP505", "手机QQ支付"),
		ALIPAY("VSP511", "支付宝支付"),
		UNION_RCODE("VSP551", "银联扫码支付"),
		BALANCE("VSP521", "收银宝-通联钱包"),
		SCAN_CREDIT("VSP011", "收银宝-扫码预消费"),
		BANK_PAY("VSP001", "收银宝-消费(银行卡)"),
		BANK_CREDIT("VSP004", "收银宝-预授权"),
		;

		private String value;
		private String valueName;

		EnumTrxCode(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}

		public CollectPayType toCollectPayType() {
			if (WEIXIN.getValue().equalsIgnoreCase(this.getValue())) {
				return CollectPayType.POS_WECHAT;
			} else if (QQ.getValue().equalsIgnoreCase(this.getValue())) {
				return CollectPayType.POS_QQ;
			} else if (ALIPAY.getValue().equalsIgnoreCase(this.getValue())) {
				return CollectPayType.POS_ALIPAY;
			} else if (UNION_RCODE.getValue().equalsIgnoreCase(this.getValue())) {
				return CollectPayType.POS_UNION_RCODE;
			} else if (BALANCE.getValue().equalsIgnoreCase(this.getValue())) {
				return CollectPayType.BALANCE;
			} else if (BANK_PAY.getValue().equalsIgnoreCase(this.getValue())) {
				return CollectPayType.POS_UNION;
			}
			return null;
		}
	}

	public enum EnumAcctType implements Valuable<String> {
		DEBIT("00", "借记卡"),
		PASSBOOK("01", "存折"),
		CREDIT("02", "信用卡"),
		SEMI_CREDIT("03", "准贷记卡"),
		PREPAID("04", "预付费卡"),
		OVERSEA("05", "境外卡"),
		ETC("99", "其他");

		private String value;
		private String valueName;

		EnumAcctType(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}
	}
}
