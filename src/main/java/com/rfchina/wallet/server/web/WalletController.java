package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.*;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
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
import java.util.Map;
import java.util.TimeZone;

@RestController
@Api
public class WalletController {

	@Autowired
	private WalletApi walletApi;

	@ApiOperation("查询支付状态")
	@PostMapping(UrlConstant.WALLET_APPLY_QUERY)
	public ResponseValue<List<PayStatusResp>> queryWalletApply(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "业务凭证号(业务方定义唯一)", required = false, example = "123")
			@RequestParam(value = "biz_no", required = false) String bizNo,
			@ApiParam(value = "钱包批次号", required = false) @RequestParam(value = "batch_no", required = false)
					String batchNo) {
		List<PayStatusResp> resp = walletApi.queryWalletApply(accessToken, bizNo, batchNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("重做问题单")
	@PostMapping(UrlConstant.WALLET_APPLY_REDO)
	public ResponseValue redoWalletApply(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "流水id", required = true, example = "1") @RequestParam(value = "wallet_log_id")
					Long walletLogId) {
		walletApi.redoWalletApply(accessToken, walletLogId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("查询钱包信息（企业or个人）")
	@PostMapping(UrlConstant.WALLET_QUERY_INFO)
	public ResponseValue<WalletInfoResp> queryWalletInfo(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "钱包ID", required = true, example = "2") @RequestParam("wallet_id") Long walletId) {

		WalletInfoResp resp = walletApi.queryWalletInfo(accessToken, walletId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("通过UID查询钱包信息（企业or个人）")
	@PostMapping(UrlConstant.WALLET_QUERY_INFO_BY_UID)
	public ResponseValue<WalletInfoResp> queryWalletInfoByUid(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "用户ID", required = true, example = "2") @RequestParam("user_id") Long userId) {

		WalletInfoResp resp = walletApi.queryWalletInfoByUserId(accessToken, userId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
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
			@ApiParam(value = "审核方式，1：运营，3：银企直连，5：通联", required = true, example = "6") @RequestParam("audit_type")
					Long auditType) {
		walletApi.activeWalletCompany(walletId, companyName, auditType);
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
			@ApiParam(value = "审核方式，1：运营，3：银企直连，5：通联", required = true, example = "6") @RequestParam("audit_type")
					Long auditType) {
		walletApi.activeWalletPerson(walletId, name, idType, idNo, auditType);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("钱包流水")
	@PostMapping(UrlConstant.WALLET_LOG_LIST)
	public ResponseValue<Pagination<WalletApply>> walletLogList(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "开始时间") @RequestParam(value = "start_time", required = false)
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "end_time", required = false)
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
			@ApiParam(value = "需要查询的数量（数量最大50）", required = true) @RequestParam(value = "limit") int limit,
			@ApiParam(value = "查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取", required = true)
			@RequestParam(value = "offset") long offset,
			@ApiParam(value = "非必填, false:否, true:是, 是否返回数据总量, 默认false") @RequestParam(value = "stat", required =
					false)
					Boolean stat) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.walletApplyList(accessToken, walletId, startTime, endTime, limit, offset, stat));
	}

	@ApiOperation("钱包绑定的银行卡列表")
	@PostMapping(UrlConstant.WALLET_BANK_CARD_LIST)
	public ResponseValue<List<WalletCard>> bindingBankCardList(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi.bankCardList(accessToken, walletId));
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
				walletApi.bindBankCard(accessToken, walletId, bankCode, bankAccount, depositName, isDef, telephone));
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
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi.bankAreaList(classCode));
	}

	@ApiOperation("银行支行列表")
	@PostMapping(UrlConstant.WALLET_BANK_LIST)
	public ResponseValue<List<Bank>> bankList(
			@ApiParam(value = "银行类型编码", required = true) @RequestParam("class_code") String classCode,
			@ApiParam(value = "地区编码", required = true) @RequestParam("area_code") String areaCode) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi.bankList(classCode, areaCode));
	}

	@ApiOperation("银行支行信息")
	@PostMapping(UrlConstant.WALLET_BANK)
	public ResponseValue<BankCode> bank(
			@ApiParam(value = "银行编码", required = true) @RequestParam("bank_code") String bankCode) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletApi.bank(bankCode));
	}

	@ApiOperation("发送短信验证码")
	@PostMapping(UrlConstant.WALLET_SEND_VERIFY_CODE)
	public ResponseValue<Map<String, Object>> sendVerifyCode(@RequestParam("access_token") String accessToken,
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
		return walletApi.sendVerifyCode(accessToken, bizUserId, mobile, type, verifyToken, redirectUrl, ip);
	}

	@ApiOperation("通过短信验证码登录")
	@PostMapping(UrlConstant.WALLET_LOGIN_WITH_VERIFY_CODE)
	public ResponseValue<WalletUser> loginWithVerifyCode(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
			@ApiParam(value = "验证码", required = true) @RequestParam("verify_code") String verifyCode,
			@ApiParam(value = "短信类型, 1:登录当前钱包, 2:登录已开通钱包", required = true) @RequestParam("type") Integer type,
			@ApiParam(value = "来源IP", required = true) @RequestParam(value = "ip") String ip) {
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.loginWithVerifyCode(accessToken, mobile, verifyCode, type, ip));
	}

	@ApiOperation("升级高级钱包")
	@PostMapping(UrlConstant.WALLET_UPGRADE)
	public ResponseValue<WalletChannel> upgradeSeniorWallet(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
					Integer channelType,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) throws Exception {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.upgradeSeniorWallet(accessToken, source, channelType, walletId));
	}

	@ApiOperation("高级钱包认证验证码")
	@PostMapping(UrlConstant.WALLET_SENIOR_SMS_VERIFY_CODE)
	public ResponseValue<WalletChannel> seniorWalletSmsCodeVerification(
			@RequestParam("access_token") String accessToken,
			@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
					Integer channelType,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
			@ApiParam(value = "短信类型", required = true) @RequestParam("sms_type") Integer smsCodeType) throws Exception {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.seniorWalletSmsCodeVerification(accessToken, source, channelType, walletId, mobile,
						smsCodeType));
	}

	@ApiOperation("高级钱包用户修改手机认证")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_CHANGE_BIND_PHONE)
	public ResponseValue<String> seniorWalletPersonChangeBindPhone(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
					Integer channelType,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "姓名", required = true) @RequestParam("real_name") String realName,
			@ApiParam(value = "身份证号", required = true) @RequestParam("id_no") String idNo,
			@ApiParam(value = "手机号码", required = true) @RequestParam("old_phone") String oldPhone) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.seniorWalletPersonChangeBindPhone(accessToken, source, channelType, walletId, realName, idNo,
						oldPhone));
	}

	@ApiOperation("高级钱包企业用户绑定手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_BIND_PHONE)
	public ResponseValue<Long> seniorWalletBindPhone(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
					Integer channelType,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
			@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		return new ResponseValue<Long>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.seniorWalletBindPhone(accessToken, source, channelType, walletId, mobile, verifyCode));
	}

	@ApiOperation("高级钱包认证")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_AUTHENTICATION)
	public ResponseValue<String> seniorWalletPersonAuthentication(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
					Integer channelType,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "姓名", required = true) @RequestParam("real_name") String realName,
			@ApiParam(value = "身份证号", required = true) @RequestParam("id_no") String idNo,
			@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
			@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.seniorWalletPersonAuthentication(accessToken, source, channelType, walletId, realName, idNo,
						mobile, verifyCode));
	}

	@ApiOperation("高级钱包商家资料审核（通道）")
	@PostMapping(UrlConstant.WALLET_SENIOR_COMPANY_INFO_AUDIT)
	public ResponseValue<Integer> seniorWalletCompanyInfoAudit(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
					Integer channelType,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "审核方式", required = true) @RequestParam("audit_type") Integer auditType,
			@ApiParam(value = "企业信息(json)", required = true) @RequestParam("company_basic_info")
					String companyBasicInfo) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.seniorWalletCompanyAudit(accessToken, source, channelType, auditType, walletId,
						JsonUtil.toObject(companyBasicInfo, YunstSetCompanyInfoReq.CompanyBasicInfo.class,
								objectMapper -> {
									objectMapper.setTimeZone(TimeZone.getDefault());
									objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
								})));
	}

	@ApiOperation("高级钱包会员协议")
	@PostMapping(UrlConstant.WALLET_SENIOR_MEMBER_PROTOCOL)
	public ResponseValue<String> seniorWalletSignMemberProtocol(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.signMemberProtocol(accessToken, source, walletId));
	}

	@ApiOperation("高级钱包委托代扣协议")
	@PostMapping(UrlConstant.WALLET_SENIOR_BANLACE_PROTOCOL)
	public ResponseValue<String> seniorWalletSignBalanceProtocol(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.signBalanceProtocol(accessToken, source, walletId));
	}

	@ApiOperation("高级钱包验证银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_VERIFY_BANK_CARD)
	public ResponseValue<Long> seniorWalletVerifyBankCard(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "银行卡号", required = true) @RequestParam("card_no") String cardNo,
			@ApiParam(value = "姓名", required = true) @RequestParam("real_name") String realName,
			@ApiParam(value = "银行预留手机号", required = true) @RequestParam("phone") String phone,
			@ApiParam(value = "身份证", required = true) @RequestParam("identity_no") String identityNo,
			@ApiParam(value = "信用卡到期4位日期", required = false) @RequestParam(value = "validate", required = false)
					String validate,
			@ApiParam(value = "信用卡cvv2码", required = false) @RequestParam(value = "cvv2", required = false)
					String cvv2) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.seniorWalletVerifyBankCard(accessToken, walletId, source, cardNo, realName, phone,
						identityNo,
						validate, cvv2));
	}

	@ApiOperation("高级钱包确认绑定银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_CONFIRM_BIND_BANK_CARD)
	public ResponseValue<Long> seniorWalletConfirmBindBankCard(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
			@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
			@RequestParam("source") Byte source,
			@ApiParam(value = "验证银行卡流水号", required = true) @RequestParam("trans_num") String transNum,
			@ApiParam(value = "银行预留手机号", required = true) @RequestParam("phone") String phone,
			@ApiParam(value = "信用卡到期4位日期", required = false) @RequestParam(value = "validate", required = false)
					String validate,
			@ApiParam(value = "信用卡cvv2码", required = false) @RequestParam(value = "cvv2", required = false) String cvv2,
			@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
				walletApi.seniorWalletConfirmBindBankCard(accessToken, walletId, source, transNum, phone, validate,
						cvv2, verifyCode));
	}
}
