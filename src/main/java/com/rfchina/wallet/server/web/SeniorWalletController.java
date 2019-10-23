package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstApplyBindBankCardResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.SeniorWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SeniorWalletController {

	public static final ObjectSetter<ObjectMapper> DEF_REQ_OBJ_MAP = objectMapper -> {
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	};

	@Autowired
	private SeniorWalletService seniorWalletService;
	@Autowired
	private WalletApi walletApi;

	@ApiOperation("高级钱包-充值")
	@PostMapping(UrlConstant.SENIOR_WALLET_RECHARGE)
	public ResponseValue recharge(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "充值内容，参考RechargeReq结构体", required = true) @RequestParam("recharge_req") String rechargeReq
	) {

		RechargeReq req = JsonUtil.toObject(rechargeReq, RechargeReq.class, DEF_REQ_OBJ_MAP);
		seniorWalletService.recharge(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-定时代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_ASYNC)
	public ResponseValue<WalletCollect> collectAsync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		WalletCollect collect = seniorWalletService.preCollect(accessToken, req);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, collect);
	}

	@ApiOperation("高级钱包-即刻代收")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_SYNC)
	public ResponseValue<WalletCollect> collectSync(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收内容，参考CollectReq结构体", required = true) @RequestParam("collect_req") String collectReq
	) {
		CollectReq req = JsonUtil.toObject(collectReq, CollectReq.class, DEF_REQ_OBJ_MAP);
		WalletCollect walletCollect = seniorWalletService.preCollect(accessToken, req);
		WalletCollect result = seniorWalletService.doCollect(accessToken, walletCollect);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-代收结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_COLLECT_QUERY)
	public ResponseValue<WalletCollect> collectQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单号", required = true) @RequestParam("collect_order_no") String collectOrderNo
	) {
		WalletCollect collect = seniorWalletService.queryCollect(accessToken, collectOrderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, collect);
	}

	@ApiOperation("高级钱包-代付")
	@PostMapping(UrlConstant.SENIOR_WALLET_AGENT_PAY)
	public ResponseValue<SettleResp> agentPay(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单号", required = true) @RequestParam(value = "collect_order_no") String collectOrderNo,
		@ApiParam(value = "代付列表（与代收的分账规则对应），参考AgentPayReq结构体", required = true) @RequestParam("agent_pay_req") String agentPayReq
	) {
		AgentPayReq req = JsonUtil.toObject(agentPayReq, AgentPayReq.class, DEF_REQ_OBJ_MAP);
		seniorWalletService.agentPay(accessToken, collectOrderNo, req.getReceivers());
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-代付结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_AGENT_PAY_QUERY)
	public ResponseValue<WalletClearing> agentPayQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代付单号", required = true) @RequestParam(value = "pay_order_no") String payOrderNo
	) {
		WalletClearing walletClearings = seniorWalletService.agentPayQuery(accessToken, payOrderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletClearings);
	}

	@ApiOperation("高级钱包-退款")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND)
	public ResponseValue<WalletRefund> refund(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "代收单号", required = true) @RequestParam("collect_order_no") String collectOrderNo,
		@ApiParam(value = "退款清单，参考List<RefundInfo>结构体", required = true) @RequestParam("refund_list") String refundList
	) {
		List<RefundInfo> rList = JsonUtil.toArray(refundList, RefundInfo.class, DEF_REQ_OBJ_MAP);
		WalletRefund refund = seniorWalletService.refund(accessToken, collectOrderNo, rList);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}

	@ApiOperation("高级钱包-退款结果查询")
	@PostMapping(UrlConstant.SENIOR_WALLET_REFUND_QUERY)
	public ResponseValue<WalletRefund> refundQuery(
		@ApiParam(value = "应用令牌", required = true) @RequestParam("access_token") String accessToken,
		@ApiParam(value = "退款单号", required = true) @RequestParam("refund_order_no") String refundOrderNo
	) {
		WalletRefund refund = seniorWalletService.refundQuery(accessToken, refundOrderNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, refund);
	}

//****************************************************高级钱包基础相关接口****************************************************************************

	@ApiOperation("升级高级钱包")
	@PostMapping(UrlConstant.WALLET_UPGRADE)
	public ResponseValue<WalletChannel> seniorWalletUpgrade(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId)
		throws Exception {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.seniorWalletUpgrade(accessToken, source, channelType, walletId));
	}

	@ApiOperation("高级钱包渠道信息")
	@PostMapping(UrlConstant.WALLET_CHANNEL_INFO)
	public ResponseValue<WalletChannel> seniorWalletChannelInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId)
		throws Exception {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.seniorWalletChannelInfo(accessToken, channelType, walletId));
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
		@ApiParam(value = "短信类型", required = true) @RequestParam("sms_type") Integer smsCodeType)
		throws Exception {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi
				.seniorWalletSmsCodeVerification(accessToken, source, channelType, walletId, mobile,
					smsCodeType));
	}

	@ApiOperation("高级钱包个人用户修改手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_CHANGE_BIND_PHONE)
	public ResponseValue<String> seniorWalletPersonChangeBindPhone(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "姓名", required = true) @RequestParam("real_name") String realName,
		@ApiParam(value = "身份证号", required = true) @RequestParam("id_no") String idNo,
		@ApiParam(value = "手机号码", required = true) @RequestParam("old_phone") String oldPhone) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.seniorWalletPersonChangeBindPhone(accessToken, source, channelType, walletId,
				realName, idNo,
				oldPhone));
	}

	@ApiOperation("高级钱包企业用户绑定手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_BIND_PHONE)
	public ResponseValue<Long> seniorWalletBindPhone(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		return new ResponseValue<Long>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.seniorWalletBindPhone(accessToken, source, channelType, walletId, mobile,
				verifyCode));
	}

	@ApiOperation("高级钱包认证")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_AUTHENTICATION)
	public ResponseValue<String> seniorWalletPersonAuthentication(
		@RequestParam("access_token") String accessToken,
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
			walletApi.seniorWalletPersonAuthentication(accessToken, source, channelType, walletId,
				realName, idNo,
				mobile, verifyCode));
	}

	@ApiOperation("高级钱包商家资料审核（通道）")
	@PostMapping(UrlConstant.WALLET_SENIOR_COMPANY_INFO_AUDIT)
	public ResponseValue<Integer> seniorWalletCompanyInfoAudit(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "审核方式", required = true) @RequestParam("audit_type") Integer auditType,
		@ApiParam(value = "企业信息(json)", required = true) @RequestParam("company_basic_info")
			String companyBasicInfo) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi
				.seniorWalletCompanyAudit(accessToken, source, channelType, auditType, walletId,
					JsonUtil
						.toObject(companyBasicInfo, YunstSetCompanyInfoReq.CompanyBasicInfo.class,
							objectMapper -> {
								objectMapper.setTimeZone(TimeZone.getDefault());
								objectMapper
									.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
										false);
							})));
	}

	@ApiOperation("高级钱包会员协议")
	@PostMapping(UrlConstant.WALLET_SENIOR_MEMBER_PROTOCOL)
	public ResponseValue<String> seniorWalletSignMemberProtocol(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.signMemberProtocol(accessToken, source, walletId));
	}

	@ApiOperation("高级钱包委托代扣协议")
	@PostMapping(UrlConstant.WALLET_SENIOR_BANLACE_PROTOCOL)
	public ResponseValue<String> seniorWalletSignBalanceProtocol(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.signBalanceProtocol(accessToken, source, walletId));
	}

	@ApiOperation("高级钱包个人设置支付密码")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_SET_PAY_PASSWORD)
	public ResponseValue<String> seniorWalletPersonSetPayPassword(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "绑定手机", required = true) @RequestParam("phone") String phone,
		@ApiParam(value = "姓名", required = true) @RequestParam("name") String name,
		@ApiParam(value = "身份证", required = true) @RequestParam("identity_no") String identityNo) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.personSetPayPassword(accessToken, source, walletId, phone, name, identityNo));
	}

	@ApiOperation("高级钱包验证银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_VERIFY_BANK_CARD)
	public ResponseValue<YunstApplyBindBankCardResult> seniorWalletVerifyBankCard(
		@RequestParam("access_token") String accessToken,
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
			walletApi
				.seniorWalletVerifyBankCard(accessToken, walletId, source, cardNo, realName, phone,
					identityNo,
					validate, cvv2));
	}

	@ApiOperation("高级钱包确认绑定银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_CONFIRM_BIND_BANK_CARD)
	public ResponseValue<Long> seniorWalletConfirmBindBankCard(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "验证银行卡流水号", required = true) @RequestParam("trans_num") String transNum,
		@ApiParam(value = "验证银行卡申请时间", required = true) @RequestParam("trans_date") String transDate,
		@ApiParam(value = "银行预留手机号", required = true) @RequestParam("phone") String phone,
		@ApiParam(value = "信用卡到期4位日期", required = false) @RequestParam(value = "validate", required = false)
			String validate,
		@ApiParam(value = "信用卡cvv2码", required = false) @RequestParam(value = "cvv2", required = false) String cvv2,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi
				.seniorWalletConfirmBindBankCard(accessToken, walletId, source, transNum, transDate,
					phone, validate,
					cvv2, verifyCode));
	}


	@ApiOperation("高级钱包解除绑定银行卡")
	@PostMapping(UrlConstant.WALLET_SENIOR_UN_BIND_BANK_CARD)
	public ResponseValue<Long> seniorWalletUnBindBankCard(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "银行卡号", required = true) @RequestParam("card_no") String cardNo) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.seniorWalletUnBindBankCard(accessToken, walletId, source, cardNo));
	}


	@ApiOperation("高级钱包个人信息")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_INFO)
	public ResponseValue<YunstMemberInfoResult.PersonInfoResult> seniorWalletPersonInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.seniorWalletGetPersonInfo(accessToken, walletId, source));
	}


	@ApiOperation("高级钱包企业信息")
	@PostMapping(UrlConstant.WALLET_SENIOR_COMPANY_INFO)
	public ResponseValue<YunstMemberInfoResult.CompanyInfoResult> seniorWalletCompanyInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			walletApi.seniorWalletGetCompanyInfo(accessToken, walletId, source));
	}
}
