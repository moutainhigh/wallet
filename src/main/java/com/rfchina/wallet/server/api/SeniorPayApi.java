package com.rfchina.wallet.server.api;

import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import java.util.List;

public interface SeniorPayApi {

	RechargeResp recharge(String accessToken, RechargeReq req);

	void smsConfirm(String accessToken, String preBindTicket,
		String verifyCode, String ip);

	WalletOrder withdraw(String accessToken, WithdrawReq req);

	WalletCollect preCollect(String accessToken, CollectReq req);

	WalletCollectResp doCollect(String accessToken, WalletCollect walletCollect);

	void agentPay(String accessToken, String bizNo, String collectOrderNo,
		List<Reciever> receivers);

	WalletOrder refund(String accessToken, String bizNo, String collectOrderNo,
		List<RefundInfo> rList);

	WalletOrder orderQuery(String accessToken, String orderNo);
}
