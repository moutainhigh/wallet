package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.misc.Tuple;
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
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.BankCard;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Pos;
import com.rfchina.wallet.server.model.ext.DeductionReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectRoleType;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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

	private Long payerWalletId = 301L;
	private Long payeeWalletId = 305L;
	//	private Long payeeWalletId = 314L;
	private Long platWalletId = 299L;
	private Long posWalletId = 444L;
	private Long cardId = 17L;


	/**
	 * 更新订单状态
	 */
	@Test
	public void updateOrderStatus() {
		String orderNo = "DWC2020071764632651";
		seniorPayService.updateOrderStatusWithMq(orderNo, false);
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
			.doWithdraw(payerWalletId, walletCard, 1L, (byte) 2,
				"http://192.168.197.75:7777/#/withdrawSuccess",
				"8.8.8.8");
		log.info("withdraw.resp = {}", JsonUtil.toJSON(withdraw));
	}


	private Tuple<WalletPayMethod, List<Reciever>> getReciverOnAgentPay(Long total, Long fee) {
		Pos pos = Pos.builder()
			.payType(CollectPayType.POS.getValue())
			.vspCusid("56058104816U8U6")
			.amount(2L)
			.build();

		return new Tuple<>(WalletPayMethod.builder().pos(pos).build(), getReciever(total, fee));
	}

	private Tuple<WalletPayMethod, List<Reciever>> getReciverSilyProxy(Long total, Long fee) {
		Pos pos = Pos.builder()
			.payType(CollectPayType.POS.getValue())
			.vspCusid("56058104816U8U6")
			.amount(2L)
			.build();

		return new Tuple<>(WalletPayMethod.builder().pos(pos).build(), getReciever(total, fee));
	}

	private Tuple<WalletPayMethod, List<Reciever>> getReciverBalance(Long total, Long fee) {
		Balance balance = Balance.builder()
			.payerWalletId(platWalletId)
			.amount(1L)
			.build();

		return new Tuple<>(WalletPayMethod.builder().balance(balance).build(),
			getReciever(total, fee));
	}


	private Tuple<WalletPayMethod, List<Reciever>> getReciverCodePay(Long total, Long fee) {
		CodePay codePay = CodePay.builder()
			.payType(CollectPayType.CODEPAY.getValue())
			.authcode("134923871200237362")
			.vspCusid("56058104816U8U6")
			.amount(total)
			.build();

		return new Tuple<>(WalletPayMethod.builder().codePay(codePay).build(),
			getReciever(total, fee));
	}

	private Tuple<WalletPayMethod, List<Reciever>> getReciverBankcard(Long total, Long fee) {
		BankCard bankCard = BankCard.builder()
			.payType(CollectPayType.BANKCARD.getValue())
			.bankCardNo("6214850201481956")
			.amount(total)
			.build();

		return new Tuple<>(WalletPayMethod.builder().bankCard(bankCard).build(),
			getReciever(total, fee));
	}

	private List<Reciever> getReciever(Long total, Long fee) {

		Reciever mch = Reciever.builder()
			.walletId(payeeWalletId)
			.amount(total - fee)
			.roleType(CollectRoleType.PROJECTOR.getValue())
			.build();
		if (fee > 0) {
			Reciever plat = Reciever.builder()
				.walletId(platWalletId)
				.amount(fee)
				.roleType(CollectRoleType.BUDGETER.getValue())
				.build();
			return Arrays.asList(plat, mch);
		} else {
			return Arrays.asList(mch);
		}
	}

	/**
	 * 代收
	 */
	@Test
	public void collect() {

		Tuple<WalletPayMethod, List<Reciever>> tuple = getReciverSilyProxy(2L, 1L);

		CollectReq req = CollectReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(2L)
			.note("")
			.fee(0L)
			.validateType((byte) 0)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.recievers(tuple.right)
			.walletPayMethod(tuple.left)
			.build();
		WalletCollectResp collect = seniorPayService.collect(req, "", "");
		log.info("预代收 {}", JsonUtil.toJSON(collect));
	}


	@Test
	public void agentPay() {
		String orderNo = "DWC2020071764632651";
		long amount = 1L;

		AgentPayReq.Reciever reciever = new AgentPayReq.Reciever();
		reciever.setWalletId(payeeWalletId);
		reciever.setAmount(amount);
		reciever.setFeeAmount(0L);
		WalletOrder order = walletOrderDao.selectByOrderNo(orderNo);
		SettleResp resp = seniorPayService.agentPay(order,
			String.valueOf(System.currentTimeMillis()), reciever, "jUnitAgentPay");
		log.info("agent pay {}", resp);
	}

	@Test
	public void refund() {
		String orderNo = "DWC2020071764632651";
		long amount = 1L;

		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setWalletId(platWalletId);
		refundInfo.setAmount(amount);
		WalletOrder collectOrder = walletOrderDao.selectByOrderNo(orderNo);
		WalletOrder refund = seniorPayService
			.refund(collectOrder, String.valueOf(System.currentTimeMillis()),
				Arrays.asList(refundInfo), "");
		log.info("refund {}", refund);
	}

	@Test
	public void deduction() {
		Balance balance = Balance.builder()
			.payerWalletId(payeeWalletId)
			.amount(1L)
			.build();
		DeductionReq req = DeductionReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(1L)
			.fee(0L)
			.walletPayMethod(WalletPayMethod.builder().balance(balance).build())
			.industryCode("1010")
			.industryName("保险代理")
			.build();
		WalletCollectResp resp = seniorPayService.deduction(req);
		log.info("deduction {}", resp);
	}


	@Test
	public void getSellerId() {
		String sellerId = seniorPayService.getSellerId("440106");
		Assert.assertNotNull(sellerId);
	}
}