package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.domain.model.WalletConfig;
import com.rfchina.wallet.server.api.WalletConfigApi;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
public class WalletConfigController {

	@Autowired
	private WalletConfigApi walletConfigApi;

	@ApiOperation("钱包配置-获取全局配置")
	@PostMapping(UrlConstant.WALLET_GET_UNI_CONFIG)
	public ResponseValue<WalletConfig> getUniConfig(
		@RequestParam("access_token") String accessToken) {

		WalletConfig config = walletConfigApi.getUniConfig();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, config);
	}

	@ApiOperation("钱包配置-更新全局配置")
	@PostMapping(UrlConstant.WALLET_UPDATE_UNI_CONFIG)
	public ResponseValue editUniConfig(
		@RequestParam("access_token") String accessToken,
		@ApiParam(name = "manual_withdraw_company_min", value = "企业手动提现最低金额") @RequestParam("manual_withdraw_company_min") Long manualWithdrawCompanyMin,
		@ApiParam(name = "manual_withdraw_person_min", value = "个人手动提现最低金额") @RequestParam("manual_withdraw_person_min") Long manualWithdrawPersonMin
	) {

		walletConfigApi.editUniConfig(manualWithdrawCompanyMin,manualWithdrawPersonMin);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

}
