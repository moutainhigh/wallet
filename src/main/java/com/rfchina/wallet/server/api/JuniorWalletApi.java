package com.rfchina.wallet.server.api;

import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import java.util.List;

public interface JuniorWalletApi {

	/**
	 * 出佣到个人钱包
	 */
	PayInResp payIn(String accessToken, List<PayInReq> payInReqs);

	/**
	 * 查询出佣结果
	 */
	List<PayStatusResp> query(String accessToken, String elecChequeNo, String acceptNo);

	/**
	 * 定时更新支付状态
	 */
	void quartzUpdate();
}
