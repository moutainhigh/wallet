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
	public enum TransStatus8804 implements Valuable<String> {
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

		TransStatus8804(String value, String valueName, String description) {
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

		public static TransStatus8804 parse(String value) {
			return EnumUtil.parse(TransStatus8804.class, value);
		}

		public boolean isEndStatus() {
			return FINISH.getValue().equals(this.getValue())
				|| REJECT.getValue().equals(this.getValue())
				|| REVOKE.getValue().equals(this.getValue());
		}

	}

	/**
	 * 包授权状态
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

		public String getValueName() {
			return valueName;
		}

		public boolean isEndStatus() {
			return SUCC.getValue().equals(value)
				|| HOST_REFUSE.getValue().equals(value)
				|| EBANK_REFUSE.getValue().equals(value)
				|| AUDIT_REFUSE.getValue().equals(value)
				|| COMMIT_ERROR.getValue().equals(value);
		}
	}

	/**
	 * 明细授权状态
	 */
	public enum TransStatusDO49 implements Valuable<String> {
		WAIT_AUDIT("0", "待授权"),
		HAS_AUDIT("1", "已授权"),
		REFUSE("5", "授权拒绝");

		private String value;
		private String valueName;

		TransStatusDO49(String value, String valueName) {
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

	/**
	 * 交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销
	 */
	public enum WalletApplyStatus implements Valuable<Byte> {
		WAIT_SEND((byte) 1, "待发送银行网关"),
		PROCESSING((byte) 2, "银行受理中"),
		SUCC((byte) 3, "交易成功"),
		FAIL((byte) 4, "交易失败(确切失败)"),
		REVOKE((byte) 5, "撤销"),
		WAIT_DEAL((byte) 6, "待处理"),
		REDO((byte) 7, "等待重新发起");

		private Byte value;
		private String valueName;

		WalletApplyStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		public boolean isEndStatus() {
			return this.value.byteValue() == SUCC.getValue().byteValue()
				|| this.value.byteValue() == FAIL.getValue().byteValue()
				|| this.value.byteValue() == REVOKE.getValue().byteValue();
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
		public static WalletApplyStatus parsePuDong8804(String tranStatus) {
			switch (tranStatus) {
				case "4":
					return SUCC;
				case "9":
					return REVOKE;
				default:
					return PROCESSING;
			}
		}

		/**
		 * 0：成功 1：失败 2：处理中
		 */
		public static WalletApplyStatus parsePuDongAQ54(String tranStatus) {
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

	public enum LockStatus implements Valuable<Byte> {
		UNLOCK((byte) 1, "未锁"),
		LOCKED((byte) 2, "锁定");

		private Byte value;
		private String valueName;

		LockStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	public enum NotifyType implements Valuable<Byte> {
		DEVELOPER((byte) 1),
		BUSINESS((byte) 2);

		private Byte value;

		NotifyType(byte value) {
			this.value = value;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	public enum LancherType implements Valuable<Byte> {
		SYS((byte) 1, "系统发起"),
		USER((byte) 2, "用户发起");

		private Byte value;
		private String valueName;

		LancherType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 支付渠道 1：钱包余额 2：微信 4：支付宝 8：银行卡
	 */
	public enum ChannelType implements Valuable<Byte> {
		BALANCE((byte) 1, "钱包余额"),
		WECHAT((byte) 2, "微信"),
		ALIPAY((byte) 4, "支付宝"),
		CODEPAY((byte) 8, "统一刷卡"),
		BANKCARD((byte) 16, "银行卡");

		private Byte value;
		private String valueName;

		ChannelType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	public enum GwProgress implements Valuable<Byte> {
		WAIT_SEND((byte) 1, "待发送"),
		HAS_SEND((byte) 2, "已发送"),
		HAS_RESP((byte) 3, "已接收结果");

		private Byte value;
		private String valueName;

		GwProgress(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 订单状态。 2：进行中，3：交易成功，4：交易失败
	 */
	public enum OrderStatus implements Valuable<Byte> {
		WAITTING((byte) 2, "进行中"),
		SUCC((byte) 3, "交易成功"),
		FAIL((byte) 4, "交易失败"),
		CLOSED((byte) 5, "交易关闭（超时或其他）"),
		;

		private Byte value;
		private String valueName;

		OrderStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}

		public boolean isEndStatus() {
			return this.value.byteValue() == SUCC.getValue().byteValue()
				|| this.value.byteValue() == FAIL.getValue().byteValue()
				|| this.value.byteValue() == CLOSED.getValue().byteValue();
		}

		/**
		 * 0-待补录：该笔支付需要柜员手工补录必要支付信息； 1-待记帐：该笔支付已经处在等待柜员处理记账； 2-待复核：该笔支付处于等待柜员处理复核阶段；
		 * 3-待授权：该笔支付处于等待客户进行网银授权阶段； 4-完成：该笔支付处理成功，客户记账成功； 8-拒绝：该笔支付处理失败，被拒绝；如果对外支付时被人民银行退票则也会将该笔支付状态置为拒绝，同时冲回客户扣出的钱款；
		 * 9-撤销：该笔支付已被撤销；客户和柜员都可以对待处理的支付进行撤销动作；
		 */
		public static OrderStatus parsePuDong8804(String tranStatus) {
			switch (tranStatus) {
				case "4":
					return SUCC;
				case "9":
					return CLOSED;
				default:
					return WAITTING;
			}
		}
	}

	/**
	 * 钱包余额 21：微信小程序 22：微信原生APP 23：微信原生H5 24：微信JS支付(公众号) 25：微信扫码支付(正扫) 31：支付宝扫码支付(正扫)
	 * 32：支付宝JS支付(生活号) 33：支付宝原生
	 */
	public enum CollectPayType implements Valuable<Byte> {
		BALANCE((byte) 11, "余额"),
		WECHAT_MINIPROGROGRAM((byte) 21, "微信小程序支付（收银宝）"),
		WECHAT_APPOPEN((byte) 22, "微信原生APP支付"),
		WECHAT_H5OPEN((byte) 23, "微信原生H5支付"),
		WECHAT_WechatPublic((byte) 24, "微信JS支付(公众号)"),
		WECHAT_ScanWeixin((byte) 25, "微信扫码支付(正扫)"),
		ALIPAY_ScanAlipay((byte) 31, "支付宝扫码支付(正扫)"),
		ALIPAY_Service((byte) 32, "支付宝JS支付(生活号)"),
		ALIPAY_AppOpen((byte) 33, "支付宝原生"),
		CODEPAY((byte) 41, "扫码支付"),
		BANKCARD((byte) 51, "银行卡快捷支付"),
		;

		private Byte value;
		private String valueName;

		CollectPayType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

//	/**
//	 * 清算业务状态。1：待清算 2：已清算
//	 */
//	public enum ClearingStatus implements Valuable<Byte> {
//		WAITING((byte) 1, "未清算"),
//		CLEARED((byte) 2, "已清算"),
//		FAIL((byte) 3, "交易失败"),
//		;
//
//		private Byte value;
//		private String valueName;
//
//		ClearingStatus(Byte value, String valueName) {
//			this.value = value;
//			this.valueName = valueName;
//		}
//
//		@Override
//		public Byte getValue() {
//			return value;
//		}
//	}

//	/**
//	 * 分帐状态。1：未清算 2：部分清算 3：已清算
//	 */
//	public enum ClearInfoStatus implements Valuable<Byte> {
//		WAITING((byte) 1, "未清算"),
//		PARTAL((byte) 2, "部分清算"),
//		FINISH((byte) 3, "已清算");
//
//		private Byte value;
//		private String valueName;
//
//		ClearInfoStatus(Byte value, String valueName) {
//			this.value = value;
//			this.valueName = valueName;
//		}
//
//		@Override
//		public Byte getValue() {
//			return value;
//		}
//	}

//	/**
//	 * 退款状态。1：未退款 2：已退款 3:交易失败
//	 */
//	public enum RefundStatus implements Valuable<Byte> {
//		WAITING((byte) 1, "未退款"),
//		REFUNDED((byte) 2, "已退款"),
//		FAIL((byte) 3, "交易失败");
//
//		private Byte value;
//		private String valueName;
//
//		RefundStatus(Byte value, String valueName) {
//			this.value = value;
//			this.valueName = valueName;
//		}
//
//		@Override
//		public Byte getValue() {
//			return value;
//		}
//	}

	/**
	 * 退款方式
	 */
	public enum RefundType implements Valuable<String> {
		D1("D1", "D+1 14:30 向渠道发起退款"),
		D0("D0", "D+0 实时向渠道发起退款");

		private String value;
		private String valueName;

		RefundType(String value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public String getValue() {
			return this.value;
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
		VERIFY_RESULT("verifyResult"),
		SIGN_CONTRACT("signContract"),
		SIGN_BALANCE_PROTOCOL("signBalanceProtocol"),
		CHANGE_BIND_PHONE("updatePhoneByPayPwd"),
		SET_PAY_PWD("setPayPwd"),
		PAY("pay"),
		;

		private String value;

		YunstMethodName(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
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

	/**
	 * 进度。1：待发送 2：已发送 3：已接收结果',
	 */
	public enum UniProgress implements Valuable<Byte> {
		WAIT_SEND((byte) 1, "待发送"),
		SENDED((byte) 2, "已发送"),
		RECEIVED((byte) 3, "已接收结果"),
		;

		private Byte value;
		private String valueName;

		UniProgress(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return this.value;
		}

	}

	/**
	 * 通联sdk返回枚举
	 */
	public enum EnumYunstResponse implements Valuable<String> {
		ALREADY_EXISTS_MEMEBER("30000","用户已存在"),
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
	 * 通联银行卡/账户属性
	 */
//	public enum EnumYunstCardPro implements Valuable<Long> {
//		PERSON(0L, "个人银行卡"),
//		ENTERPRICE(1L, "企业对公账户");
//
//		private Long value;
//		private String valueName;
//
//		EnumYunstCardPro(Long value, String valueName) {
//			this.value = value;
//			this.valueName = valueName;
//		}
//
//		@Override
//		public Long getValue() {
//			return this.value;
//		}
//
//	}

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
	 * 业务标签
	 */
	public enum EnumBizTag implements Valuable<Byte> {
		REFUND((byte) 1, "已退款"),
		RECORD((byte) 2, "已记账");

		private Byte value;
		private String valueName;

		EnumBizTag(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return this.value;
		}

		public boolean contains(Byte bizTag) {
			return (this.value.byteValue() & bizTag.byteValue()) > 0;
		}

		public byte and(Byte bizTag) {
			return (byte) (this.value.byteValue() | bizTag.byteValue());
		}
	}

	/**
	 * 借贷
	 */
	public enum DebitType implements Valuable<Byte> {
		DEBIT((byte) 1, "记入借方"),
		CREDIT((byte) 2, "记入贷方");

		private Byte value;
		private String valueName;

		DebitType(byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
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
	 * 脏数据标识 1：正常 2：
	 */
	public enum DirtyType implements Valuable<Byte> {
		NORMAL((byte) 1, "正常"),
		DIRTY((byte) 2, "脏数据"),
		;

		private Byte value;
		private String valueName;

		DirtyType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return this.value;
		}
	}

	/**
	 * 对账结果状态
	 */
	public enum BalanceResultStatus implements Valuable<Byte> {
		SUCC((byte) 1, "对账成功"),
		AMOUNT_NOT_MATCH((byte) 2, "金额不匹配"),
		TUNNEL_MORE((byte) 3, "通道条目多"),
		WALLET_MORE((byte) 4, "钱包条目多"),
		;

		private Byte value;
		private String valueName;

		BalanceResultStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return this.value;
		}
	}

	/**
	 * 对账任务状态
	 */
	public enum BalanceJobStatus implements Valuable<Byte> {
		RUNNING((byte) 1, "进行中"),
		SUCC((byte) 2, "对账成功"),
		;

		private Byte value;
		private String valueName;

		BalanceJobStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return this.value;
		}
	}

	/**
	 * 卡属性 0：个人银行卡 1：企业对公账户
	 */
	public enum CardPro implements Valuable<Byte> {
		PERSON((byte) 0, "个人银行卡"),
		COMPANY((byte) 1, "企业对公账户");

		private Byte value;
		private String valueName;

		CardPro(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * Gateway账户类型
	 */
	public enum GwPayeeType implements Valuable<Byte> {

		COMPANY((byte) 1, "对公账号"),
		PERSON((byte) 2, "个人账户");

		private Byte value;
		private String valueName;

		GwPayeeType(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 财务转账状态
	 */
	public enum FinanceSubStatus implements Valuable<Byte> {
		NORMAL((byte) 0, "默认"),
		WAIT_DEAL((byte) 1, "待人工处理"),
		WAIT_REDO((byte) 2, "等待重新发起");

		private Byte value;
		private String valueName;

		FinanceSubStatus(Byte value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Byte getValue() {
			return value;
		}
	}

	/**
	 * 企业信息审核状态
	 */
	public enum YunstCompanyInfoAuditStatus implements Valuable<Long> {
		SUCCESS( 2L, "审核通过"),
		FAIL(3L, "审核失败");

		private Long value;
		private String valueName;

		YunstCompanyInfoAuditStatus(Long value, String valueName) {
			this.value = value;
			this.valueName = valueName;
		}

		@Override
		public Long getValue() {
			return value;
		}
	}
}
