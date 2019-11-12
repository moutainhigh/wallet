package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorPayServiceTest extends SpringBaseTest {

	@Autowired
	private SeniorPayService seniorPayService;

	@Autowired
	private WalletCollectExtDao walletCollectDao;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private WalletCardDao walletCardDao;

	private Long payerWalletId = 10035L;
	private Long payeeWalletId = 10035L;
	private Long platWalletId = 10000L;
	private Long cardId = 17L;


	/**
	 * 更新订单状态
	 */
	@Test
	public void updateOrderStatus() {
		String orderNo = "WO20191111418392075";
		seniorPayService.updateOrderStatus(orderNo);
	}

	/**
	 * 充值
	 */
	@Test
	public void recharge() {
		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		RechargeResp resp = seniorPayService.recharge(payerWalletId, walletCard, 1L);
		log.info("recharge.resp = {}", JsonUtil.toJSON(resp));
	}

	@Test
	public void rechargeConfirm() {
		WalletOrder order = walletOrderDao.selectByPrimaryKey(14L);
		seniorPayService.smsConfirm(order,
			"{\"sign\":\"\",\"tphtrxcrtime\":\"\",\"tphtrxid\":0,\"trxflag\":\"trx\",\"trxsn\":\"\"}",
			"575636", "113.194.30.199");
	}

	@Test
	public void withdraw() {
		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		WalletOrder withdraw = seniorPayService
			.withdraw(payerWalletId, walletCard, 1L, "http://192.168.197.75:7777/#/withdrawSuccess", "8.8.8.8");
		log.info("withdraw.resp = {}", JsonUtil.toJSON(withdraw));
	}

	/**
	 * 代收
	 */
	@Test
	public void collect() {
//		Balance balance = Balance.builder()
//			.amount(1L)
//			.build();
		CodePay codePay = CodePay.builder()
			.payType((byte) 41)
			.authcode("134998119558850474")
			.amount(10L)
			.build();
//		BankCard bankCard = BankCard.builder()
//			.payType(CollectPayType.BANKCARD.getValue())
//			.bankCardNo("6214850201481956")
//			.amount(1L)
//			.build();

		Reciever reciever = Reciever.builder()
			.walletId(payeeWalletId)
			.amount(10L)
			.build();
		CollectReq req = CollectReq.builder()
			.payerWalletId(platWalletId)
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(10L)
			.note("")
			.fee(0L)
			.validateType((byte) 0)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.recievers(Arrays.asList(reciever))
			.walletPayMethod(WalletPayMethod.builder().codePay(codePay).build())
			.build();
		WalletCollectResp collect = seniorPayService.collect(req, "", "");
		log.info("预代收 {}", JsonUtil.toJSON(collect));
	}


	@Test
	public void agentPay() {
		AgentPayReq.Reciever reciever = new AgentPayReq.Reciever();
		reciever.setWalletId(payeeWalletId);
		reciever.setAmount(10L);
		reciever.setFeeAmount(0L);
		WalletOrder order = walletOrderDao.selectByOrderNo("WC20191107748264218");
		SettleResp resp = seniorPayService
			.agentPay(order, String.valueOf(System.currentTimeMillis())
				, reciever,"note");
		log.info("agent pay {}", resp);
	}

	@Test
	public void refund() {
		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setWalletId(platWalletId);
		refundInfo.setAmount(1L);
		WalletOrder collectOrder = walletOrderDao.selectByOrderNo("WC20191101684530808");
		WalletOrder refund = seniorPayService
			.refund(collectOrder, String.valueOf(System.currentTimeMillis()),
				Arrays.asList(refundInfo));
		log.info("refund {}", refund);
	}




}