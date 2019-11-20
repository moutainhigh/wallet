package com.rfchina.wallet.server.api.impl;

import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.DeductionReq;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorPayApiImplTest extends SpringApiTest {


	private Long payerWalletId = 10035L;
	private Long payeeWalletId = 10001L;
	private Long platWalletId = 10000L;
	private Long cardId = 12L;


	@Autowired
	private SeniorPayApi seniorPayApi;

	@Test
	public void collect() {
		CodePay codePay = CodePay.builder()
			.payType((byte) 41)
			.authcode("134841889691749460")
			.amount(3L)
			.build();

		Reciever reciever1 = Reciever.builder()
			.walletId(platWalletId)
			.amount(1L)
			.build();
		Reciever reciever2 = Reciever.builder()
			.walletId(payeeWalletId)
			.amount(2L)
			.build();
		CollectReq req = CollectReq.builder()
			.payerWalletId(payerWalletId)
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(3L)
			.note("")
			.fee(0L)
			.validateType((byte) 0)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.recievers(Arrays.asList(reciever1, reciever2))
			.walletPayMethod(WalletPayMethod.builder().codePay(codePay).build())
			.build();

		WalletCollectResp resp = seniorPayApi.collect(super.accessToken, req, null, null, null);
		log.info("collect resp = {}", JsonUtil.toJSON(resp));
	}

	@Test
	public void agentPay() {
		String collectOrderNo = "WC20191106391255264";

		AgentPayReq.Reciever reciever = new AgentPayReq.Reciever();
		reciever.setWalletId(platWalletId);
		reciever.setAmount(1L);
		reciever.setFeeAmount(0L);
		seniorPayApi.agentPay(super.accessToken, String.valueOf(System.currentTimeMillis()),
			collectOrderNo, reciever, "", null);

		reciever.setWalletId(payeeWalletId);
		reciever.setAmount(2L);
		reciever.setFeeAmount(0L);
		seniorPayApi.agentPay(super.accessToken, String.valueOf(System.currentTimeMillis()),
			collectOrderNo, reciever, "", null);
	}

	@Test
	public void deduction() {

		Balance balance = Balance.builder()
			.amount(1L)
			.build();
		DeductionReq req = DeductionReq.builder()
			.payerWalletId(payeeWalletId)
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(1L)
			.note("")
			.fee(0L)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.walletPayMethod(WalletPayMethod.builder().balance(balance).build())
			.build();
		WalletCollectResp resp = seniorPayApi.deduction(super.accessToken, req, null);
		log.info("Deduction resp = {}", JsonUtil.toJSON(resp));
	}

	@Test
	public void smsConfirm() {
		seniorPayApi.smsConfirm(super.accessToken, "021013e9-12a5-4267-9caa-e485d9972038", "669805",
			"192.168.197.28");
	}
}