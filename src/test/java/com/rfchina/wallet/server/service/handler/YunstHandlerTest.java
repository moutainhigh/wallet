package com.rfchina.wallet.server.service.handler;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.service.handler.pudong.Handler8800;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
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
		int type = "M".equals(randomPersonCompany())?1:2;
		String bizUserId = "Test" + randomPersonCompany() + System.currentTimeMillis();
		YunstCreateMemberResult member = yunstUserHandler.createMember(bizUserId, type);

		assertNotNull(member);
		logStack(member);

	}

	@Test
	public void sendSmsVerificationCode() throws Exception {
		int type = "M".equals(randomPersonCompany())?1:2;
		String bizUserId = "Test" + randomPersonCompany() + System.currentTimeMillis();
		boolean result = yunstUserHandler.sendVerificationCode(bizUserId, type, "13800138000", 9);
		logStack(result);
	}

	@Test
	public void bindPhone() throws Exception {
		int type = "M".equals(randomPersonCompany())?1:2;
		String bizUserId = "Test" + randomPersonCompany() + System.currentTimeMillis();
		boolean result = yunstUserHandler.bindPhone(bizUserId, type, "13800138000", "11111");
		logStack(result);
	}

	@Test
	public void modifyPhone() throws Exception {
		int type = "M".equals(randomPersonCompany())?1:2;
		String bizUserId = "Test" + randomPersonCompany() + System.currentTimeMillis();
		boolean result = yunstUserHandler.modifyPhone(bizUserId, type, "13800138000", "13800138001", "11111");
		logStack(result);
	}

	@Test
	public void getMemberInfo() throws Exception {
		int type = "M".equals(randomPersonCompany())?1:2;
		String bizUserId = "Test" + randomPersonCompany() + System.currentTimeMillis();
		Object result = yunstUserHandler.getMemberInfo(bizUserId, type);
		logStack(result);
	}



	private String randomPersonCompany() {
		return new Random().nextInt(2) < 1 ? "U" : "M";
	}
}