package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.*;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.domain.model.ext.WalletCardExt;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstApplyBindBankCardResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;
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
	void quartzDealApply();

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
		Date endTime, int limit,
		long offset, Boolean stat);

	/**
	 * 查询绑定的银行卡列表
	 *
	 * @param walletId 钱包ID
	 */
	List<WalletCard> bankCardList(String accessToken, Long walletId);

	/**
	 * 绑定银行卡
	 */
	WalletCardExt bindBankCard(String accessToken, Long walletId, String bankCode,
		String bankAccount,
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
	 */
	BankCode bank(String bankCode);

	/**
	 * 富慧通审核个人商家钱包
	 */
	void activeWalletPerson(Long walletId, String name, Byte idType, String idNo, Byte status,
		Long auditType);

	/**
	 * 富慧通审核企业商家钱包
	 */
	void activeWalletCompany(Long walletId, String companyName, Byte status, Long auditType);

	/**
	 * 发送手机验证码
	 *
	 * @param mobile 非必填，手机号码
	 * @param type 必填，验证码类型, 1:登录, 2:身份验证
	 * @param verifyToken 必填，反作弊结果查询token
	 * @param redirectUrl 非必填，触发图形验证码并验证成功后重定向地址
	 */
	ResponseValue sendVerifyCode(String accessToken, Long userId, String mobile, Integer type,
		String verifyToken,
		String redirectUrl, String ip);

	/**
	 * 通过手机验证码登录
	 *
	 * @param mobile 必填, 手机号码
	 * @param verifyCode 必填, 验证码
	 * @param type 必填, 短信类型, 1:登录当前钱包, 2:登录已开通钱包
	 */
	WalletUser loginWithVerifyCode(String accessToken, String mobile, String verifyCode,
		Integer type, String ip);

	/**
	 * 高级钱包渠道信息
	 *
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 */
	WalletChannel seniorWalletChannelInfo(String accessToken, Integer channelType, Long walletId);

	/**
	 * 升级高级钱包
	 *
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 */
	WalletChannel seniorWalletUpgrade(String accessToken, Byte source, Integer channelType,
		Long walletId);

	/**
	 * 高级钱包认证验证码
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 * @param mobile 必填, 电话
	 * @param walletId 必填, 短信验证码类型
	 */
	WalletChannel seniorWalletSmsCodeVerification(String accessToken, Byte source,
		Integer channelType, Long walletId,
		String mobile, Integer smsCodeType);

	/**
	 * 高级钱包个人修改绑定手机认证
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 * @param realName 必填, 真实姓名
	 * @param idNo 必填, 身份证
	 * @param oldPhone 必填, 电话
	 */
	String seniorWalletPersonChangeBindPhone(String accessToken, Byte source, Integer channelType,
		Long walletId, String realName, String idNo, String oldPhone);

	/**
	 * 高级钱包绑定手机
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 * @param mobile 必填, 电话
	 * @param verifyCode 必填, 短信验证码
	 */
	Long seniorWalletBindPhone(String accessToken, Byte source, Integer channelType, Long walletId,
		String mobile, String verifyCode);

	/**
	 * 高级钱包个人认证
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 * @param realName 必填, 真实姓名
	 * @param idNo 必填, 身份证
	 * @param mobile 必填, 电话
	 * @param verifyCode 必填, 短信验证码
	 */
	String seniorWalletPersonAuthentication(String accessToken, Byte source, Integer channelType,
		Long walletId, String realName, String idNo, String mobile, String verifyCode);

	/**
	 * 高级钱包商家资料审核
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param auditType 必填, 审核方式.1: 线上，2：人工
	 * @param walletId 必填, 钱包id
	 * @param companyBasicInfo 必填, 企业基本信息
	 */
	Integer seniorWalletCompanyAudit(String accessToken, Byte source, Integer channelType,
		Integer auditType, Long walletId, YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo);

	/**
	 * 高级钱包会员协议地址
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 */
	String signMemberProtocol(String accessToken, Byte source, Long walletId);

	/**
	 * 高级钱包扣款协议地址
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 */
	String signBalanceProtocol(String accessToken, Byte source, Long walletId);

	/**
	 * 高级钱包银行卡验证
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 * @param realName 必填, 姓名
	 * @param cardNo 必填, 卡号
	 * @param phone 必填, 电话
	 * @param identityNo 必填, 身份证号
	 * @param validate 非必填, 信用卡4位有效期
	 * @param cvv2 非必填, 信用卡cvv2码
	 */
	YunstApplyBindBankCardResult seniorWalletVerifyBankCard(String accessToken, Long walletId,
		Byte source, String cardNo, String realName, String phone, String identityNo,
		String validate,
		String cvv2);

	/**
	 * 高级钱包确认绑定银行卡
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 * @param transNum 必填, 验证流水号
	 * @param transDate 必填, 验证流水申请时间
	 * @param phone 必填, 电话
	 * @param validate 非必填, 信用卡4位有效期
	 * @param cvv2 非必填, 信用卡cvv2码
	 * @param verifyCode 必填, 验证码
	 */
	Long seniorWalletConfirmBindBankCard(String accessToken, Long walletId, Byte source,
		String transNum,
		String transDate, String phone, String validate, String cvv2, String verifyCode);

	/**
	 * 高级钱包确认绑定银行卡
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 * @param cardNo 必填, 卡号
	 */
	Long seniorWalletUnBindBankCard(String accessToken, Long walletId, Byte source, String cardNo);


	/**
	 * 高级钱包获取企业用户信息
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 */
	YunstMemberInfoResult.CompanyInfoResult seniorWalletGetCompanyInfo(String accessToken,
		Long walletId,
		Byte source);

	/**
	 * 高级钱包获取个人用户信息
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 */
	YunstMemberInfoResult.PersonInfoResult seniorWalletGetPersonInfo(String accessToken,
		Long walletId,
		Byte source);
}
