package com.rfchina.wallet.server.bank.pudong.domain.request;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账户余额查询（4402）
 *
 * @author nzm
 * @Descript 该交易用于查询企业相关多个账户的当前余额信息。
 * @Example 浦发文档： <?xml version='1.0' encoding='GB2312'?>
 * <body>
 * <lists name=”acctList”>
 * <list>
 * <acctNo>12345678900000</acctNo>
 * </list>
 * </lists>
 * </body>
 */
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceReqBody {

	private Lists lists;


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AcctNo {

		/**
		 * 要查询余额的账号
		 */
		private String acctNo;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Lists {

		private List<AcctNo> list;
	}
}
