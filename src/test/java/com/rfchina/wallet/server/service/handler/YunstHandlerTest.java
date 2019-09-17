package com.rfchina.wallet.server.service.handler;

import com.allinpay.yunst.sdk.util.RSAUtil;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstPersonSetRealNameResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSetCompanyInfoResult;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.service.handler.pudong.Handler8800;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import com.rfchina.wallet.server.utils.IdCardGenerator;
import com.rfchina.wallet.server.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class YunstHandlerTest extends SpringBaseTest {

	@Autowired
	private YunstUserHandler yunstUserHandler;


	@Test
	public void createMember() throws Exception {
		Tuple<String, Integer> bizUserTuple = genBizUser(null);
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);

		assertNotNull(member);
		logStack(member);

	}

	@Test
	public void sendSmsVerificationCode() throws Exception {
		Tuple<String, Integer> bizUserTuple = genBizUser(2);
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		int type = bizUserTuple.right;
		String bizUserId = bizUserTuple.left;
		String tel = genRamdomTelphone(null);
		boolean result = yunstUserHandler.sendVerificationCode(bizUserId, type, tel, 9);
		logStack(result);
	}

	@Test
	public void bindPhone() throws Exception {
		Tuple<String, Integer> bizUserTuple = genBizUser(2);
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		int type = bizUserTuple.right;
		String bizUserId = bizUserTuple.left;
		String tel = genRamdomTelphone(null);
		boolean result = yunstUserHandler.sendVerificationCode(bizUserId, type, tel, 9);
		assertTrue(result);
		logStack(result);
		result = yunstUserHandler.bindPhone(bizUserId, type, tel, "11111");
		assertTrue(result);
		logStack(result);
	}

	@Test
	public void modifyPhone() throws Exception {
		Tuple<String, Integer> bizUserTuple = genBizUser(2);
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		int type = bizUserTuple.right;
		String bizUserId = bizUserTuple.left;
		String tel = genRamdomTelphone(null);
		boolean result = yunstUserHandler.sendVerificationCode(bizUserId, type, tel, 9);
		assertTrue(result);
		logStack(result);
		result = yunstUserHandler.bindPhone(bizUserId, type, tel, "11111");
		assertTrue(result);
		logStack(result);
		String newTel = genRamdomTelphone(null);
		result = yunstUserHandler.modifyPhone(bizUserId, type, tel, newTel, "11111");
		logStack(result);
		assertTrue(result);
	}

	@Test
	public void getMemberInfo() throws Exception {
		int type = "M".equals(randomPersonCompany(null))?1:2;
		String bizUserId = "Test" + randomPersonCompany(null) + System.currentTimeMillis();
		Object result = yunstUserHandler.getMemberInfo(bizUserId, type);
		logStack(result);
	}

	@Test
	public void getPersonCertification() throws Exception {
		Tuple<String, Integer> bizUserTuple = genBizUser(2);
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		int type = bizUserTuple.right;
		String bizUserId = bizUserTuple.left;
		String realName = RandomUtils.getChineseName();
		Long identityType = 1L;
		String identityNo = RSAUtil.encrypt(new IdCardGenerator().generate());
		boolean result = yunstUserHandler.personCertification(bizUserId, type, realName,identityType,identityNo);
		logStack(result);
	}

	@Test
	public void setCompanyInfo() throws Exception {
		Tuple<String, Integer> bizUserTuple = genBizUser(1);
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		int type = bizUserTuple.right;
		String bizUserId = bizUserTuple.left;
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
		YunstSetCompanyInfoResult result = yunstUserHandler.setCompanyInfo(bizUserId, type, true,
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
						.legalIds(RSAUtil.encrypt(new IdCardGenerator().generate()))
						.legalPhone(genRamdomTelphone(null))
						.accountNo(RSAUtil.encrypt("6228481000000051211"))
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
		Tuple<String, Integer> bizUserTuple = genBizUser(2);
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserTuple.left, bizUserTuple.right);
		assertNotNull(member);
		logStack(member);
		int type = bizUserTuple.right;
		String bizUserId = bizUserTuple.left;
		String result = yunstUserHandler.generateSignContractUrl(bizUserId, type);
		logStack(result);
	}


	private String randomPersonCompany(Integer type) {
		if (type == null){
			return new Random().nextInt(2) < 1 ? "U" : "M";
		}else {
			return type == 2 ? "U" : "M";
		}
	}

	private Tuple<String,Integer> genBizUser(Integer type){
		String mark = randomPersonCompany(type);
		if (type == null){
			type = "M".equals(mark)?1:2;
		}
		String bizUserId = "Test" + mark + System.currentTimeMillis();
		return new Tuple<>(bizUserId,type);
	}


	private String genRamdomTelphone(String tel){
		if (StringUtils.isBlank(tel)){
			tel = RandomUtils.getTelephone();
		}
		return tel;
	}

}