package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorPayServiceTest extends SpringBaseTest {

	@Autowired
	private SeniorPayService seniorPayService;

	@Autowired
	private WalletCollectExtDao walletCollectDao;

	@Spy
	@Autowired
	private YunstTpl yunstTpl;

	private Long payerWalletId = 10001L;
	private Long platWalletId = 10000L;

	/**
	 * 充值
	 */
	@Test
	public void recharge() {
		CodePay codePay = CodePay.builder()
			.payType(CollectPayType.CODEPAY.getValue())
			.authcode("134572047983132026")
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
		seniorPayService.recharge(req);
	}

	/**
	 * 代收
	 */
	@Test
	public void collect() {
		Balance balance = Balance.builder()
			.amount(2L)
			.build();
		Reciever reciever = Reciever.builder()
			.walletId(platWalletId)
			.amount(2L)
			.build();
		CollectReq req = CollectReq.builder()
			.payerWalletId(payerWalletId)
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(2L)
			.note("")
			.fee(0L)
			.validateType((byte) 0)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.recievers(Arrays.asList(reciever))
			.walletPayMethod(WalletPayMethod.builder().balance(balance).build())
			.build();
		WalletCollect collect = seniorPayService.preCollect(req);
		log.info("预代收 {}", collect);
		seniorPayService.doCollect(collect);
	}


	@Test
	public void agentPay() {
		AgentPayReq.Reciever reciever = new AgentPayReq.Reciever();
		reciever.setWalletId(platWalletId);
		reciever.setAmount(1L);
		reciever.setFeeAmount(0L);
		SettleResp resp = seniorPayService.agentPay("WC20191021829821028"
			, Arrays.asList(reciever));
		log.info("agent pay {}", resp);
	}

	@Test
	public void refund() {
		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setWalletId(platWalletId);
		refundInfo.setAmount(1L);
		WalletRefund refund = seniorPayService
			.refund("WC20191022924053256", Arrays.asList(refundInfo));
		log.info("refund {}", refund);
	}
}