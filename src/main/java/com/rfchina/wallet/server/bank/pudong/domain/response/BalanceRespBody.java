package com.rfchina.wallet.server.bank.pudong.domain.response;


import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 余额查询结果 (4402)
 * <lists name=”acctBalanceList”>
 * <list>
 * <acctNo>1234567890000000</ acctNo>
 * <masterID>1122334455</masterID>
 * < balance>23</ balance> < reserveBalance>1234.34</ reserveBalance> < freezeBalance>30.23</
 * freezeBalance> < cortrolBalance>999.00</ cortrolBalance> < canUseBalance>123.23</ canUseBalance>
 * < overdraftBalance>44</ overdraftBalance>
 * </list>
 */
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class BalanceRespBody {

	private Lists lists;

	@Data
	public static class Balance {

		/**
		 * 要查询余额的账号
		 */
		private String acctNo;
		/**
		 * 客户号
		 */
		private String masterID;
		/**
		 * 账号余额
		 */
		private String balance;
		/**
		 * 保留余额
		 */
		private String reserveBalance;
		/**
		 * 冻结余额
		 */
		private String freezeBalance;
		/**
		 * 控制余额
		 */
		private String cortrolBalance;
		/**
		 * 可用金额
		 */
		private String canUseBalance;
		/**
		 * 透支余额
		 */
		private String overdraftBalance;
	}

	@Data
	public static class Lists {

		private List<Balance> list;
	}
}
