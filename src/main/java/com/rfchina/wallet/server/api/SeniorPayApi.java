package com.rfchina.wallet.server.api;

import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import java.util.List;

public interface SeniorPayApi {

	void recharge(String accessToken, RechargeReq req);

	WalletCollect preCollect(String accessToken, CollectReq req);

	WalletCollectResp doCollect(String accessToken, WalletCollect walletCollect);

	WalletCollect queryCollect(String accessToken, String collectOrderNo);

	void agentPay(String accessToken, String collectOrderNo, List<Reciever> receivers);

	WalletClearing agentPayQuery(String accessToken, String payOrderNo);

	WalletRefund refund(String accessToken, String collectOrderNo, List<RefundInfo> rList);

	WalletRefund refundQuery(String accessToken, String refundOrderNo);
}
