package com.rfchina.wallet.server.api;

import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import java.util.List;

public interface JuniorPayApi {

	/**
	 * 出佣到个人钱包
	 */
	PayInResp payIn(String accessToken, List<PayInReq> payInReqs);


}
