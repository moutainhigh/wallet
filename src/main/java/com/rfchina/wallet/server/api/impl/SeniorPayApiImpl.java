package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.RechargeConfirmVo;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawReq;
import com.rfchina.wallet.server.service.SeniorPayService;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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

		RechargeResp resp = seniorPayService.recharge(req);
		RechargeConfirmVo vo = BeanUtil.newInstance(resp, RechargeConfirmVo.class);

		String rechargeTicket = UUID.randomUUID().toString();
		redisTemplate.opsForValue()
			.set(PRE_RECHARGE + rechargeTicket, vo, 10, TimeUnit.MINUTES);

		return resp;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void rechargeConfirm(String accessToken, String rechargeTicker,
		String verifyCode, String ip) {

		RechargeConfirmVo vo = (RechargeConfirmVo) redisTemplate.opsForValue()
			.get(PRE_RECHARGE + rechargeTicker);
		seniorPayService.rechargeConfirm(vo.getApplyId(), vo.getTradeNo(), verifyCode, ip);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletWithdraw withdraw(String accessToken, WithdrawReq req) {
		return seniorPayService.withdraw(req);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletCollect preCollect(String accessToken, CollectReq req) {
		return seniorPayService.preCollect(req);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletCollectResp doCollect(String accessToken, WalletCollect walletCollect) {
		return seniorPayService.doCollect(walletCollect);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletCollect queryCollect(String accessToken, String collectOrderNo) {
		return seniorPayService.queryCollect(collectOrderNo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void agentPay(String accessToken, String collectOrderNo, List<Reciever> receivers) {
		seniorPayService.agentPay(collectOrderNo, receivers);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletClearing agentPayQuery(String accessToken, String payOrderNo) {
		return seniorPayService.agentPayQuery(payOrderNo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletRefund refund(String accessToken, String collectOrderNo, List<RefundInfo> rList) {
		return seniorPayService.refund(collectOrderNo, rList);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletRefund refundQuery(String accessToken, String refundOrderNo) {
		return seniorPayService.refundQuery(refundOrderNo);
	}
}
