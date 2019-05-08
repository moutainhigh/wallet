package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.msic.UrlConstant;
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
	private WalletApi walletApi;

	@ApiOperation("初级钱包-查询支付状态")
	@PostMapping(UrlConstant.JUNIOR_WALLET_QUERY)
	public ResponseValue<List<PayStatusResp>> queryPayStatus(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务凭证号(业务方定义唯一)", required = false, example = "123") @RequestParam(value = "biz_no", required = false) String bizNo,
		@ApiParam(value = "钱包批次号", required = false) @RequestParam(value = "batch_no", required = false) String batchNo
	) {
		List<PayStatusResp> resp = walletApi.queryWalletLog(accessToken, bizNo, batchNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("查询钱包信息（企业or个人）")
	@PostMapping(UrlConstant.WALLET_QUERY_INFO)
	public ResponseValue<WalletInfoResp> queryWalletInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId
	) {

		WalletInfoResp resp = walletApi.queryWalletInfo(accessToken, walletId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("通过UID查询钱包信息（企业or个人）")
	@PostMapping(UrlConstant.WALLET_QUERY_INFO_BY_UID)
	public ResponseValue<WalletInfoResp> queryWalletInfoByUid(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "用户ID", required = true, example = "2") @RequestParam("user_id") Long userId
	) {

		WalletInfoResp resp = walletApi.queryWalletInfoByUserId(accessToken, userId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("开通未审核的钱包")
	@PostMapping(UrlConstant.CREATE_WALLET)
	public ResponseValue<Wallet> createWallet(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包类型， 1：企业钱包，2：个人钱包", required = true, example = "2") @RequestParam("type") Byte type,
		@ApiParam(value = "钱包标题，通常是姓名或公司名", required = true, example = "测试个人钱包") @RequestParam("title") String title,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2") @RequestParam("source") Byte source
	) {
		Wallet wallet = walletApi.createWallet(accessToken, type, title, source);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, wallet);
	}

	@ApiOperation("富慧通审核企业商家钱包")
	@PostMapping(UrlConstant.AUDIT_WALLET_COMPANY)
	public ResponseValue auditWalletCompany(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "公司名称", required = true) @RequestParam("company_name") String companyName,
		@ApiParam(value = "钱包状态: 1:待审核，2：激活,3：禁用", required = true) @RequestParam("status") Byte status,
		@ApiParam(value = "审核方式，1：运营，3：银企直连，5：通联", required = true, example = "6") @RequestParam("audit_type") Long auditType
	) {
		walletApi.auditWalletCompany(walletId, companyName, status, auditType);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("富慧通审核个人商家钱包")
	@PostMapping(UrlConstant.AUDIT_WALLET_PERSON)
	public ResponseValue auditWalletPerson(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "姓名", required = true) @RequestParam("name") String name,
		@ApiParam(value = "证件类型，1:身份证", required = true, example = "1") @RequestParam("id_type") Byte idType,
		@ApiParam(value = "证件号", required = true) @RequestParam("id_no") String idNo,
		@ApiParam(value = "钱包状态: 1:待审核，2：激活,3：禁用", required = true) @RequestParam("status") Byte status,
		@ApiParam(value = "审核方式，1：运营，3：银企直连，5：通联", required = true, example = "6") @RequestParam("audit_type") Long auditType
	) {
		walletApi.auditWalletPerson(walletId, name, idType, idNo, status, auditType);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("钱包流水")
	@PostMapping(UrlConstant.WALLET_LOG_LIST)
	public ResponseValue<Pagination<WalletLog>> walletLogList(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "开始时间") @RequestParam(value = "start_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
		@ApiParam(value = "结束时间") @RequestParam(value = "end_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
		@ApiParam(value = "需要查询的数量（数量最大50）", required = true) @RequestParam(value = "limit") int limit,
		@ApiParam(value = "查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取", required = true) @RequestParam(value = "offset") long offset,
		@ApiParam(value = "非必填, false:否, true:是, 是否返回数据总量, 默认false") @RequestParam(value = "stat", required = false) Boolean stat) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi
			.walletLogList(accessToken, walletId, startTime, endTime, limit, offset, stat));
	}

	@ApiOperation("钱包绑定的银行卡列表")
	@PostMapping(UrlConstant.WALLET_BANK_CARD_LIST)
	public ResponseValue<List<WalletCard>> bindingBankCardList(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.bankCardList(accessToken, walletId));
	}

	@ApiOperation("绑定银行卡(对公)")
	@PostMapping(UrlConstant.WALLET_BANK_CARD_BIND)
	public ResponseValue<WalletCard> bindBankCard(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "银行帐号", required = true) @RequestParam("bank_account") String bankAccount,
		@ApiParam(value = "开户支行", required = true) @RequestParam("bank_code") String bankCode,
		@ApiParam(value = "开户名", required = true) @RequestParam("deposit_name") String depositName,
		@ApiParam(value = "是否默认银行卡: 1:是，2：否") @RequestParam(value = "is_def", required = false, defaultValue = "1") Integer isDef,
		@ApiParam(value = "预留手机号") @RequestParam(value = "telephone", required = false) String telephone) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi
			.bindBankCard(accessToken, walletId, bankCode, bankAccount, depositName, isDef,
				telephone));
	}

	@ApiOperation("银行类别列表")
	@PostMapping(UrlConstant.WALLET_BANK_CLASS_LIST)
	public ResponseValue<List<BankClass>> bankClassList() {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi
			.bankClassList());
	}

	@ApiOperation("银行地区列表")
	@PostMapping(UrlConstant.WALLET_BANK_AREA_LIST)
	public ResponseValue<List<BankArea>> bankAreaList(
		@ApiParam(value = "银行类型编码", required = true) @RequestParam("class_code") String classCode) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi
			.bankAreaList(classCode));
	}

	@ApiOperation("银行支行列表")
	@PostMapping(UrlConstant.WALLET_BANK_LIST)
	public ResponseValue<List<Bank>> bankList(
		@ApiParam(value = "银行类型编码", required = true) @RequestParam("class_code") String classCode,
		@ApiParam(value = "地区编码", required = true) @RequestParam("area＿code") String areaCode) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi
			.bankList(classCode, areaCode));
	}

}
