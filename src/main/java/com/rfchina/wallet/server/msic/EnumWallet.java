package com.rfchina.wallet.server.msic;

import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.platform.common.utils.Valuable;

public class EnumWallet {

	/**
	 * 收款人账户类型
	 */
	public enum PayeeType implements Valuable<String> {

		PUBLIC_ACCOUNT("0", "对公账号"),
		BANKCARD("1", "卡");

		private String value;
		private String valueName;

		PayeeType(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}
	}


	/**
	 * 本行/他行标志
	 */
	public enum SysFlag implements Valuable<String> {

		SELF("0", "本行"),
		OTHER("1", "他行");

		private String value;
		private String valueName;

		SysFlag(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	/**
	 * 同城异地标志
	 */
	public enum RemitLocation implements Valuable<String> {

		SELF("0", "同城"),
		OTHER("1", "异地");

		private String value;
		private String valueName;

		RemitLocation(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	/**
	 * 交易状态
	 */
	public enum TransStatus implements Valuable<String> {
		WAIT_FILLING("0", "待补录", "该笔支付需要柜员手工补录必要支付信息"),
		WAIT_RECORD("1", "待记帐", "该笔支付已经处在等待柜员处理记账"),
		WAIT_AUDIT("2", "待复核", "该笔支付处于等待柜员处理复核阶段"),
		WAIT_AUTHORIZE_("3", "待授权", "该笔支付处于等待客户进行网银授权阶段"),
		FINISH("4", "完成", "该笔支付处理成功，客户记账成功"),
		REJECT("8", "拒绝", "该笔支付处理失败，被拒绝；如果对外支付时被人民银行退票则也会将该笔支付状态置为拒绝，同时冲回客户扣出的钱款"),
		REVOKE("9", "撤销", "该笔支付已被撤销；客户和柜员都可以对待处理的支付进行撤销动作");

		private String value;
		private String valueName;
		private String description;

		TransStatus(String value, String valueName, String description) {
			this.value = value;
			this.valueName = valueName;
			this.description = description;
		}

		@Override
		public String getValue() {
			return value;
		}

		public String getDescription() {
			return description;
		}

		public static TransStatus parse(String value) {
			return EnumUtil.parse(TransStatus.class, value);
		}
	}

	/**
	 * 流水类型
	 */
	public enum WalletLogType implements Valuable<Byte> {
		TRANSFER((byte) 1, "直接转帐"),
		PAY_IN((byte) 2, "收入"),
		PAY_OUT((byte) 3, "支出");

		private Byte value;
		private String valueName;

		WalletLogType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 交易状态。1：银行受理中，2：交易成功，3：交易失败 4: 待发送银行网关
	 */
	public enum WalletLogStatus implements Valuable<Byte> {
		PROCESSING((byte) 1, "银行受理中"),
		SUCC((byte) 2, "交易成功"),
		FAIL((byte) 3, "交易失败"),
		SENDING((byte) 4, "待发送银行网关");

		private Byte value;
		private String valueName;

		WalletLogStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}

		public String getValueName(){
			return valueName;
		}

		public static WalletLogStatus parsePuDong8804(String tranStatus){
			return null;
		}
	}
}
