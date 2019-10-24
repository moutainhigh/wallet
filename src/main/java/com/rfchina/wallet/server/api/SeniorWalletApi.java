package com.rfchina.wallet.server.api;

import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;

public interface SeniorWalletApi {



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
	Wallet seniorWalletBindPhone(String accessToken, Byte source, Integer channelType, Long walletId,
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
	WalletChannel seniorWalletCompanyAudit(String accessToken, Byte source, Integer channelType,
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
	 * 高级钱包个人设置支付密码地址
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 * @param phone 必填, 绑定手机
	 * @param name 必填,姓名
	 * @param identityNo 必填,身份证号
	 */
	String personSetPayPassword(String accessToken, Byte source, Long walletId, String phone,
		String name, String identityNo);

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
