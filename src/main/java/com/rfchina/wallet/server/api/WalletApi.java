package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import java.util.Date;
import java.util.List;

public interface WalletApi {

	/**
	 * 查询钱包明细
	 */
	WalletInfoResp queryWalletInfo(String accessToken, Long walletId);

	/**
	 * 开通未审核的钱包
	 */
	Wallet createWallet(String accessToken, Byte type, String title, Byte source);

	/**
	 * 查詢钱包流水
	 *
	 * @param walletId 钱包ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 */
	Pagination<WalletLog> walletLogList(String accessToekn, Long walletId, Date startTime,
		Date endTime, int limit, long offset, Boolean stat);

	/**
	 * 查询绑定的银行卡列表
	 *
	 * @param walletId 钱包ID
	 */
	List<WalletCard> bankCardList(String accessToken, Long walletId);

	/**
	 * 绑定银行卡
	 */
	WalletCard bindBankCard(String accessToken, Long walletId, String bankCode, String bankAccount,
							String depositName, Integer isDef, String telephone);
}
