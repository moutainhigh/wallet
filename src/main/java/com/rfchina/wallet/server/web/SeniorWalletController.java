package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.server.model.ext.CollectQueryResp;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectAsyncResp;
import com.rfchina.wallet.server.model.ext.CollectSyncResp;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.RefundResult;
import com.rfchina.wallet.server.model.ext.SettleQueryResp;
import com.rfchina.wallet.server.model.ext.SettleReq;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.SeniorWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SeniorWalletController {

	@Autowired
	private SeniorWalletService seniorWalletService;

	@ApiOperation("高级钱包-同步代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_ASYNC)
	public ResponseValue<WalletCollect> collectAsync(
		@ApiParam(value = "应用令牌") @RequestParam("access_token") String accessToken,
		@ApiParam(required = true) @RequestBody CollectReq collectReq
	) {
		WalletCollect collect = seniorWalletService.preCollect(accessToken, collectReq);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, collect);
	}

	@ApiOperation("高级钱包-异步代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_SYNC)
	public ResponseValue<WalletCollect> collectSync(
		@ApiParam(value = "应用令牌") @RequestParam("access_token") String accessToken,
		@ApiParam(required = true) @RequestBody CollectReq collectReq
	) {
		WalletCollect walletCollect = seniorWalletService
			.preCollect(accessToken, collectReq);
		WalletCollect result = seniorWalletService.doCollect(accessToken, walletCollect);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-代收结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_QUERY)
	public ResponseValue<List<WalletCollect>> collectQuery(
		@ApiParam(value = "应用令牌") @RequestParam("access_token") String accessToken,
		@ApiParam(required = true) @RequestParam("batch_no") String batchNo
	) {
		List<WalletCollect> collects = seniorWalletService.getCollects(accessToken, batchNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, collects);
	}

	@ApiOperation("高级钱包-清算")
	@PostMapping(UrlConstant.SENIOR_WALLET_SETTLE)
	public ResponseValue<SettleResp> settle(
		@ApiParam(value = "应用令牌") @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单据ID", required = true) @RequestParam(value = "collect_id") Long collectId,
		@ApiParam(value = "代付列表（与代收的分账规则对应）", required = true) @RequestBody SettleReq settleReq
	) {
		seniorWalletService.agentPay(accessToken, collectId, settleReq.getReceivers());
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-清算结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_SETTLE_QUERY)
	public ResponseValue<List<WalletClearing>> settleQuery(
		@ApiParam(value = "工单ID", required = true) @RequestParam(value = "apply_id") Long applyId
	) {
		List<WalletClearing> walletClearings = seniorWalletService.agentPayQuery(applyId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletClearings);
	}

	@ApiOperation("高级钱包-退款")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND)
	public ResponseValue<RefundResult> refund(
		@ApiParam(value = "代收单据ID", required = true) @RequestParam(value = "collect_id") Long collectId,
		@ApiParam(value = "") @RequestBody List<RefundInfo> refundList
	) {
		seniorWalletService.refund(collectId, refundList);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-充值")
	@PostMapping(UrlConstant.SENIOR_WALLET_RECHARGE)
	public ResponseValue recharge(
		@ApiParam(value = "应用令牌") @RequestParam("access_token") String accessToken,
		@ApiParam(required = true) @RequestBody RechargeReq rechargeReq
	) {
		seniorWalletService.recharge(accessToken, rechargeReq);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}
}
