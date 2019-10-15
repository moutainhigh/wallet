package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import java.util.Arrays;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SeniorWalletServiceTest extends SpringBaseTest {

	@Autowired
	private SeniorWalletService seniorWalletService;

	@Autowired
	private WalletCollectExtDao walletCollectDao;

	@Test
	public void preCollect() {
		Balance balance = Balance.builder()
			.amount(1L)
			.build();
		Reciever reciever = Reciever.builder()
			.walletId(10000L)
			.amount(1L)
			.build();
		CollectReq req = CollectReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(1L)
			.note("")
			.fee(0L)
			.validateType((byte) 0)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.recievers(Arrays.asList(reciever))
			.walletPayMethod(WalletPayMethod.builder().balance(balance).build())
			.build();
		seniorWalletService.preCollect("", req);
	}

	@Test
	public void doCollect() {
		WalletCollect walletCollect = walletCollectDao.selectByPrimaryKey(2L);
		seniorWalletService.doCollect("", walletCollect);
	}

	@Test
	public void recharge() {
		CodePay codePay = CodePay.builder()
			.payType(CollectPayType.CODEPAY.getValue())
			.authcode("134659812413794300")
			.amount(1L)
			.build();
		RechargeReq req = RechargeReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(1L)
			.fee(0L)
			.validateType((byte) 0)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.walletPayMethod(WalletPayMethod.builder().codePay(codePay).build())
			.build();
		seniorWalletService.recharge("", req);
	}
}