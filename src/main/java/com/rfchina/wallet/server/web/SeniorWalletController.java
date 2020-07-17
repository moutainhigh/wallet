package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletTerminal;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.api.SeniorWalletApi;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq.CompanyBasicInfo;
import com.rfchina.wallet.server.bank.yunst.response.VspTermidResp;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.CompanyInfoResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult.PersonInfoResult;
import com.rfchina.wallet.server.model.ext.PageVo;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SeniorWalletController {

	@Autowired
	private SeniorWalletApi seniorWalletApi;

	@ApiOperation("升级高级钱包")
	@PostMapping(UrlConstant.WALLET_UPGRADE)
	public ResponseValue<WalletTunnel> seniorWalletCreateTunnel(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId)
		throws Exception {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi.seniorWalletCreateTunnel(accessToken, source, channelType, walletId));
	}

	@ApiOperation("高级钱包渠道信息")
	@PostMapping(UrlConstant.WALLET_TUNNEL_INFO)
	public ResponseValue<WalletTunnel> seniorWalletTunnelInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true) @RequestParam("tunnel_type") Byte tunnelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		WalletTunnel channel = seniorWalletApi
			.seniorWalletTunnelInfo(accessToken, tunnelType, walletId);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, channel);
	}

	@ApiOperation("高级钱包认证验证码")
	@PostMapping(UrlConstant.WALLET_SENIOR_SMS_VERIFY_CODE)
	public ResponseValue<WalletTunnel> seniorWalletSmsCodeVerification(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "短信类型", required = true) @RequestParam("sms_type") Integer smsCodeType)
		throws Exception {

		WalletTunnel tunnel = seniorWalletApi.seniorWalletSmsCodeVerification(accessToken,
			source, channelType, walletId, mobile, smsCodeType);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, tunnel);
	}

	@ApiOperation("高级钱包-修改手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_UPDATE_SECURITY_TEL)
	public ResponseValue<PageVo> updateSecurityTel(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "前端回跳地址") @RequestParam(value = "jump_url", required = false) String jumpUrl) {

		String redirectUrl = seniorWalletApi.updateSecurityTel(accessToken, walletId, jumpUrl);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			PageVo.builder().url(redirectUrl).build());
	}

	@ApiOperation("高级钱包-企业用户绑定手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_BIND_PHONE)
	public ResponseValue<Wallet> seniorWalletBindPhone(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type") Byte channelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		return new ResponseValue<Wallet>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi.bindPhone(accessToken, channelType, walletId, mobile, verifyCode));
	}

	@ApiOperation("高级钱包-解绑手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_UNBIND_PHONE)
	public ResponseValue unBindPhone(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type") Byte channelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		seniorWalletApi.unBindPhone(accessToken, channelType, walletId, mobile, verifyCode);
		return new ResponseValue(EnumResponseCode.COMMON_SUCCESS, null);
	}


	@ApiOperation("高级钱包-个人认证")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_AUTHENTICATION)
	public ResponseValue<PageVo> seniorPersonAuthentication(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("tunnel_type") Byte tunnelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "用户id", required = true) @RequestParam("user_id") Long userId,
		@ApiParam(value = "姓名", required = true) @RequestParam("real_name") String realName,
		@ApiParam(value = "身份证号", required = true) @RequestParam("id_no") String idNo,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode,
		@ApiParam(value = "前端跳转地址", required = true) @RequestParam("jump_url") String jumpUrl) {

		String protocolUrl = seniorWalletApi.personAudit(accessToken, tunnelType, walletId, userId,
			realName, idNo, mobile, verifyCode, jumpUrl);
		PageVo pageVo = PageVo.builder().url(protocolUrl).build();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, pageVo);
	}

	@ApiOperation("高级钱包-用户绑定钱包身份")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_IDBIND)
	public ResponseValue seniorPersonIdBind(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "用户id", required = true) @RequestParam("user_id") Long userId) {

		seniorWalletApi.personIdBind(accessToken, walletId, userId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-商家资料审核（通道）")
	@PostMapping(UrlConstant.WALLET_SENIOR_COMPANY_INFO_AUDIT)
	public ResponseValue<WalletTunnel> seniorWalletCompanyInfoAudit(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type") Integer channelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "审核方式", required = true) @RequestParam("audit_type") Integer auditType,
		@ApiParam(value = "企业信息(json)", required = true) @RequestParam("company_basic_info") String companyBasicInfo) {

		CompanyBasicInfo companyInfo = JsonUtil.toObject(companyBasicInfo, CompanyBasicInfo.class,
			objectMapper -> {
				objectMapper.setTimeZone(TimeZone.getDefault());
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			});
		WalletTunnel walletChannel = seniorWalletApi
			.setCompanyInfo(accessToken, channelType, auditType, walletId, companyInfo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, walletChannel);
	}

	@ApiOperation("高级钱包-会员协议")
	@PostMapping(UrlConstant.WALLET_SENIOR_MEMBER_PROTOCOL)
	public ResponseValue<PageVo> seniorWalletSignMemberProtocol(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "前端跳转地址", required = true) @RequestParam("jump_url") String jumpUrl) {
		String tongLianUrl = seniorWalletApi.signMemberProtocol(accessToken, walletId, jumpUrl);
		PageVo pageVo = PageVo.builder().url(tongLianUrl).build();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, pageVo);
	}

	@ApiOperation("高级钱包-委托代扣协议")
	@PostMapping(UrlConstant.WALLET_SENIOR_BANLACE_PROTOCOL)
	public ResponseValue<PageVo> seniorWalletSignBalanceProtocol(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "前端跳转地址", required = true) @RequestParam("jump_url") String jumpUrl) {
		String tongLianUrl = seniorWalletApi.signBalanceProtocol(accessToken, walletId, jumpUrl);
		PageVo pageVo = PageVo.builder().url(tongLianUrl).build();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, pageVo);
	}

	@ApiOperation("高级钱包-个人设置支付密码")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_SET_PAY_PASSWORD)
	public ResponseValue<PageVo> seniorWalletPersonSetPayPassword(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "前端跳转地址", required = true) @RequestParam("jump_url") String jumpUrl) {
		String tongLianUrl = seniorWalletApi
			.personSetPayPassword(accessToken, walletId, jumpUrl);
		PageVo pageVo = PageVo.builder().url(tongLianUrl).build();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, pageVo);
	}

	@ApiOperation("高级钱包-修改支付密码")
	@PostMapping(UrlConstant.WALLET_SENIOR_UPDATE_PAY_PWD)
	public ResponseValue<PageVo> updatePayPwd(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "前端跳转地址", required = true) @RequestParam("jump_url") String jumpUrl) {

		String redirectUrl = seniorWalletApi.updatePayPwd(accessToken, walletId, jumpUrl);
		PageVo pageVo = PageVo.builder().url(redirectUrl).build();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, pageVo);
	}

	@ApiOperation("高级钱包-重置支付密码")
	@PostMapping(UrlConstant.WALLET_SENIOR_RESET_PAY_PWD)
	public ResponseValue<PageVo> resetPayPwd(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "前端跳转地址", required = true) @RequestParam("jump_url") String jumpUrl) {

		String redirectUrl = seniorWalletApi.resetPayPwd(accessToken, walletId, jumpUrl);
		PageVo pageVo = PageVo.builder().url(redirectUrl).build();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, pageVo);
	}

	@ApiOperation("高级钱包-个人信息")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_INFO)
	public ResponseValue<YunstMemberInfoResult.PersonInfoResult> seniorWalletPersonInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		PersonInfoResult result = seniorWalletApi.getPersonInfo(accessToken, walletId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}


	@ApiOperation("高级钱包-企业信息")
	@PostMapping(UrlConstant.WALLET_SENIOR_COMPANY_INFO)
	public ResponseValue<YunstMemberInfoResult.CompanyInfoResult> seniorWalletCompanyInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId
	) {

		CompanyInfoResult result = seniorWalletApi.getCompanyInfo(accessToken, walletId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}


	@ApiOperation("高级钱包-余额明细")
	@PostMapping(UrlConstant.WALLET_SENIOR_ORDER_DETAIL)
	public ResponseValue<Pagination<WalletOrder>> seniorOrderDetail(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = false) @RequestParam(value = "wallet_id", required = false) Long walletId,
		@ApiParam(value = "交易时间开始", required = false) @RequestParam(value = "from_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
			Date fromTime,
		@ApiParam(value = "交易时间结束", required = false) @RequestParam(value = "to_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
			Date toTime,
		@ApiParam(value = "交易类型", required = false) @RequestParam(value = "trade_type", required = false) Byte tradeType,
		@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) Byte status,
		@ApiParam(value = "每页限制", required = false) @RequestParam(value = "limit") int limit,
		@ApiParam(value = "起始页偏移量", required = false) @RequestParam(value = "offset") int offset,
		@ApiParam(value = "是否统计", required = false) @RequestParam(value = "stat", required = false) Boolean stat) {
		if (Objects.nonNull(fromTime)) {
			toTime = Objects.isNull(toTime) ? new Date()
				: fromTime.compareTo(toTime) == 0 ? DateUtil
					.addSecs(DateUtil.addDate2(toTime, 1), -1) : toTime;
		}
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi
				.queryWalletOrderDetail(accessToken, walletId, fromTime, toTime, tradeType, status,
					limit, offset, stat));
	}


	@ApiOperation("高级钱包-线下确认更新企业信息")
	@PostMapping(UrlConstant.M_WALLET_MANUAL_COMPANY_AUDIT)
	public ResponseValue<YunstMemberInfoResult.CompanyInfoResult> manualCompanyAudit(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId
	) {

		YunstMemberInfoResult.CompanyInfoResult result = seniorWalletApi
			.manualCompanyAudit(accessToken, walletId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result);
	}

	@ApiOperation("高级钱包-商家绑定终端")
	@PostMapping(UrlConstant.WALLET_BIND_TERMINAL2)
	public ResponseValue<VspTermidResp> bindTerminal2(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道用户ID", required = true) @RequestParam("biz_user_id") String bizUserId,
		@ApiParam(value = "集团号", required = true) @RequestParam("vsp_merchantid") String vspMerchantid,
		@ApiParam(value = "子商户号", required = true) @RequestParam("vsp_cusid") String vspCusid,
		@ApiParam(value = "APPID", required = true) @RequestParam("app_id") String appId,
		@ApiParam(value = "终端号", required = true) @RequestParam("vsp_termid") String vspTermid
	) {

		VspTermidResp resp = seniorWalletApi
			.bindTerminal(bizUserId, vspMerchantid, vspCusid, appId, vspTermid);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

	@ApiOperation("高级钱包-商家绑定终端")
	@PostMapping(UrlConstant.WALLET_BIND_TERMINAL)
	public ResponseValue bindTerminal(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "终端id", required = true) @RequestParam("terminal_id") Long terminalId,
		@ApiParam(value = "类型： 1：绑定，2：解绑", required = true) @RequestParam("opt_type") Byte optType
	) {

		seniorWalletApi.bindTerminal(walletId, terminalId);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

	@ApiOperation("高级钱包-终端列表")
	@PostMapping(UrlConstant.WALLET_QUERY_TERMINAL)
	public ResponseValue<Pagination<WalletTerminal>> queryTerminal(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = false) @RequestParam(value = "wallet_id", required = false) Long walletId,
		@ApiParam(value = "子商户号", required = false) @RequestParam(value = "vsp_cusid", required = false) String vspCusid,
		@ApiParam(value = "终端号", required = false) @RequestParam(value = "vsp_termid", required = false) String vspTermid,
		@ApiParam(value = "省份", required = false) @RequestParam(value = "province", required = false) String province,
		@ApiParam(value = "商家id", required = false) @RequestParam(value = "mch_id", required = false) String mchId,
		@ApiParam(value = "limit", required = true) @RequestParam("limit") Integer limit,
		@ApiParam(value = "offset", required = true) @RequestParam("offset") Integer offset
	) {

		Pagination<WalletTerminal> page = seniorWalletApi
			.queryTerminal(walletId, vspCusid, vspTermid, province, mchId, limit, offset);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, page);
	}

	@ApiOperation("高级钱包-新增终端")
	@PostMapping(UrlConstant.WALLET_CREATE_TERMINAL)
	public ResponseValue createTerminal(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "通联appId", required = true) @RequestParam("app_id") String appId,
		@ApiParam(value = "集团商户号", required = true) @RequestParam("vsp_merchantid") String vspMerchantid,
		@ApiParam(value = "子商户号", required = true) @RequestParam("vsp_cusid") String vspCusid,
		@ApiParam(value = "终端号", required = true) @RequestParam("vsp_termid") String vspTermid,
		@ApiParam(value = "省份", required = true) @RequestParam("province") String province,
		@ApiParam(value = "商家id", required = true) @RequestParam("mch_id") String mchId,
		@ApiParam(value = "商家名称", required = true) @RequestParam("mch_name") String mchName,
		@ApiParam(value = "门店地址", required = true) @RequestParam("shop_address") String shopAddress
	) {

		seniorWalletApi.createTerminal(appId, vspMerchantid, vspCusid, vspTermid, province,
			mchId, mchName, shopAddress);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}
}
