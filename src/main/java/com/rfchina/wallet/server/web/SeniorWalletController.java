package com.rfchina.wallet.server.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.api.SeniorWalletApi;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstMemberInfoResult;
import com.rfchina.wallet.server.model.ext.PageVo;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseValue<WalletTunnel> seniorWalletUpgrade(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户", required = true, example = "2")
		@RequestParam("source") Byte source,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId)
		throws Exception {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi.seniorWalletUpgrade(accessToken, source, channelType, walletId));
	}

	@ApiOperation("高级钱包渠道信息")
	@PostMapping(UrlConstant.WALLET_CHANNEL_INFO)
	public ResponseValue<WalletTunnel> seniorWalletChannelInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true) @RequestParam("tunnel_type") Byte tunnelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		WalletTunnel channel = seniorWalletApi
			.seniorWalletChannelInfo(accessToken, tunnelType, walletId);

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

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi
				.seniorWalletSmsCodeVerification(accessToken, source, channelType, walletId, mobile,
					smsCodeType));
	}

	@ApiOperation("高级钱包-修改手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_RESET_SECURITY_TEL)
	public ResponseValue<PageVo> resetSecurityTel(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "前端回跳地址") @RequestParam(value = "jump_url", required = false) String jumpUrl) {

		String redirectUrl = seniorWalletApi.resetSecurityTel(accessToken, walletId, jumpUrl);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			PageVo.builder().url(redirectUrl).build());
	}

	@ApiOperation("高级钱包-企业用户绑定手机")
	@PostMapping(UrlConstant.WALLET_SENIOR_BIND_PHONE)
	public ResponseValue<Wallet> seniorWalletBindPhone(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode) {

		return new ResponseValue<Wallet>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi
				.seniorWalletBindPhone(accessToken, channelType, walletId, mobile,
					verifyCode));
	}

	@ApiOperation("高级钱包-个人认证")
	@PostMapping(UrlConstant.WALLET_SENIOR_PERSON_AUTHENTICATION)
	public ResponseValue<PageVo> seniorWalletPersonAuthentication(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "姓名", required = true) @RequestParam("real_name") String realName,
		@ApiParam(value = "身份证号", required = true) @RequestParam("id_no") String idNo,
		@ApiParam(value = "手机号码", required = true) @RequestParam("mobile") String mobile,
		@ApiParam(value = "短信验证码", required = true) @RequestParam("verify_code") String verifyCode,
		@ApiParam(value = "前端跳转地址", required = true) @RequestParam("jump_url") String jumpUrl) {
		String tongLianUrl = seniorWalletApi
			.seniorWalletPersonAuthentication(accessToken, channelType, walletId,
				realName, idNo, mobile, verifyCode, jumpUrl);
		PageVo pageVo = PageVo.builder().url(tongLianUrl).build();
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, pageVo);
	}

	@ApiOperation("高级钱包-商家资料审核（通道）")
	@PostMapping(UrlConstant.WALLET_SENIOR_COMPANY_INFO_AUDIT)
	public ResponseValue<WalletTunnel> seniorWalletCompanyInfoAudit(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "渠道类型 1:浦发银企直连,2:通联云商通", required = true, example = "1") @RequestParam("channel_type")
			Integer channelType,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "审核方式", required = true) @RequestParam("audit_type") Integer auditType,
		@ApiParam(value = "企业信息(json)", required = true) @RequestParam("company_basic_info")
			String companyBasicInfo) {
		WalletTunnel walletChannel = seniorWalletApi
			.seniorWalletCompanyAudit(accessToken, channelType, auditType, walletId,
				JsonUtil.toObject(companyBasicInfo, YunstSetCompanyInfoReq.CompanyBasicInfo.class,
					objectMapper -> {
						objectMapper.setTimeZone(TimeZone.getDefault());
						objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
							false);
					}));
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

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi.seniorWalletGetPersonInfo(accessToken, walletId));
	}


	@ApiOperation("高级钱包-企业信息")
	@PostMapping(UrlConstant.WALLET_SENIOR_COMPANY_INFO)
	public ResponseValue<YunstMemberInfoResult.CompanyInfoResult> seniorWalletCompanyInfo(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包id", required = true) @RequestParam("wallet_id") Long walletId) {

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			seniorWalletApi.seniorWalletGetCompanyInfo(accessToken, walletId));
	}
}
