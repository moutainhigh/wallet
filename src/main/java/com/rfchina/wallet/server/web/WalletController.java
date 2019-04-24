package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@Api
public class WalletController {

	@Autowired
	private WalletService walletService;

	@ApiOperation("查询钱包信息（企业or个人）")
	@PostMapping(UrlConstant.WALLET_QUERY_INFO)
	public ResponseValue<WalletInfoResp> queryWalletInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId
	) {

		WalletInfoResp resp = walletService.queryWalletInfo(accessToken, walletId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("开通未审核的钱包")
	@PostMapping(UrlConstant.CREATE_WALLET)
	public ResponseValue<Wallet> createWallet(
		@ApiParam(value = "钱包类型， 1：企业钱包，2：个人钱包", required = true, example = "2") @RequestParam("type") Byte type,
		@ApiParam(value = "钱包标题，通常是姓名或公司名", required = true, example = "测试个人钱包") @RequestParam("title") String title,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2") @RequestParam("source") Byte source
	) {
		Wallet wallet = walletService.createWallet(type,title,source);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, wallet);
	}

	@ApiOperation("钱包流水")
	@PostMapping(UrlConstant.WALLET_LOG_LIST)
	public ResponseValue<Pagination<WalletLog>> walletLogList(@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
														@ApiParam(value = "开始时间") @RequestParam(value = "start_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
														@ApiParam(value = "结束时间") @RequestParam(value = "end_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
														@ApiParam(value = "需要查询的数量（数量最大50）", required = true) @RequestParam(value = "limit") int limit,
														@ApiParam(value = "查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取", required = true) @RequestParam(value = "offset") long offset,
														@ApiParam(value = "非必填, false:否, true:是, 是否返回数据总量, 默认false") @RequestParam(value = "stat", required = false) Boolean stat){
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletService.walletLogList(walletId, startTime, endTime, limit, offset, stat));
	}

	@ApiOperation("钱包绑定的银行卡列表")
	@PostMapping(UrlConstant.WALLET_BANK_CARD_LIST)
	public ResponseValue<List<WalletCard>> bindingBankCardList(@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId){
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletService.bankCardList(walletId));
	}
}
