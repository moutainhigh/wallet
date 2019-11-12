package com.rfchina.wallet.server;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunConfig;
import com.rfchina.platform.common.security.SecurityCoder;
import com.rfchina.platform.common.utils.SignUtil;
import com.rfchina.platform.unittest.BaseTest;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.service.ConfigService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class WalletBaseTest extends BaseTest {

	@Value("${app.base.url}")
	protected String BASE_URL;

	@Autowired
	private ConfigService configService;

	static {
		System.setProperty("spring.profiles.active", "dev");
	}


	@Before
	public void initYunst() throws Exception {
		Process process = Runtime.getRuntime().exec("git config user.name");
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String name = reader.readLine();
		if (name.startsWith("niezengming")) {
			YunClient.configure(new YunConfig(configService.getYstServerUrl(),
				configService.getYstSysId(),
				configService.getYstPassword(), configService.getYstAlias(),
				configService.getYstVersion(),
				"/data/env/config/dev-key/yunst/1902271423530473681.pfx",
				"/data/env/config/dev-key/yunst/TLCert-test.cer"));

		}
	}

	@Autowired
	protected MockHttpSession session;

	protected Map<String, Object> walletInfo(Long walletId) {
		Map<String, String> params = new HashMap<>();

		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("wallet_id", String.valueOf(walletId));

		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_QUERY_INFO, params);
	}

	protected Map<String, Object> createWallet(Byte type, String title, Byte source) {
		Map<String, String> params = new HashMap<>();

		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("type", String.valueOf(type));
		params.put("title", title);
		params.put("source", String.valueOf(source));

		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);

		return postAndValidateSuccessCode(BASE_URL, UrlConstant.CREATE_WALLET, params);
	}

	protected Map<String, Object> walletLogList(Long walletId, String startTime, String endTime,
		int limit,
		long offset,
		Boolean stat) {
		Map<String, String> params = new HashMap<>();

		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("limit", String.valueOf(limit));
		params.put("offset", String.valueOf(offset));
		params.put("stat", String.valueOf(stat));

		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);

		Optional.ofNullable(startTime).ifPresent(o -> params.put("start_time", startTime));
		Optional.ofNullable(endTime).ifPresent(o -> params.put("end_time", endTime));
		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_LOG_LIST, params);
	}

	protected Map<String, Object> bindingBankCardList(Long walletId) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("wallet_id", String.valueOf(walletId));

		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);

		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_CARD_LIST, params);
	}

	protected Map<String, Object> bindBankCard(Long walletId, String bankCode, String bankAccount,
		String depositName,
		Integer isDef, String telephone) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("bank_code", bankCode);
		params.put("bank_account", bankAccount);
		params.put("deposit_name", depositName);
		params.put("is_def", String.valueOf(isDef));
		params.put("telephone", telephone);

		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);

		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_CARD_BIND, params);
	}

	protected Map<String, Object> bankClassList() {
		Map<String, String> params = new HashMap<>();
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_CLASS_LIST, params);
	}

	protected Map<String, Object> bankAreaList(String classCode) {
		Map<String, String> params = new HashMap<>();
		params.put("class_code", classCode);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_AREA_LIST, params);
	}

	protected Map<String, Object> bankList(String classCode, String areaCode) {
		Map<String, String> params = new HashMap<>();
		params.put("class_code", classCode);
		params.put("area_code", areaCode);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_LIST, params);
	}

	protected Map<String, Object> queryWalletByUserId(Long userId) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("user_id", userId.toString());
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_QUERY_INFO_BY_UID, params);
	}

	protected Map<String, Object> sendVerifyCode(String mobile, Integer type, String ip) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("mobile", mobile);
		params.put("type", String.valueOf(type));
		params.put("ip", ip);
		params.put("verify_token", "123");
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SEND_VERIFY_CODE, params, 2033);
	}

	protected Map<String, Object> loginWithVerify(String mobile, String verifyCode, Integer type,
		String ip) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("mobile", mobile);
		params.put("verify_code", verifyCode);
		params.put("ip", ip);
		params.put("type", String.valueOf(type));
		params.put("verify_token", "123");
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_LOGIN_WITH_VERIFY_CODE, params,
			2033);
	}

	protected Map<String, Object> upgradeWallet(Integer channelType, Byte source, Long walletId) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("channel_type", String.valueOf(channelType));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_UPGRADE, params, 1001);
	}

	protected Map<String, Object> seniorWalletApplyBindPhone(Integer channelType, Byte source,
		Long walletId,
		String mobile, Integer smsType) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("channel_type", String.valueOf(channelType));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("mobile", String.valueOf(mobile));
		params.put("sms_type", String.valueOf(smsType));
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_SMS_VERIFY_CODE, params,
			1001);
	}

	protected Map<String, Object> seniorWalletBindPhone(Integer channelType, Byte source,
		Long walletId,
		String mobile, String verifyCode) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("channel_type", String.valueOf(channelType));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("mobile", String.valueOf(mobile));
		params.put("verify_code", verifyCode);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_BIND_PHONE, params,
			1001);
	}

	protected Map<String, Object> seniorWalletChangeBindPhone(Integer channelType, Byte source,
		Long walletId,
		String realName, String idNo, String oldPhone) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("channel_type", String.valueOf(channelType));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("real_name", realName);
		params.put("id_no", idNo);
		params.put("old_phone", oldPhone);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_UPDATE_SECURITY_TEL,
			params, 1001);
	}

	protected Map<String, Object> seniorWalletAuth(Integer channelType, Byte source, Long walletId,
		String realName,
		String idNo, String mobile, String smsVerifyCode) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("channel_type", String.valueOf(channelType));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("real_name", realName);
		params.put("id_no", idNo);
		params.put("mobile", String.valueOf(mobile));
		params.put("verify_code", smsVerifyCode);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_PERSON_AUTHENTICATION,
			params, 1001);
	}

	protected Map<String, Object> seniorWalletCompanyInfoAudit(Integer channelType, Byte source,
		Long walletId,
		Integer auditType, String companyBasicInfo) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("channel_type", String.valueOf(channelType));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("audit_type", String.valueOf(auditType));
		params.put("company_basic_info", companyBasicInfo);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_COMPANY_INFO_AUDIT,
			params, 1001);
	}


	protected Map<String, Object> seniorWalletChannel(Integer channelType, Long walletId) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("channel_type", String.valueOf(channelType));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_TUNNEL_INFO, params, 1001);
	}


	protected Map<String, Object> seniorWalletSignMemberProtocol(Byte source, Long walletId) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_MEMBER_PROTOCOL, params,
			1001);
	}

	protected Map<String, Object> seniorWalletSignBalanceProtocol(Byte source, Long walletId) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("source", String.valueOf(source));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_BANLACE_PROTOCOL, params,
			1001);
	}


	protected Map<String, Object> seniorWalletPersonSetPayPassword(Long walletId,
		String jumpUrl) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", getAccessToken(appId, appSecret));
		params.put("wallet_id", String.valueOf(walletId));
		params.put("jump_url", jumpUrl);
		params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
		params.put("sign", sign);
		return postAndValidateSpecCode(BASE_URL, UrlConstant.WALLET_SENIOR_PERSON_SET_PAY_PASSWORD,
			params, 1001);
	}
}
