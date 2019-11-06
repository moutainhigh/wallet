package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.UnifiedConfirmVo;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.service.SeniorPayService;
import com.rfchina.wallet.server.service.VerifyService;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SeniorPayApiImpl implements SeniorPayApi {

	public static final String PRE_RECHARGE = "wallet:recharge";
	@Autowired
	private SeniorPayService seniorPayService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private VerifyService verifyService;


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public RechargeResp recharge(String accessToken, Long walletId, Long cardId, Long amount,
		String jumpUrl, String customerIp) {

		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		if (walletCard == null) {
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_NOT_EXISTS);
		}
		if (walletId.longValue() != walletCard.getWalletId().longValue()) {
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_NOT_AUTH);
		}

		RechargeResp resp = seniorPayService
			.recharge(walletId, walletCard, amount, jumpUrl, customerIp);

		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(resp, UnifiedConfirmVo.class);
		String ticket = saveConfirmVo(confirmVo);
		resp.setTicket(ticket);

		return resp;
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WithdrawResp withdraw(String accessToken, Long walletId, Long cardId, Long amount,
		String jumpUrl, String customerIp) {
		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		if (walletCard == null) {
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_NOT_EXISTS);
		}
		if (walletId.longValue() != walletCard.getWalletId().longValue()) {
			throw new WalletResponseException(EnumWalletResponseCode.BANK_CARD_NOT_AUTH);
		}

		WithdrawResp withdraw = seniorPayService
			.withdraw(walletId, walletCard, amount, jumpUrl, customerIp);

		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(withdraw, UnifiedConfirmVo.class);
		String ticket = saveConfirmVo(confirmVo);
		withdraw.setTicket(ticket);

		return withdraw;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletCollectResp collect(String accessToken, CollectReq req, String jumpUrl,
		String customerIp) {
		// 判断收款人记录不重复
		Set<String> walletSet = req.getRecievers().stream()
			.map(receiver -> receiver.getWalletId().toString())
			.collect(Collectors.toSet());
		if (walletSet.size() != req.getRecievers().size()) {
			throw new WalletResponseException(EnumWalletResponseCode.COLLECT_RECEIVER_DUPLICATE);
		}
		// 判断金额相同
		Long recAmount = req.getRecievers().stream()
			.collect(Collectors.summingLong(CollectReq.Reciever::getAmount));
		if (req.getAmount() != req.getFee() + recAmount) {
			throw new WalletResponseException(EnumWalletResponseCode.COLLECT_AMOUNT_NOT_MATCH);
		}
		// 检验支付方式
		WalletPayMethod payMethod = req.getWalletPayMethod();
		if (payMethod.getMethods() == 0) {
			throw new WalletResponseException(EnumResponseCode.COMMON_INVALID_PARAMS,
				"walletPayMethod");
		}
		// 发起代收
		WalletCollectResp collect = seniorPayService.collect(req, jumpUrl, customerIp);
		// 生成票据
		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(collect, UnifiedConfirmVo.class);
		String ticket = saveConfirmVo(confirmVo);
		collect.setTicket(ticket);

		return collect;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public SettleResp agentPay(String accessToken, String bizNo, String collectOrderNo,
		Reciever receiver) {
		// 检查代收单
		WalletOrder collectOrder = verifyService
			.checkOrder(collectOrderNo, OrderStatus.SUCC.getValue());
		return seniorPayService.agentPay(collectOrder, bizNo, receiver);
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletOrder refund(String accessToken, String bizNo, String collectOrderNo,
		List<RefundInfo> refundList) {
		// 判断退款申请不重复
		Set<String> walletIdSet = refundList.stream()
			.map(r -> r.getWalletId().toString())
			.collect(Collectors.toSet());
		if (walletIdSet.size() != refundList.size()) {
			throw new WalletResponseException(EnumWalletResponseCode.COLLECT_RECEIVER_DUPLICATE);
		}

		// 检查代收单
		WalletOrder collectOrder = verifyService
			.checkOrder(collectOrderNo, OrderStatus.SUCC.getValue());
		return seniorPayService.refund(collectOrder, bizNo, refundList);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletOrder orderQuery(String accessToken, String orderNo) {
		return seniorPayService.orderQuery(orderNo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void smsConfirm(String accessToken, String ticket,
		String verifyCode, String ip) {

		UnifiedConfirmVo vo = getUnifiedConfirmVo(ticket);
		WalletOrder order = verifyService
			.checkOrder(vo.getOrderId(), OrderStatus.WAITTING.getValue());
		seniorPayService.smsConfirm(order, vo.getTradeNo(), verifyCode, ip);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void smsRetry(String accessToken, String ticket) {
		UnifiedConfirmVo vo = getUnifiedConfirmVo(ticket);
		WalletOrder order = verifyService
			.checkOrder(vo.getOrderId(), OrderStatus.WAITTING.getValue());
		seniorPayService.smsRetry(order);
	}

	private UnifiedConfirmVo getUnifiedConfirmVo(String ticket) {
		return (UnifiedConfirmVo) redisTemplate.opsForValue()
			.get(PRE_RECHARGE + ticket);
	}

	private String saveConfirmVo(UnifiedConfirmVo confirmVo) {
		String ticket = UUID.randomUUID().toString();
		redisTemplate.opsForValue()
			.set(PRE_RECHARGE + ticket, confirmVo, 30, TimeUnit.MINUTES);
		return ticket;
	}
}
