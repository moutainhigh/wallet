package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.*;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.domain.model.ext.WalletCardExt;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;

import java.util.Date;
import java.util.List;

public interface WalletApi {


	/**
	 * 查询出佣结果
	 */
	List<PayStatusResp> queryWalletApply(String accessToken, String bizNo, String batchNo);

	/**
	 * 重做订单
	 */
	void redoWalletApply(String accessToken, Long walletLogId);

	/**
	 * 定时更新支付状态
	 */
	void quartzUpdate();

	/**
	 * 定时支付
	 */
	void quartzPay();

	/**
	 * 定时通知
	 */
	void quartzNotify();

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
	Pagination<WalletApply> walletApplyList(String accessToekn, Long walletId, Date startTime,
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
	WalletCardExt bindBankCard(String accessToken, Long walletId, String bankCode, String bankAccount,
							   String depositName, Integer isDef, String telephone);

	/**
	 * 银行类别列表
	 */
	List<BankClass> bankClassList();

	/**
	 * 银行地区列表
	 *
	 * @param classCode 必填，银行类型编码
	 */
	List<BankArea> bankAreaList(String classCode);

	/**
	 * 银行支行列表
	 *
	 * @param classCode 必填，银行类别编码
	 * @param areaCode 必填，地区编码
	 */
	List<Bank> bankList(String classCode, String areaCode);

	/**
	 * 银行支行信息
	 * @param bankCode
	 * @return
	 */
	BankCode bank(String bankCode);

	/**
	 * 富慧通审核个人商家钱包
	 */
	void activeWalletPerson(Long walletId, String name, Byte idType, String idNo, Long auditType);

	/**
	 * 富慧通审核企业商家钱包
	 */
	void activeWalletCompany(Long walletId, String companyName, Long auditType);

	/**
	 * 发送手机验证码
	 *
	 * @param mobile		非必填，手机号码
	 * @param type			必填，验证码类型, 1:登录, 2:身份验证
	 * @param verifyToken	必填，反作弊结果查询token
	 * @param redirectUrl 	非必填，触发图形验证码并验证成功后重定向地址
	 * @return
	 */
	ResponseValue sendVerifyCode(String accessToken, Long userId, String mobile, Integer type, String verifyToken, String redirectUrl, String ip);

	/**
	 * 通过手机验证码登录
	 *
	 * @param mobile		必填, 手机号码
	 * @param verifyCode	必填, 验证码
	 * @param type			必填, 短信类型, 1:登录当前钱包, 2:登录已开通钱包
	 * @return
	 */
	WalletUser loginWithVerifyCode(String accessToken, String mobile,String verifyCode,Integer type, String ip);


}
