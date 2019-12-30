package com.rfchina.wallet.server.msic;

import com.rfchina.platform.common.utils.Valuable;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;

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
}