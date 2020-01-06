package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletCardStatus;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletStatus;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VerifyService {

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	/**
	 * 检查钱包合法性
	 */
	public Wallet checkSeniorWallet(Long walletId) {

		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		Optional<Wallet> opt = Optional.ofNullable(wallet);

		opt.orElseThrow(
			() -> new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST,
				String.valueOf(walletId)));

		opt.filter(w -> w.getLevel() == EnumDef.EnumWalletLevel.SENIOR.getValue().byteValue())
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.WALLET_LEVEL_ERROR));

		Optional.ofNullable(wallet)
			.filter(w -> w.getStatus() == WalletStatus.ACTIVE.getValue())
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.WALLET_STATUS_ERROR));

		return wallet;
	}

	/**
	 * 检查订单状态
	 */
	public WalletOrder checkOrder(Long orderId, Byte expectStatus) {

		WalletOrder order = walletOrderDao.selectByPrimaryKey(orderId);
		Optional.ofNullable(order)
			.filter(o -> o.getStatus().byteValue() == expectStatus.byteValue())
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.ORDER_STATUS_ERROR));
		return order;
	}

	/**
	 * 检查订单状态
	 */
	public WalletOrder checkOrder(String orderNo, Byte expectStatus) {

		WalletOrder order = walletOrderDao.selectByOrderNo(orderNo);
		Optional.ofNullable(order)
			.filter(o -> o.getStatus().byteValue() == expectStatus.byteValue())
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.ORDER_STATUS_ERROR));
		return order;
	}

	/**
	 * 检查银行卡状态
	 */
	public void checkCard(WalletCard walletCard, Wallet payerWallet) {

		Optional.ofNullable(walletCard)
			.filter(card -> card.getWalletId().longValue() == payerWallet.getId().longValue()
				&& card.getStatus().byteValue() == EnumWalletCardStatus.BIND.getValue().byteValue())
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.BANK_CARD_STATUS_ERROR));
	}

	/**
	 * 检查银行卡
	 */
	public void checkCard(WalletCard walletCard) {
		if (walletCard == null) {
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_NOT_EXISTS);
		}
		if (walletCard.getStatus().intValue() != EnumWalletCardStatus.BIND.getValue().intValue()) {
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_STATUS_ERROR);
		}
	}


	/**
	 * 检查渠道注册
	 */
	public WalletTunnel checkChannel(Long walletId, TunnelType channelType) {

		WalletTunnel channel = walletTunnelDao.selectByWalletId(walletId, channelType.getValue());
		Optional.ofNullable(channel).orElseThrow(
			() -> new WalletResponseException(EnumWalletResponseCode.CHANNEL_STATUS_ERROR));
		return channel;
	}
}
