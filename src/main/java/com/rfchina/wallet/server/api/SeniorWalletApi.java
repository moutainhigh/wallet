package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;
import java.util.Date;

public interface SeniorWalletApi {


	/**
	 * 高级钱包渠道信息
	 *
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 */
	WalletTunnel seniorWalletTunnelInfo(String accessToken, Byte channelType, Long walletId);

	/**
	 * 升级高级钱包
	 *
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 */
	WalletTunnel seniorWalletUpgrade(String accessToken, Byte source, Integer channelType,
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
	WalletTunnel seniorWalletSmsCodeVerification(String accessToken, Byte source,
		Integer channelType, Long walletId, String mobile, Integer smsCodeType);

	/**
	 * 高级钱包个人修改绑定手机认证
	 *
	 * @param walletId 必填, 钱包id
	 */
	String updateSecurityTel(String accessToken, Long walletId, String jumpUrl);

	/**
	 * 高级钱包绑定手机
	 *
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 * @param mobile 必填, 电话
	 * @param verifyCode 必填, 短信验证码
	 */
	Wallet seniorWalletBindPhone(String accessToken, Integer channelType, Long walletId,
		String mobile, String verifyCode);

	/**
	 * 高级钱包个人认证
	 *
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param walletId 必填, 钱包id
	 * @param realName 必填, 真实姓名
	 * @param idNo 必填, 身份证
	 * @param mobile 必填, 电话
	 * @param verifyCode 必填, 短信验证码
	 * @param jumpUrl 必填,前端跳转地址
	 */
	String seniorWalletPersonAuthentication(String accessToken, Integer channelType,
		Long walletId, String realName, String idNo, String mobile, String verifyCode,
		String jumpUrl);

	/**
	 * 高级钱包商家资料审核
	 *
	 * @param channelType 必填, 渠道类型.1: 浦发银企直连，2：通联云商通
	 * @param auditType 必填, 审核方式.1: 线上，2：人工
	 * @param walletId 必填, 钱包id
	 * @param companyBasicInfo 必填, 企业基本信息
	 */
	WalletTunnel seniorWalletCompanyAudit(String accessToken, Integer channelType,
		Integer auditType, Long walletId, YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo);

	/**
	 * 高级钱包会员协议地址
	 *
	 * @param walletId 必填, 钱包id
	 * @param jumpUrl 必填,前端跳转地址
	 */
	String signMemberProtocol(String accessToken, Long walletId, String jumpUrl);

	/**
	 * 高级钱包扣款协议地址
	 *
	 * @param walletId 必填, 钱包id
	 * @param jumpUrl 必填,前端跳转地址
	 */
	String signBalanceProtocol(String accessToken, Long walletId, String jumpUrl);

	/**
	 * 高级钱包个人设置支付密码地址
	 *
	 * @param walletId 必填, 钱包id
	 * @param jumpUrl 必填,前端跳转地址
	 */
	String personSetPayPassword(String accessToken, Long walletId, String jumpUrl);

	/**
	 * 重置支付密码
	 */
	String updatePayPwd(String accessToken, Long walletId, String jumpUrl);

	/**
	 * 高级钱包获取企业用户信息
	 *
	 * @param walletId 必填, 钱包id
	 */
	YunstMemberInfoResult.CompanyInfoResult seniorWalletGetCompanyInfo(String accessToken,
		Long walletId, Boolean isManualRefresh);

	/**
	 * 高级钱包获取个人用户信息
	 *
	 * @param walletId 必填, 钱包id
	 */
	YunstMemberInfoResult.PersonInfoResult seniorWalletGetPersonInfo(String accessToken,
		Long walletId);

	/**
	 * 高级钱包余额明细
	 *
	 * @param walletId 必填, 钱包id
	 */
	Pagination<WalletOrder> queryWalletOrderDetail(String accessToken, Long walletId, Date fromTime,
		Date toTime, Integer tradeType, Integer status, String orderNo, String bizNo, int limit,
		int offset, Boolean stat);
}
