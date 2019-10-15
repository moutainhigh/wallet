package com.rfchina.wallet.server;

import com.alibaba.fastjson.JSONObject;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimeZone;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WalletTest extends WalletBaseTest {

	@Autowired
	private com.rfchina.internal.api.ApiClient internalApiClient;

	@Test
	public void testWalletInfo() {
		walletInfo(36L);
	}

	@Test
	public void testCreateWallet() {
		createWallet((byte) 2, "个人钱包1", (byte) 3);
	}

	@Test
	public void testWalletLogList() {
		walletLogList(1L, null, null, 10, 0, true);
	}

	@Test
	public void testBindingBankCardList() {
		bindingBankCardList(1L);
	}

	@Test
	public void testBindBankCard() {
		bindBankCard(1L, "402336100092", "12345678901234567890", "哈哈", 1, "");
	}

	@Test
	public void testBankClassList() {
		bankClassList();
	}

	@Test
	public void testBankAreaList() {
		bankAreaList("001");
	}

	@Test
	public void testBankList() {
		bankList("001", "4910");
	}

	@Test
	public void testQueryWalletByUserId() {
		queryWalletByUserId(30799L);
	}

	@Test
	public void testSendVerifyCode() {
		sendVerifyCode("13560166318", 1, "127.0.0.1");
	}

	@Test
	public void testLoginWithVerify() {
		loginWithVerify("13560166318", "12345", 1, "127.0.0.1");
	}

	@Test
	public void testWalletUpgrade() {
		upgradeWallet(2, (byte) 3, 11L);
	}

	@Test
	public void testSeniorWalletChangeBindPhone() {
		seniorWalletChangeBindPhone(2, (byte) 3, 11L,"张二丰", "440104198803124487", "13800138111");
	}

	@Test
	public void testSeniorWalletAuthBindPhone() {
		seniorWalletApplyBindPhone(2, (byte) 3, 21L, "13800138111", 9);
	}


	@Test
	public void testSeniorWalletAuth() {
		seniorWalletAuth(2, (byte) 3, 11L, "张二丰", "440104198803124487", "13800138111", "11111");
	}

	@Test
	public void testSeniorWalletCompanyInfoAudit() throws Exception {
		JSONObject companyBasicInfo = new JSONObject();
		companyBasicInfo.put("companyName", "通联支付网络服务有限公司2");
		companyBasicInfo.put("companyAddress", "浙江省宁波市");
		companyBasicInfo.put("authType", 1L);//1-三证，2-一证
		companyBasicInfo.put("uniCredit", "111111");
		companyBasicInfo.put("businessLicense", "222222");
		companyBasicInfo.put("organizationCode", "333333");
		companyBasicInfo.put("taxRegister", "444444");
		companyBasicInfo.put("expLicense", "2020-1-1");
		companyBasicInfo.put("telephone", "555555");
		companyBasicInfo.put("legalName", "邬海艳");
		companyBasicInfo.put("identityType", 1L);
		companyBasicInfo.put("legalIds", "666666");
		companyBasicInfo.put("legalPhone", "777777");
		companyBasicInfo.put("accountNo", "6228481000000051211");
		companyBasicInfo.put("parentBankName", "农业银行");
		companyBasicInfo.put("bankCityNo", "777777");
		companyBasicInfo.put("bankName", "农业银行");
		companyBasicInfo.put("unionBank", "010300000000");
		companyBasicInfo.put("bankCityNo", "");
		companyBasicInfo.put("province", "上海");
		companyBasicInfo.put("city", "上海");

//		String json = companyBasicInfo.toJSONString();

//		YunstSetCompanyInfoReq.CompanyBasicInfo info = JsonUtil.toObject(json, YunstSetCompanyInfoReq.CompanyBasicInfo.class,
//				objectMapper -> {
//					objectMapper.setTimeZone(TimeZone.getDefault());
//					objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				});
//
//		System.out.println(info);
		seniorWalletCompanyInfoAudit(2,(byte)1,12L,2,companyBasicInfo.toJSONString());
	}


}
