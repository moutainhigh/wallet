package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletUser;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletCardVo;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
public class WalletController {

	@Autowired
	private WalletApi walletApi;

	@ApiOperation("查询支付状态")
	@PostMapping(UrlConstant.WALLET_APPLY_QUERY)
	public ResponseValue<List<PayStatusResp>> queryWalletApply(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务凭证号(业务方定义唯一)", required = false, example = "123")
		@RequestParam(value = "biz_no", required = false) String bizNo,
		@ApiParam(value = "钱包批次号", required = false) @RequestParam(value = "batch_no", required = false)
			String batchNo) {
		List<PayStatusResp> resp = walletApi.queryWalletApply(accessToken, bizNo, batchNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

//	@ApiOperation("重做问题单")
//	@PostMapping(UrlConstant.WALLET_APPLY_REDO)
//	public ResponseValue redoWalletApply(@RequestParam("access_token") String accessToken,
//		@ApiParam(value = "流水id", required = true, example = "1") @RequestParam(value = "wallet_log_id")
//			Long walletLogId) {
//		walletApi.redoWalletApply(accessToken, walletLogId);
//		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
//	}

	@ApiOperation("查询钱包信息（企业or个人）")
	@PostMapping(UrlConstant.WALLET_QUERY_INFO)
	public ResponseValue<WalletInfoResp> queryWalletInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId) {

		WalletInfoResp resp = walletApi.queryWalletInfo(accessToken, walletId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("通过UID查询钱包信息（企业or个人）")
	@PostMapping(UrlConstant.WALLET_QUERY_INFO_BY_UID)
	public ResponseValue<WalletInfoResp> queryWalletInfoByUid(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "用户ID", required = true, example = "2") @RequestParam("user_id") Long userId) {

		WalletInfoResp resp = walletApi.queryWalletInfoByUserId(accessToken, userId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}


	@ApiOperation("钱包列表")
	@PostMapping(UrlConstant.WALLET_LIST)
	public ResponseValue<Pagination<Wallet>> walletList(
		@ApiParam(name = "access_token", value = "访问令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(name = "title", value = "钱包名字") @RequestParam(value = "title", required = false) String title,
		@ApiParam(name = "type", value = "钱包类型， 1：企业钱包，2：个人钱包") @RequestParam(value = "type", required = false) Byte type,
		@ApiParam(name = "wallet_level", value = "钱包等级 1： 初级钱包，2： 高级钱包") @RequestParam(value = "wallet_level", required = false) Byte walletLevel,
		@ApiParam(name = "status", value = "钱包状态: 1:待审核，2：激活,3：禁用") @RequestParam(value = "status", required = false) Byte status,
		@ApiParam(name = "limit", value = "必填，需要查询的数量（数量最大50）", required = true) @RequestParam("limit") Integer limit,
		@ApiParam(name = "offset", value = "必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。", required = true) @RequestParam("offset") Integer offset,
		@ApiParam(name = "stat", value = "非必填, false:否, true:是, 是否返回数据总量, 默认false", required = true) @RequestParam("stat") Boolean stat) {

		Pagination<Wallet> page = walletApi
			.walletList(accessToken, title, type, walletLevel, status, limit, offset, stat);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}

	@ApiOperation("开通未审核的钱包")
	@PostMapping(UrlConstant.CREATE_WALLET)
	public ResponseValue<Wallet> createWallet(@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包类型， 1：企业钱包，2：个人钱包", required = true, example = "2") @RequestParam("type") Byte type,
		@ApiParam(value = "钱包标题，通常是姓名或公司名", required = true, example = "测试个人钱包") @RequestParam("title")
			String title,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source) {
		Wallet wallet = walletApi.createWallet(accessToken, type, title, source);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, wallet);
	}

	@ApiOperation("富慧通审核企业商家钱包")
	@PostMapping(UrlConstant.ACTIVE_WALLET_COMPANY)
	public ResponseValue activeWalletCompany(@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "公司名称", required = true) @RequestParam("company_name") String companyName,
		@ApiParam(value = "钱包状态: 1:待审核，2：激活,3：禁用", required = true) @RequestParam("status") Byte status,
		@ApiParam(value = "审核方式，1：运营，2：银企直连，4：通联", required = true, example = "3") @RequestParam("audit_type")
			Long auditType,
		@ApiParam(value = "公司电话", required = false) @RequestParam(value = "phone", required = false) String phone,
		@ApiParam(value = "公司邮箱", required = false) @RequestParam(value = "email", required = false) String email) {
		walletApi.activeWalletCompany(walletId, companyName, status, auditType, phone, email);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("富慧通审核个人商家钱包")
	@PostMapping(UrlConstant.ACTIVE_WALLET_PERSON)
	public ResponseValue activeWalletPerson(@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "姓名", required = true) @RequestParam("name") String name,
		@ApiParam(value = "证件类型，1:身份证", required = true, example = "1") @RequestParam("id_type") Byte idType,
		@ApiParam(value = "证件号", required = true) @RequestParam("id_no") String idNo,
		@ApiParam(value = "钱包状态: 1:待审核，2：激活,3：禁用", required = true) @RequestParam("status") Byte status,
		@ApiParam(value = "审核方式，1：运营，2：银企直连，4：通联", required = true, example = "3") @RequestParam("audit_type")
			Long auditType) {
		walletApi.activeWalletPerson(walletId, name, idType, idNo, status, auditType);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
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
	public ResponseValue<WalletCard> bindBankCard(@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "银行帐号", required = true) @RequestParam("bank_account") String bankAccount,
		@ApiParam(value = "开户支行", required = true) @RequestParam("bank_code") String bankCode,
		@ApiParam(value = "开户名", required = true) @RequestParam("deposit_name") String depositName,
		@ApiParam(value = "是否默认银行卡: 1:是，2：否") @RequestParam(value = "is_def", required = false, defaultValue = "1")
			Integer isDef,
		@ApiParam(value = "预留手机号") @RequestParam(value = "telephone", required = false) String telephone) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.bindBankCard(accessToken, walletId, bankCode, bankAccount, depositName, isDef,
				telephone));
	}

	@ApiOperation("银行类别列表")
	@PostMapping(UrlConstant.WALLET_BANK_CLASS_LIST)
	public ResponseValue<List<BankClass>> bankClassList() {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi.bankClassList());
	}

	@ApiOperation("银行地区列表")
	@PostMapping(UrlConstant.WALLET_BANK_AREA_LIST)
	public ResponseValue<List<BankArea>> bankAreaList(
		@ApiParam(value = "银行类型编码", required = true) @RequestParam("class_code") String classCode) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.bankAreaList(classCode));
	}

	@ApiOperation("银行支行列表")
	@PostMapping(UrlConstant.WALLET_BANK_LIST)
	public ResponseValue<List<Bank>> bankList(
		@ApiParam(value = "银行类型编码", required = true) @RequestParam("class_code") String classCode,
		@ApiParam(value = "地区编码", required = true) @RequestParam("area_code") String areaCode) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.bankList(classCode, areaCode));
	}

	@ApiOperation("银行支行信息")
	@PostMapping(UrlConstant.WALLET_BANK)
	public ResponseValue<BankCode> bank(
		@ApiParam(value = "银行编码", required = true) @RequestParam("bank_code") String bankCode) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi.bank(bankCode));
	}

	@ApiOperation("发送短信验证码")
	@PostMapping(UrlConstant.WALLET_SEND_VERIFY_CODE)
	public ResponseValue<Map<String, Object>> sendVerifyCode(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "手机号码", required = true) @RequestParam(value = "mobile") String mobile,
		@ApiParam(value = "验证码类型, 1:登录, 2:验证已开通钱包帐号", required = true) @RequestParam("type") Integer type,
		@ApiParam(value = "反作弊结果查询token", required = true) @RequestParam("verify_token") String verifyToken,
		@ApiParam(value = "触发图形验证码并验证成功后重定向地址") @RequestParam(value = "redirect_url", required = false)
			String redirectUrl,
		@ApiParam(value = "来源IP", required = true) @RequestParam(value = "ip") String ip,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = false, example = "1")
		@RequestParam(value = "channel_type", required = false) Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = false, example = "2")
		@RequestParam(value = "source", required = false) Byte source,
		@ApiParam(value = "业务用户id", required = false) @RequestParam(value = "biz_user_id", required = false)
			Long bizUserId) {
		return walletApi
			.sendVerifyCode(accessToken, bizUserId, mobile, type, verifyToken, redirectUrl, ip);
	}

	@ApiOperation("通过短信验证码登录")
	@PostMapping(UrlConstant.WALLET_LOGIN_WITH_VERIFY_CODE)
	public ResponseValue<WalletUser> loginWithVerifyCode(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "验证码", required = true) @RequestParam("verify_code") String verifyCode,
		@ApiParam(value = "短信类型, 1:登录当前钱包, 2:登录已开通钱包", required = true) @RequestParam("type") Integer type,
		@ApiParam(value = "来源IP", required = true) @RequestParam(value = "ip") String ip) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.loginWithVerifyCode(accessToken, mobile, verifyCode, type, ip));
	}


	@ApiOperation("业务查询银行卡信息")
	@PostMapping(UrlConstant.WALLET_CARD_QUERY)
	public ResponseValue<List<WalletCardVo>> queryWalletCard(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true) @RequestParam("wallet_id") Long walletId) {

		List<WalletCardVo> resp = walletApi.queryWalletCard(accessToken, walletId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}
}
