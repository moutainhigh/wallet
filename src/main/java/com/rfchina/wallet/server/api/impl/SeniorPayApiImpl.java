package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.RechargeConfirmVo;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import com.rfchina.wallet.server.service.SeniorPayService;
import java.util.List;
import java.util.Optional;
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


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public RechargeResp recharge(String accessToken, RechargeReq req) {
		Optional.ofNullable(req.getPayerWalletId())
			.orElseThrow(() -> new WalletResponseException(EnumResponseCode.COMMON_MISSING_PARAMS,
				"payerWalletId"));

		RechargeResp resp = seniorPayService.recharge(req);
		RechargeConfirmVo vo = BeanUtil.newInstance(resp, RechargeConfirmVo.class);

		String ticket = UUID.randomUUID().toString();
		redisTemplate.opsForValue()
			.set(PRE_RECHARGE + ticket, vo, 10, TimeUnit.MINUTES);

		return resp;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void smsConfirm(String accessToken, String ticket,
		String verifyCode, String ip) {

		RechargeConfirmVo vo = (RechargeConfirmVo) redisTemplate.opsForValue()
			.get(PRE_RECHARGE + ticket);
		seniorPayService.smsConfirm(vo.getOrderId(), vo.getTradeNo(), verifyCode, ip);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletOrder withdraw(String accessToken, WithdrawReq req) {
		Optional.ofNullable(req.getPayerWalletId())
			.orElseThrow(() -> new WalletResponseException(EnumResponseCode.COMMON_MISSING_PARAMS,
				"payerWalletId"));

		return seniorPayService.withdraw(req);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletCollectResp collect(String accessToken, CollectReq req) {
		WalletPayMethod payMethod = req.getWalletPayMethod();
		if (payMethod.getMethods() == 0) {
			throw new WalletResponseException(EnumResponseCode.COMMON_INVALID_PARAMS,
				"walletPayMethod");
		}
		return seniorPayService.collect(req);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void agentPay(String accessToken, String bizNo, String collectOrderNo,
		List<Reciever> receivers) {
		// 判断收款人记录不重复
		Set<String> walletSet = receivers.stream()
			.map(receiver -> receiver.getWalletId().toString())
			.collect(Collectors.toSet());
		if (walletSet.size() != receivers.size()) {
			throw new WalletResponseException(EnumWalletResponseCode.COLLECT_RECEIVER_DUPLICATE);
		}
		seniorPayService.agentPay(collectOrderNo, bizNo, receivers);
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletOrder refund(String accessToken,String bizNo, String collectOrderNo,
		List<RefundInfo> refundList) {
		// 判断退款申请不重复
		Set<String> walletIdSet = refundList.stream()
			.map(r -> r.getWalletId().toString())
			.collect(Collectors.toSet());
		if (walletIdSet.size() != refundList.size()) {
			throw new WalletResponseException(EnumWalletResponseCode.COLLECT_RECEIVER_DUPLICATE);
		}

		return seniorPayService.refund(collectOrderNo,bizNo, refundList);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletOrder orderQuery(String accessToken, String orderNo) {
		return seniorPayService.orderQuery(orderNo);
	}
}
