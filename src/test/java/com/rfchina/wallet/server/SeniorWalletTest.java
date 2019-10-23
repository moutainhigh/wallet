package com.rfchina.wallet.server;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeniorWalletTest extends WalletBaseTest {

	@Autowired
	private com.rfchina.internal.api.ApiClient internalApiClient;

	/**
	 * 钱包升级
	 */
	@Test
	public void testWalletUpgrade() {
		upgradeWallet(2, (byte) 3, 11L);
	}

	/**
	 * 钱包渠道信息
	 */
	@Test
	public void testSeniorWalletChannelInfo() {
		seniorWalletChannel(2,  11L);
	}

	/**
	 * 高级钱包更改绑定手机
	 */
	@Test
	public void testSeniorWalletChangeBindPhone() {
		seniorWalletChangeBindPhone(2, (byte) 3, 11L,"张二丰", "440104198803124487", "13800138111");
	}

	/**
	 * 高级钱包申请绑定手机（发送验证码）
	 */
	@Test
	public void testSeniorWalletApplyBindPhone() {
		seniorWalletApplyBindPhone(2, (byte) 3, 21L, "13800138111", 9);
	}

	/**
	 * 高级钱包绑定手机
	 */
	@Test
	public void testSeniorWalletBindPhone() {
		seniorWalletBindPhone(2, (byte) 3, 11L,  "13800138111", "11111");
	}

	/**
	 * 高级钱包个人验证
	 */
	@Test
	public void testSeniorWalletAuth() {
		seniorWalletAuth(2, (byte) 3, 11L, "张二丰", "440104198803124487", "13800138111", "11111");
	}

	/**
	 * 高级钱包商户提交审核资料
	 * @throws Exception
	 */
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

	/**
	 * 高级钱包电子会员协议
	 */
	@Test
	public void testSeniorWalletSignMemberProtocol() {
		seniorWalletSignMemberProtocol( (byte) 3, 11L);
	}

	/**
	 * 高级钱包扣款协议
	 */
	@Test
	public void testSeniorWalletSignBalanceProtocol() {
		seniorWalletSignBalanceProtocol( (byte) 3, 11L);
	}

	/**
	 * 高级钱包设置支付密码
	 */
	@Test
	public void testSeniorWalletPersonSetPayPassword() {
		seniorWalletPersonSetPayPassword( (byte) 3, 11L,"张二丰","13800138111","440104198803124487");
	}

}
