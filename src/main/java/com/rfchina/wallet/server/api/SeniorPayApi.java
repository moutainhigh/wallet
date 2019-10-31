package com.rfchina.wallet.server.api;

import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import java.util.List;

public interface SeniorPayApi {

	RechargeResp recharge(String accessToken, Long walletId, Long cardId, Long amount);

	WithdrawResp withdraw(String accessToken, Long walletId, Long cardId, Long amount);

	WalletCollectResp collect(String accessToken, CollectReq req);

	void agentPay(String accessToken, String bizNo, String collectOrderNo, Reciever receivers);

	WalletOrder refund(String accessToken, String bizNo, String collectOrderNo,
		List<RefundInfo> rList);

	WalletOrder orderQuery(String accessToken, String orderNo);

	void smsConfirm(String accessToken, String preBindTicket,
		String verifyCode, String ip);

	void smsRetry(String accessToken, String ticket);
}
