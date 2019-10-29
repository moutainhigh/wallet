package com.rfchina.wallet.server.api;

import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletWithdraw;
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

	void rechargeConfirm(String accessToken, String preBindTicket,
		String verifyCode, String ip);

	WalletWithdraw withdraw(String accessToken, WithdrawReq req);

	WalletCollect preCollect(String accessToken, CollectReq req);

	WalletCollectResp doCollect(String accessToken, WalletCollect walletCollect);

	WalletCollect queryCollect(String accessToken, String collectOrderNo);

	void agentPay(String accessToken, String collectOrderNo, List<Reciever> receivers);

	WalletClearing agentPayQuery(String accessToken, String payOrderNo);

	WalletRefund refund(String accessToken, String collectOrderNo, List<RefundInfo> rList);

	WalletRefund refundQuery(String accessToken, String refundOrderNo);
}
