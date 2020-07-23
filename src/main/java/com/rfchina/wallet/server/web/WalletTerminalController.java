package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.WalletArea;
import com.rfchina.wallet.domain.model.ext.WalletTerminalExt;
import com.rfchina.wallet.server.api.WalletTerminalApi;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class WalletTerminalController {

	@Autowired
	private WalletTerminalApi walletTerminalApi;

	@ApiOperation("终端管理-地区绑定终端")
	@PostMapping(UrlConstant.WALLET_BIND_TERMINAL)
	public ResponseValue bindTerminal(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "地区码", required = true) @RequestParam("area_code") String areaCode,
		@ApiParam(value = "终端号", required = true) @RequestParam("vsp_termid") String vspTermid,
		@ApiParam(value = "创建人id", required = true) @RequestParam("creator_id") String creatorId,
		@ApiParam(value = "创建人名称", required = true) @RequestParam("creator_name") String creatorName
	) {

		walletTerminalApi.bindTerminal(accessToken, areaCode, vspTermid, creatorId, creatorName);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("终端管理-地区绑定子商户")
	@PostMapping(UrlConstant.WALLET_BIND_VSP_CUSID)
	public ResponseValue bindVspCusid(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "地区码", required = true) @RequestParam("area_code") String areaCode,
		@ApiParam(value = "集团商户号", required = true) @RequestParam("vsp_merchantid") String vspMerchantid,
		@ApiParam(value = "子商户号", required = true) @RequestParam("vsp_cusid") String vspCusid,
		@ApiParam(value = "主收款人钱包id", required = true) @RequestParam("proxy_wallet_id") Long proxyWalletId,
		@ApiParam(value = "创建人id", required = true) @RequestParam("creator_id") String creatorId,
		@ApiParam(value = "创建人名称", required = true) @RequestParam("creator_name") String creatorName
	) {

		walletTerminalApi
			.bindVspId(accessToken, areaCode, vspMerchantid, vspCusid, proxyWalletId, creatorId,
				creatorName);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("终端管理-地区商户号列表")
	@PostMapping(UrlConstant.WALLET_QUERY_AREA)
	public ResponseValue<Pagination<WalletArea>> queryArea(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "limit", required = true) @RequestParam("limit") Integer limit,
		@ApiParam(value = "offset", required = true) @RequestParam("offset") Integer offset
	) {

		Pagination<WalletArea> page = walletTerminalApi.queryArea(accessToken, limit, offset);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}

	@ApiOperation("终端管理-终端列表")
	@PostMapping(UrlConstant.WALLET_QUERY_TERMINAL)
	public ResponseValue<Pagination<WalletTerminalExt>> queryTerminal(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "地区码", required = false) @RequestParam(value = "area_code", required = false) String areaCode,
		@ApiParam(value = "主收款钱包id", required = false) @RequestParam(value = "proxy_wallet_id", required = false) Long proxyWalletId,
		@ApiParam(value = "子商户号", required = false) @RequestParam(value = "vsp_cusid", required = false) String vspCusid,
		@ApiParam(value = "终端号", required = false) @RequestParam(value = "vsp_termid", required = false) String vspTermid,
		@ApiParam(value = "状态： 0：未绑定，1：已绑定，2：已解绑", required = false) @RequestParam(value = "status", required = false) Byte status,
		@ApiParam(value = "limit", required = true) @RequestParam("limit") Integer limit,
		@ApiParam(value = "offset", required = true) @RequestParam("offset") Integer offset
	) {

		Pagination<WalletTerminalExt> page = walletTerminalApi
			.queryTerminalExt(areaCode, proxyWalletId, vspCusid, vspTermid, status, limit, offset);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}
}
