package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.UnifiedConfirmVo;
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

	@Autowired
	private WalletOrderExtDao walletOrderDao;


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public RechargeResp recharge(String accessToken, RechargeReq req) {
		Optional.ofNullable(req.getPayerWalletId())
			.orElseThrow(() -> new WalletResponseException(EnumResponseCode.COMMON_MISSING_PARAMS,
				"payerWalletId"));

		RechargeResp resp = seniorPayService.recharge(req);

		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(resp, UnifiedConfirmVo.class);
		saveConfirmVo(confirmVo);
		return resp;
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletOrder withdraw(String accessToken, WithdrawReq req) {
		Optional.ofNullable(req.getPayerWalletId())
			.orElseThrow(() -> new WalletResponseException(EnumResponseCode.COMMON_MISSING_PARAMS,
				"payerWalletId"));

		WalletOrder withdraw = seniorPayService.withdraw(req);

		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(withdraw, UnifiedConfirmVo.class);
		saveConfirmVo(confirmVo);
		return withdraw;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletCollectResp collect(String accessToken, CollectReq req) {
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
		WalletCollectResp collect = seniorPayService.collect(req);
		// 生成票据
		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(collect, UnifiedConfirmVo.class);
		saveConfirmVo(confirmVo);
		return collect;
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
		// 检查代收单
		WalletOrder collectOrder = walletOrderDao.selectByOrderNo(collectOrderNo);
		Optional.ofNullable(collectOrder)
			.orElseThrow(() -> new WalletResponseException(EnumResponseCode.COMMON_INVALID_PARAMS,
				"collectOrderNo"));
		seniorPayService.agentPay(collectOrder, bizNo, receivers);
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

		return seniorPayService.refund(collectOrderNo, bizNo, refundList);
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
		seniorPayService.smsConfirm(vo.getOrderId(), vo.getTradeNo(), verifyCode, ip);
	}

	private UnifiedConfirmVo getUnifiedConfirmVo(String ticket) {
		return (UnifiedConfirmVo) redisTemplate.opsForValue()
			.get(PRE_RECHARGE + ticket);
	}

	private void saveConfirmVo(UnifiedConfirmVo confirmVo) {
		String ticket = UUID.randomUUID().toString();
		redisTemplate.opsForValue()
			.set(PRE_RECHARGE + ticket, confirmVo, 10, TimeUnit.MINUTES);
	}
}
