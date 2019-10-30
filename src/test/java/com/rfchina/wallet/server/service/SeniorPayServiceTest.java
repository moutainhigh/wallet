package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.BankCard;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
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

	@Autowired
	private YunstBizHandler yunstBizHandler;

	private Long payerWalletId = 10001L;
	private Long platWalletId = 10000L;

	/**
	 * 充值
	 */
	@Test
	public void recharge() {

		BankCard bankCard = BankCard.builder()
			.payType(CollectPayType.BANKCARD.getValue())
			.bankCardNo("6214850201481956")
			.amount(1L)
			.build();

//		CodePay codePay = CodePay.builder()
//			.payType(CollectPayType.CODEPAY.getValue())
//			.authcode("134535230243995898")
//			.amount(1L)
//			.build();

		RechargeReq req = RechargeReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.payerWalletId(10035L)
			.amount(1L)
			.fee(0L)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.walletPayMethod(WalletPayMethod.builder().bankCard(bankCard).build())
			.build();
		RechargeResp resp = seniorPayService.recharge(req);
		log.info("recharge.resp = {}", JsonUtil.toJSON(resp));
	}

	@Test
	public void rechargeConfirm() {
		seniorPayService.smsConfirm(1000213L,
			"{\"sign\":\"\",\"tphtrxcrtime\":\"\",\"tphtrxid\":0,\"trxflag\":\"trx\",\"trxsn\":\"\"}",
			"575636", "113.194.30.199");
	}

	@Test
	public void withdraw() {
		WithdrawReq req = WithdrawReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.payerWalletId(10035L)
			.cardId(12L)
			.amount(1L)
			.fee(0L)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.build();
		WalletOrder withdraw = seniorPayService.withdraw(req);
		log.info("withdraw.resp = {}", withdraw);
	}

	@Test
	public void withdrawConfirm() {
		seniorPayService.smsConfirm(1000207L, "","982604", "113.194.30.199");
	}

	/**
	 * 更新订单状态
	 */
	@Test
	public void updateOrderStatus(){
		String orderNo = "WR20191030789939639";
//		yunstBizHandler.updateWithdrawStatus(orderNo);
		yunstBizHandler.updateOrderStatus(orderNo);

	}


	/**
	 * 代收
	 */
	@Test
	public void collect() {
		Balance balance = Balance.builder()
			.amount(1L)
			.build();
		Reciever reciever = Reciever.builder()
			.walletId(platWalletId)
			.amount(1L)
			.build();
		CollectReq req = CollectReq.builder()
			.payerWalletId(payerWalletId)
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
		SettleResp resp = seniorPayService.agentPay("WC20191021829821028","test"
			, Arrays.asList(reciever));
		log.info("agent pay {}", resp);
	}

	@Test
	public void refund() {
		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setWalletId(platWalletId);
		refundInfo.setAmount(1L);
		WalletOrder refund = seniorPayService
			.refund("WC20191022924053256", "", Arrays.asList(refundInfo));
		log.info("refund {}", refund);
	}


}