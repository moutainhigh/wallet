package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

		WalletInfoResp resp = walletService.queryWalletInfo(accessToken,walletId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,resp);
	}

}
