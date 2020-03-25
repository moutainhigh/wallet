package com.rfchina.wallet.server.service;

import static com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode.WALLET_BALANCE_IN_NOT_ENOUGH;

import com.google.common.collect.Lists;
import com.rfchina.biztools.functional.MaxIdIterator;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletBalanceDetailDao;
import com.rfchina.wallet.domain.misc.EnumDef.BalanceDetailStatus;
import com.rfchina.wallet.domain.model.WalletBalanceDetail;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceFreezeMode;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletBalanceDetailService {

	@Autowired
	private WalletBalanceDetailDao walletBalanceDetailDao;

	/**
	 * 消费余额明细
	 */
	public WalletBalanceDetail consumePayDetail(WalletOrder order, Long detailId,
		WalletBalanceDetail payDetail, Long withdrawAmount, BalanceFreezeMode mode) {

		Long revertAmount = 0 - withdrawAmount.longValue();
		if(BalanceFreezeMode.FREEZEN.getValue().byteValue() == mode.getValue()) {
			walletBalanceDetailDao.updateDetailFreezen(payDetail.getOrderId(),
				payDetail.getOrderDetailId(), withdrawAmount, revertAmount);
		}

		WalletBalanceDetail withdrawDetail = WalletBalanceDetail.builder()
			.walletId(order.getWalletId())
			.orderId(order.getId())
			.orderNo(order.getOrderNo())
			.orderDetailId(detailId)
			.refOrderId(payDetail.getOrderId())
			.refOrderNo(payDetail.getOrderNo())
			.refOrderDetailId(payDetail.getOrderDetailId())
			.type(order.getType())
			.status(BalanceDetailStatus.WAITTING.getValue())
			.amount(revertAmount)
			.balance(revertAmount)
			.freezen(0L)
			.createTime(new Date())
			.build();
		walletBalanceDetailDao.insertSelective(withdrawDetail);
		return withdrawDetail;
	}

	/**
	 * 选择提现余额
	 */
	public List<Tuple<WalletBalanceDetail, Long>> selectDetailToPay(Long walletId, Long amount) {

		List<Tuple<WalletBalanceDetail, Long>> payDetails = Lists.newArrayList();
		AtomicReference<Long> remainAmount = new AtomicReference<>(0L);
		remainAmount.set(amount);
		new MaxIdIterator<WalletBalanceDetail>().apply(
			maxId -> walletBalanceDetailDao.selectUnWithdraw(walletId, maxId),
			payDetail -> {
				if (remainAmount.get() <= 0) {
					return Long.MAX_VALUE;
				}
				if (payDetail.getBalance() > 0) {
					Long withdrawAmount =
						payDetail.getBalance() > remainAmount.get() ? remainAmount.get()
							: payDetail.getBalance();
					remainAmount.updateAndGet(x -> x - withdrawAmount);
					payDetails.add(new Tuple<>(payDetail, withdrawAmount));
				}
				return payDetail.getId();
			});
		if (remainAmount.get() < amount) {
			log.error("钱包[{}]入金余额[{}]不足出金金额[{}]", walletId, remainAmount.get(), amount);
			throw new WalletResponseException(WALLET_BALANCE_IN_NOT_ENOUGH);
		}
		return payDetails;
	}
}
