package com.rfchina.wallet.server.bank.pudong.domain.request;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)

public class PubPayReqBody {

	/**
	 * 指定授权客户号
	 */
	private String authMasterID;
	/**
	 * 总笔数  最多不能超过20条
	 */
	private String totalNumber;
	/**
	 * 总金额
	 */
	private String totalAmount;
	/**
	 * 包号
	 */
	private String packageNo;
	/**
	 * 支付列表
	 */
	private Lists lists;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Lists {

		private List<PubPayReq> list;
	}






}
