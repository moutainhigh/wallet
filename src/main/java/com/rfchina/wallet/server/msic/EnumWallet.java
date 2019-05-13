package com.rfchina.wallet.server.msic;

import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.platform.common.utils.Valuable;

public class EnumWallet {

	/**
	 * 钱包状态: 1:待审核，2：激活,3：禁用
	 */
	public enum WalletStatus implements Valuable<Byte> {
		WAIT_AUDIT((byte) 1, "待审核"),
		ACTIVE((byte) 2, "激活"),
		UNVALID((byte) 3, "禁用");

		private Byte value;
		private String valueName;

		WalletStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

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
	public enum TransStatus8800 implements Valuable<String> {
		WAIT_FILLING("0", "待补录", "该笔支付需要柜员手工补录必要支付信息"),
		WAIT_RECORD("1", "待记帐", "该笔支付已经处在等待柜员处理记账"),
		WAIT_AUDIT("2", "待复核", "该笔支付处于等待柜员处理复核阶段"),
		WAIT_AUTHORIZE_("3", "待授权", "该笔支付处于等待客户进行网银授权阶段"),
		FINISH("4", "完成", "该笔支付处理成功，客户记账成功"),
		REJECT("8", "拒绝", "该笔支付处理失败，被拒绝"),
		REVOKE("9", "撤销", "该笔支付已被撤销"),
		UNKNOWN("A", "未知状态", "接口文档未记录的状态");

		private String value;
		private String valueName;
		private String description;

		TransStatus8800(String value, String valueName, String description) {
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

		public static TransStatus8800 parse(String value) {
			return EnumUtil.parse(TransStatus8800.class, value);
		}
	}

	/**
	 * 授权状态
	 */
	public enum TransStatusDO48 implements Valuable<String> {
		SUCC("0", "交易成功"),
		COMMU_ERROR("1", "通讯失败"),
		HOST_REFUSE("2", "主机拒绝"),
		EBANK_REFUSE("3", "网银拒绝"),
		AUDIT_REFUSE("4", "授权拒绝"),
		WAIT_AUDIT("5", "交易录入，待授权"),
		WAIT_DUIL("9", "待处理"),
		COMMITED("Z", "交易提交成功"),
		COMMIT_ERROR("Y", "交易提交不成功");

		private String value;
		private String valueName;

		TransStatusDO48(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}

		public boolean isEndStatus() {
			return SUCC.getValue().equals(value)
				|| HOST_REFUSE.getValue().equals(value)
				|| EBANK_REFUSE.getValue().equals(value)
				|| AUDIT_REFUSE.getValue().equals(value);
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
	 * 交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销
	 */
	public enum WalletLogStatus implements Valuable<Byte> {
		SENDING((byte) 1, "待发送银行网关"),
		PROCESSING((byte) 2, "银行受理中"),
		SUCC((byte) 3, "交易成功"),
		FAIL((byte) 4, "交易失败"),
		REVOKE((byte) 5, "撤销");

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

		public String getValueName() {
			return valueName;
		}

		/**
		 * 0-待补录：该笔支付需要柜员手工补录必要支付信息； 1-待记帐：该笔支付已经处在等待柜员处理记账； 2-待复核：该笔支付处于等待柜员处理复核阶段；
		 * 3-待授权：该笔支付处于等待客户进行网银授权阶段； 4-完成：该笔支付处理成功，客户记账成功； 8-拒绝：该笔支付处理失败，被拒绝；如果对外支付时被人民银行退票则也会将该笔支付状态置为拒绝，同时冲回客户扣出的钱款；
		 * 9-撤销：该笔支付已被撤销；客户和柜员都可以对待处理的支付进行撤销动作；
		 */
		public static WalletLogStatus parsePuDong8804(String tranStatus) {
			switch (tranStatus) {
				case "4":
					return SUCC;
				case "8":
					return FAIL;
				case "9":
					return REVOKE;
				default:
					return PROCESSING;
			}
		}

		/**
		 * 0：成功 1：失败 2：处理中
		 */
		public static WalletLogStatus parsePuDongAQ54(String tranStatus) {
			switch (tranStatus) {
				case "0":
					return SUCC;
				case "1":
					return FAIL;
				case "2":
					return PROCESSING;
				default:
					return null;
			}
		}
	}

	/**
	 * 钱包类型， 1：企业钱包，2：个人钱包
	 */
	public enum WalletType implements Valuable<Byte> {
		COMPANY((byte) 1, "企业钱包"),
		PERSON((byte) 2, "个人钱包");

		private Byte value;
		private String valueName;

		WalletType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 个人用户
	 */
	public enum WalletSource implements Valuable<Byte> {
		FHT_CORP((byte) 1, "富慧通-企业商家"),
		FHT_PERSON((byte) 2, "富慧通-个人商家"),
		USER((byte) 3, "个人用户");

		private Byte value;
		private String valueName;

		WalletSource(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 交易类型.1:代收 2:代付
	 */
	public enum AQTransType implements Valuable<Byte> {
		COLLECT((byte) 1, "代收"),
		TO_PAY((byte) 2, "代付");

		private Byte value;
		private String valueName;

		AQTransType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 收付款人对公对私标志 0:对公 1:对私
	 */
	public enum AQPayeeType implements Valuable<String> {
		PUBLIC("0", "对公"),
		PRIVATE("1", "对私");

		private String value;
		private String valueName;

		AQPayeeType(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}
	}


	/**
	 * 批次处理状态 0:成功； 1:失败； 2:处理中； 3:异常（需管理端处理）
	 */
	public enum TransStatusAQ53 implements Valuable<Byte> {
		SUCC((byte) 0, "成功"),
		FAIL((byte) 1, "失败"),
		DEALING((byte) 2, "处理中"),
		EXEPTION((byte) 3, "异常");

		private Byte value;
		private String valueName;

		TransStatusAQ53(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}

		public String getValueName() {
			return valueName;
		}
	}

	/**
	 * 明细状态 0：成功 1：失败 2：处理中
	 */
	public enum TransStatusAQ54 implements Valuable<Byte> {
		SUCC((byte) 0, "成功"),
		FAIL((byte) 1, "失败"),
		DEALING((byte) 2, "处理中");

		private Byte value;
		private String valueName;

		TransStatusAQ54(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}

		public String getValueName() {
			return valueName;
		}
	}


	/**
	 * 银行卡卡类型 0：对公帐号 1：卡 2：活期一本通 8：活期存折 9：内部帐/表外帐
	 */
	public enum AQCardType implements Valuable<String> {
		PUBLIC("0", "对公帐号"),
		BANKCARD("1", "卡");

		private String value;
		private String valueName;

		AQCardType(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	/**
	 * 关联接口方法，2：银企直连AQ52，1：银企直连8800
	 */
	public enum GatewayMethod implements Valuable<Byte> {
		PUDONG_8800((byte) 1, "银企直连8800"),
		PUDONG_AQ52((byte) 2, "银企直连AQ52");

		private Byte value;
		private String valueName;

		GatewayMethod(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	public enum LOCKSTATUS implements Valuable<Byte> {
		UNLOCK((byte) 1, "未锁"),
		LOCKED((byte) 2, "锁定");

		private Byte value;
		private String valueName;

		LOCKSTATUS(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}
}
