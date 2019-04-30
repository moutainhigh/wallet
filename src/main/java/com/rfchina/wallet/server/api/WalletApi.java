package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;

import java.util.Date;
import java.util.List;

public interface WalletApi {


	/**
	 * 查询出佣结果
	 */
	List<PayStatusResp> query(String accessToken, String bizNo, String batchNo);

	/**
	 * 定时更新支付状态
	 */
	void quartzUpdate();

	/**
	 * 定时支付
	 */
	void quartzPay();

	/**
	 * 查询钱包明细
	 */
	WalletInfoResp queryWalletInfo(String accessToken, Long walletId);

	/**
	 * 查询钱包明细
	 */
	WalletInfoResp queryWalletInfoByUserId(String accessToken, Long userId);

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

	/**
	 * 银行类别列表
	 * @return
	 */
	List<BankClass> bankClassList();

	/**
	 * 银行地区列表
	 * @param classCode		必填，银行类型编码
	 * @return
	 */
	List<BankArea> bankAreaList(String classCode);

	/**
	 * 银行支行列表
	 * @param classCode		必填，银行类别编码
	 * @param areaCode		必填，地区编码
	 * @return
	 */
	List<Bank> bankList(String classCode, String areaCode);


}
