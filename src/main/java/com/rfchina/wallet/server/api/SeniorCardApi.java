package com.rfchina.wallet.server.api;

public interface SeniorCardApi {

	/**
	 * 高级钱包-预绑定银行卡
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
	String preBindBandCard(String accessToken, Long walletId,
		Byte source, String cardNo, String realName, String phone, String identityNo,
		String validate,
		String cvv2);

	/**
	 * 高级钱包-确认绑定银行卡
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 * @param verifyCode 必填, 验证码
	 * @param preBindTicket 必填
	 */
	Long confirmBindCard(String accessToken, Long walletId, Byte source,
		String verifyCode, String preBindTicket);

	/**
	 * 高级钱包确认绑定银行卡
	 *
	 * @param source 必填, 钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
	 * @param walletId 必填, 钱包id
	 * @param cardNo 必填, 卡号
	 */
	Long unBindCard(String accessToken, Long walletId, Byte source, String cardNo);
}
