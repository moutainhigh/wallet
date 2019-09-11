package com.rfchina.wallet.server.api.impl;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.YunstApi;
import com.rfchina.wallet.server.bank.yunst.response.YunstCreateMemberResp;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class YunstImplTest extends SpringApiTest{
	@Autowired
	private YunstApi yunstApi;

	@Test
	public void createMember() throws Exception {
		String bizUserId = "Test"+ randomPersonCompany()+System.currentTimeMillis();
		Integer type = bizUserId.startsWith("TestC")?1:2;
		YunstCreateMemberResp result = yunstApi.createYunstMember(accessToken, bizUserId, type);
		logStack(result);
	}


	@Test
	public void sendSMSVerificationCode() throws Exception {
		String bizUserId = "TestU1568186437709";
		Integer type = bizUserId.startsWith("CTest")?1:2;
		Tuple<Boolean, String> tuple = yunstApi.requestSmsVerifyCode(accessToken, bizUserId, type, "18928847212", 9);
		logStack(tuple);
	}

	@Test
	public void bindPhone() throws Exception {
		String bizUserId = "TestU1568186437709";
		Integer type = bizUserId.startsWith("CTest")?1:2;
		Tuple<Boolean, String> tuple = yunstApi.bindPhone(accessToken, bizUserId, type, "18928847212", "11111");
		logStack(tuple);
	}


	@Test
	public void changeBindPhone() throws Exception {
		String bizUserId = "TestU1568186437709";
		Integer type = bizUserId.startsWith("CTest")?1:2;
		Tuple<Boolean, String> tuple = yunstApi.modifyPhone(accessToken, bizUserId, type,"18928847212", "13800138000", "11111");
		logStack(tuple);
	}
	private String randomPersonCompany(){
		return new Random().nextInt(2) < 1 ?"U":"C";
	}
}
