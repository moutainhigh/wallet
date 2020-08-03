package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.WalletOrder;
import java.util.Date;

public interface SeniorOrderApi {

	void exportOrderDetail(String accessToken, Long walletId, Date beginTime, Date endTime,
		Byte tradeType, Byte status, String fileName, String uniqueCode);

	/**
	 * 高级钱包余额明细
	 *
	 * @param walletId 必填, 钱包id
	 */
	Pagination<WalletOrder> queryWalletOrderDetail(String accessToken, Long walletId, Date fromTime,
		Date endTime, Byte tradeType, Byte status, int limit, int offset, Boolean stat);
}
