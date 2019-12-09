package com.rfchina.wallet.server.api;

import com.rfchina.wallet.domain.model.WalletCard;
import java.util.List;

public interface SeniorCardApi {

	/**
	 * 高级钱包-预绑定银行卡
	 *
	 * @param walletId 必填, 钱包id
	 * @param realName 必填, 姓名
	 * @param cardNo 必填, 卡号
	 * @param phone 必填, 电话
	 * @param identityNo 必填, 身份证号
	 * @param validate 非必填, 信用卡4位有效期
	 * @param cvv2 非必填, 信用卡cvv2码
	 * @param bankCode 支行编码
	 */
	String preBindBandCard(String accessToken, Long walletId, String cardNo, String realName,
		String phone, String identityNo, String validate, String cvv2, String bankCode);

	/**
	 * 高级钱包-确认绑定银行卡
	 *
	 * @param walletId 必填, 钱包id
	 * @param verifyCode 必填, 验证码
	 * @param preBindTicket 必填
	 */
	void confirmBindCard(String accessToken, Long walletId,
		String verifyCode, String preBindTicket);

	/**
	 * 高级钱包确认绑定银行卡
	 */
	void unBindCard(String accessToken, Long cardId);

	/**
	 * 高级钱包-银行卡列表
	 *
	 * @param walletId 必填, 钱包id
	 */
	List<WalletCard> listCard(String accessToken, Long walletId);
}
