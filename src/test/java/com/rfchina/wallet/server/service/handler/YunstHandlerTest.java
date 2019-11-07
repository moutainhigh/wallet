package com.rfchina.wallet.server.service.handler;

import static org.junit.Assert.assertNotNull;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.FileUtil;
import com.rfchina.wallet.domain.misc.EnumDef.EnumVerifyCodeType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletSource;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.exception.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.ApplyBindBankCardResp;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstBindBankCardResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstBindPhoneResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstPersonSetRealNameResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSendVerificationCodeResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSetCompanyInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import com.rfchina.wallet.server.utils.IdCardGenerator;
import com.rfchina.wallet.server.utils.RandomUtils;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class YunstHandlerTest extends SpringBaseTest {

	@Autowired
	private YunstUserHandler yunstUserHandler;
	@Autowired
	private WalletTunnelExtDao walletTunnelExtDao;

	@Test
	public void createMember() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(null);
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);

		assertNotNull(member);
		logStack(member);

	}

	@Test
	public void sendSmsVerificationCode() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		String tel = genRamdomTelphone(null);
		YunstSendVerificationCodeResult result = yunstUserHandler
			.sendVerificationCode(member.left.getBizUserId(), tel, EnumVerifyCodeType.YUNST_BIND_PHONE.getValue());
		logStack(result);
	}

	@Test
	public void bindPhone() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		String tel = genRamdomTelphone(null);
		YunstSendVerificationCodeResult result = yunstUserHandler.sendVerificationCode(member.left.getBizUserId(), tel, EnumVerifyCodeType.YUNST_BIND_PHONE.getValue());
		assertNotNull(result);
		logStack(result);
		YunstBindPhoneResult result2 = yunstUserHandler.bindPhone(member.left.getBizUserId(), tel, "11111");
		logStack(result2);
	}

	@Test
	public void modifyPhone() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		String tel = genRamdomTelphone(null);
		YunstSendVerificationCodeResult result = yunstUserHandler.sendVerificationCode(member.left.getBizUserId(), tel, EnumVerifyCodeType.YUNST_BIND_PHONE.getValue());
		assertNotNull(result);
		logStack(result);
		YunstBindPhoneResult result2 = yunstUserHandler.bindPhone(member.left.getBizUserId(), tel, "11111");
		logStack(result2);
		String realName = RandomUtils.getChineseName();
		Long identityType = 1L;
		String identityNo = new IdCardGenerator().generate();
		YunstPersonSetRealNameResult result3 = yunstUserHandler.personCertification(member.left.getBizUserId(), realName, identityType, identityNo);
		logStack(result3);
		String url = yunstUserHandler.resetSecurityTel(member.left.getBizUserId(), realName,tel, identityNo,"");
		logStack(url);
		assertNotNull(url);
	}

	@Test
	public void getMemberInfo() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		Object result = yunstUserHandler.getMemberInfo(member.left.getBizUserId());
		logStack(result);
	}

	@Test
	public void getPersonCertification() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		String realName = RandomUtils.getChineseName();
		Long identityType = 1L;
		String identityNo = new IdCardGenerator().generate();
		YunstPersonSetRealNameResult result = yunstUserHandler.personCertification(member.left.getBizUserId(), realName, identityType, identityNo);
		logStack(result);
		String url = yunstUserHandler.generateSignContractUrl(member.left.getBizUserId(),"/#/success");
		logStack(url);
	}

	@Test
	public void signBalanceProtocol() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		String realName = RandomUtils.getChineseName();
		Long identityType = 1L;
		String identityNo = new IdCardGenerator().generate();
		YunstPersonSetRealNameResult result = yunstUserHandler.personCertification(member.left.getBizUserId(), realName, identityType, identityNo);
		logStack(result);
		Tuple<String, String> url = yunstUserHandler.generateBalanceProtocolUrl(member.left.getBizUserId(),"/#/success");
		logStack(url);
	}

	@Test
	public void setCompanyInfo() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.FHT_CORP.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		/**
		 * 	companyBasicInfo.put("companyName", "通联支付网络服务有限公司");
		 * 			companyBasicInfo.put("companyAddress", "浙江省宁波市");
		 * 			companyBasicInfo.put("authType", 1L);//1-三证，2-一证
		 * 			companyBasicInfo.put("uniCredit", "111111");
		 * 			companyBasicInfo.put("businessLicense", "222222");
		 * 			companyBasicInfo.put("organizationCode", "333333");
		 * 			companyBasicInfo.put("taxRegister", "444444");
		 * 			companyBasicInfo.put("expLicense", "2020-1-1");
		 * 			companyBasicInfo.put("telephone", "555555");
		 * 			companyBasicInfo.put("legalName", "邬海艳");
		 * 			companyBasicInfo.put("identityType", 1L);
		 * 			companyBasicInfo.put("legalIds", RSAUtil.encrypt("666666"));
		 * 			companyBasicInfo.put("legalPhone", "777777");
		 * 			companyBasicInfo.put("accountNo", RSAUtil.encrypt("6228481000000051211"));
		 * 			companyBasicInfo.put("parentBankName", "农业银行");
		 * 			companyBasicInfo.put("bankCityNo", "777777");
		 * 			companyBasicInfo.put("bankName", "农业银行");
		 * 			companyBasicInfo.put("unionBank", "010300000000");
		 * 			companyBasicInfo.put("bankCityNo", "");
		 * 			companyBasicInfo.put("province", "上海");
		 * 			companyBasicInfo.put("city", "上海");
		 */
		YunstSetCompanyInfoResult result = yunstUserHandler.setCompanyInfo(member.left.getBizUserId(), false,
				YunstSetCompanyInfoReq.CompanyBasicInfo.builder()
						.companyName("通联支付网络服务有限公司")
						.companyAddress("浙江省宁波市")
						.authType(1L)
						.uniCredit("111111")
						.businessLicense("222222")
						.organizationCode("333333")
						.taxRegister("444444")
						.expLicense("2020-1-1")
						.telephone(genRamdomTelphone(null))
						.legalName("邬海艳")
						.identityType(1L)
						.legalIds(new IdCardGenerator().generate())
						.legalPhone(genRamdomTelphone(null))
						.accountNo("6228481000000051211")
						.parentBankName("农业银行")
						.bankCityNo("777777")
						.bankName("农业银行xxx支行")
						.unionBank("010300000000")
						.bankCityNo("")
						.province("上海")
						.city("上海")
						.build());
		logStack(result);
	}

	@Test
	public void genSignContractUrl() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		String result = yunstUserHandler.generateSignContractUrl(member.left.getBizUserId(),"/#/success");
//		String result = yunstUserHandler.generateSignContractUrl("WU10053","/#/success");
		logStack(result);
	}

	@Test
	public void applyBindBankCard() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		String realName = RandomUtils.getChineseName();
		Long identityType = 1L;
		String identityNo = new IdCardGenerator().generate();
		YunstPersonSetRealNameResult result = yunstUserHandler.personCertification(member.left.getBizUserId(), realName, identityType, identityNo);
		logStack(result);
//		String tel = RandomUtils.getTelephone();
		String tel = "18928847212";
		try {
			//		ApplyBindBankCardResp applyBindBankCardResp = yunstUserHandler.applyBindBankCard(walletId, source,
			//				RSAUtil.encrypt("4581240118157727"), realName, tel, identityType, identityNo,
			//				RSAUtil.encrypt("1119"), RSAUtil.encrypt("102"));
			ApplyBindBankCardResp applyBindBankCardResp = yunstUserHandler.applyBindBankCard(member.left.getBizUserId(),
					"4581240118157727", realName, tel, identityType, identityNo,
					null, null);
			logStack(applyBindBankCardResp);
		}catch (CommonGatewayException e){
			ApplyBindBankCardResp applyBindBankCardResp = new ApplyBindBankCardResp();
			String errMsg = e.getBankErrMsg();
			if (errMsg.indexOf("参数validate为空") > -1){
			}else {
			}
			logStack(result);
		}

	}

	@Test
	public void bindBankCard() throws Exception {
		Tuple<Long, Byte> bizUserTuple = genBizUser(WalletSource.USER.getValue().intValue());
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		Byte source = bizUserTuple.right;
		Long walletId = bizUserTuple.left;
		String realName = RandomUtils.getChineseName();
		Long identityType = 1L;
		String identityNo = new IdCardGenerator().generate();
		YunstPersonSetRealNameResult result = yunstUserHandler.personCertification(member.left.getBizUserId(), realName, identityType, identityNo);
		logStack(result);

//		String tel = RandomUtils.getTelephone();
		String tel = "18928847212";
		ApplyBindBankCardResp applyBindBankCardResp = yunstUserHandler.applyBindBankCard(member.left.getBizUserId(),
				"4581240118157727", realName, tel, identityType, identityNo,
				"1119", "102");
		assertNotNull(applyBindBankCardResp);
		assertNotNull(applyBindBankCardResp.getTranceNum());
		logStack(applyBindBankCardResp);

		String verifyCodePath = System.getProperty("user.dir") + "/src/test/verifyCode";
		System.out.println("请输入验证码到" + verifyCodePath + "文件");
		String oldVerificationCode = new String(FileUtil.read(verifyCodePath),"utf-8");
		String verificationCode = "";
		while (true){
			verificationCode = new String(FileUtil.read(verifyCodePath),"utf-8");
			if (verificationCode.equals(oldVerificationCode)){
				TimeUnit.SECONDS.sleep(5);
			}else {
				break;
			}
		}

		System.out.println("验证码为:" + verificationCode);
		YunstBindBankCardResult yunstBindBankCardResult = yunstUserHandler.bindBankCard(member.left.getBizUserId(),
				applyBindBankCardResp.getTranceNum(), applyBindBankCardResp.getTransDate(), tel, "1119", "102",
				verificationCode);
		logStack(yunstBindBankCardResult);
	}

	private String randomPersonCompany(Integer type) {
		if (type == null) {
			return new Random().nextInt(2) < 1 ? "U" : "M";
		} else {
			return type == 2 ? "U" : "M";
		}
	}

	private Tuple<Long, Byte> genBizUser(Integer type) {
		return new Tuple<>(randomWalletId(null), randomSource(type));
	}


	private Long randomWalletId(Long walletId){
		if (walletId == null){
			walletId = (long) new Random().nextInt(10000) + 1;
		}
		return walletId;
	}

	private Byte randomSource(Integer source){
		if (source == null){
			source = new Random().nextInt(3) + 1;
		}
		return source.byteValue();
	}

	private String genRamdomTelphone(String tel) {
		if (StringUtils.isBlank(tel)) {
			tel = RandomUtils.getTelephone();
		}
		return tel;
	}

}