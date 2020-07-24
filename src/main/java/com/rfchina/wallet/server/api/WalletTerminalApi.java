package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.WalletArea;
import com.rfchina.wallet.domain.model.ext.WalletTerminalExt;

public interface WalletTerminalApi {

	/**
	 * 地区绑定终端
	 */
	void bindTerminal(String accessToken, String areaCode, String vspTermid, String creatorId,
		String creatorName);

	/**
	 * 地区绑定子商户号
	 */
	void bindVspId(String accessToken, String areaCode, String vspMerchantid, String vspCusid,
		Long proxyWalletId, String creatorId, String creatorName);

	/**
	 * 查询地区商户号列表
	 */
	Pagination<WalletArea> queryArea(String accessToken, Integer limit, Integer offset);

	/**
	 * 查询终端列表
	 */
	Pagination<WalletTerminalExt> queryTerminalExt(String accessToken, String areaCode, Long proxyWalletId,
		String vspCusid, String vspTermid, Byte status, Integer limit, Integer offset);
}
