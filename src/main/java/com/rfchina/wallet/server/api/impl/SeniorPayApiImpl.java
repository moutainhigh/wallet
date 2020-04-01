package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.limiter.setting.RateSetting;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.ParamVerify;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BalanceJob;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.AgentPayReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.DeductionReq;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.model.ext.UnifiedConfirmVo;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import com.rfchina.wallet.server.msic.EnumWallet.BalanceFreezeMode;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.SeniorBalanceService;
import com.rfchina.wallet.server.service.SeniorPayService;
import com.rfchina.wallet.server.service.VerifyService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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

	@Autowired
	private ConfigService configService;

	@Autowired
	private SeniorBalanceService seniorBalanceService;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public RechargeResp recharge(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) Long cardId,
		@ParamValid(nullable = false) Long amount) {

		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		verifyService.checkCard(walletCard);

		RechargeResp resp = seniorPayService.recharge(walletId, walletCard, amount);

		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(resp, UnifiedConfirmVo.class);
		confirmVo.setOrderId(resp.getId());
		String ticket = saveConfirmVo(confirmVo);
		resp.setTicket(ticket);

		return resp;
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public WithdrawResp withdraw(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false) Long cardId,
		@ParamValid(nullable = false, min = 1) Long amount,
		@ParamValid(nullable = false) Integer validateType,
		@ParamValid(nullable = true) String jumpUrl,
		@ParamValid(nullable = false) String customerIp) {

		WalletCard walletCard = walletCardDao.selectByPrimaryKey(cardId);
		verifyService.checkCard(walletCard);
		// 检查回调域名
		if (StringUtils.isNotBlank(jumpUrl)) {
			jumpUrl = checkBlankUrl(jumpUrl);
		}

		WithdrawResp withdraw = seniorPayService.balanceWithdraw(walletId, walletCard, amount,
			validateType.byteValue(), jumpUrl, customerIp, BalanceFreezeMode.NO_FREEZE, false);

		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(withdraw, UnifiedConfirmVo.class);
		confirmVo.setOrderId(withdraw.getId());
		String ticket = saveConfirmVo(confirmVo);
		withdraw.setTicket(ticket);

		return withdraw;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public WalletCollectResp collect(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) CollectReq req,
		@ParamValid(nullable = true) String jumpUrl,
		@ParamValid(nullable = true) String customerIp,
		RateSetting rateSetting) {

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
		// 检查回调域名
		if (StringUtil.isNotBlank(jumpUrl)) {
			jumpUrl = checkBlankUrl(jumpUrl);
		}

		// 发起代收
		WalletCollectResp collect = seniorPayService.collect(req, jumpUrl, customerIp);
		// 生成票据
		UnifiedConfirmVo confirmVo = BeanUtil.newInstance(collect, UnifiedConfirmVo.class);
		confirmVo.setOrderId(collect.getId());
		String ticket = saveConfirmVo(confirmVo);
		collect.setTicket(ticket);

		return collect;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public SettleResp agentPay(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String bizNo,
		@ParamValid(nullable = false) String collectOrderNo,
		@ParamValid(nullable = false) Reciever receiver,
		@ParamValid(nullable = true) String note,
		RateSetting rateSetting) {
		// 检查代收单
		WalletOrder collectOrder = verifyService
			.checkOrder(collectOrderNo, OrderStatus.SUCC.getValue());
		return seniorPayService.agentPay(collectOrder, bizNo, receiver, note);
	}


	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public WalletOrder refund(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String bizNo,
		@ParamValid(nullable = false) String collectOrderNo,
		@ParamValid(nullable = false) List<RefundInfo> refundList,
		@ParamValid(nullable = true) String note,
		RateSetting rateSetting) {
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
		return seniorPayService.refund(collectOrder, bizNo, refundList, note);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public WalletCollectResp deduction(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) DeductionReq req,
		RateSetting rateSetting) {

		// 检验支付方式
		WalletPayMethod payMethod = req.getWalletPayMethod();
		if (payMethod.getMethods() == 0) {
			throw new WalletResponseException(EnumResponseCode.COMMON_INVALID_PARAMS,
				"walletPayMethod");
		}
		// 发起消费
		WalletCollectResp collect = seniorPayService.deduction(req);
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
	@ParamVerify
	public WalletOrder orderQuery(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String orderNo) {
		return seniorPayService.orderQuery(orderNo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public void smsConfirm(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String ticket,
		@ParamValid(nullable = false) String verifyCode,
		@ParamValid(nullable = false) String ip) {

		UnifiedConfirmVo vo = getUnifiedConfirmVo(ticket);
		WalletOrder order = verifyService
			.checkOrder(vo.getOrderId(), OrderStatus.WAITTING.getValue());
		seniorPayService.smsConfirm(order, vo.getTradeNo(), verifyCode, ip);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public void smsRetry(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String ticket) {

		UnifiedConfirmVo vo = getUnifiedConfirmVo(ticket);
		WalletOrder order = verifyService
			.checkOrder(vo.getOrderId(), OrderStatus.WAITTING.getValue());
		seniorPayService.smsRetry(order);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@ParamVerify
	public BalanceJob balanceFile(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Date balanceDate) {
		return seniorBalanceService.balanceFile(balanceDate);
	}

	private UnifiedConfirmVo getUnifiedConfirmVo(String ticket) {
		UnifiedConfirmVo result = (UnifiedConfirmVo) redisTemplate.opsForValue()
			.get(PRE_RECHARGE + ticket);
		return Optional.ofNullable(result)
			.orElseThrow(() -> new WalletResponseException(
				EnumWalletResponseCode.WALLET_TICKET_ERROR));
	}

	private String saveConfirmVo(UnifiedConfirmVo confirmVo) {
		String ticket = UUID.randomUUID().toString();
		redisTemplate.opsForValue()
			.set(PRE_RECHARGE + ticket, confirmVo, 30, TimeUnit.MINUTES);
		return ticket;
	}

	public String checkBlankUrl(String jumpUrl) {
		try {
			URL url = new URL(jumpUrl);
			String host = url.getHost();
			List<String> blankList = JsonUtil.toArray(configService.getBlanklist(), String.class);
			Optional<String> opt = blankList.stream()
				.filter(b -> b.equals(host))
				.findAny();
			if (opt.isPresent()) {
				return jumpUrl;
			}
		} catch (MalformedURLException e) {
			log.error("URL格式检查错误", e);
		}
		log.error("jumpUrl白名单校验错误 {}", jumpUrl);
		return configService.getYunstJumpUrlPrefix() + "/#/warning?msg=jumpUrlErr";
	}
}
