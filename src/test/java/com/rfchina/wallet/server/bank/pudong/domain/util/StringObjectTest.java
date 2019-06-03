package com.rfchina.wallet.server.bank.pudong.domain.util;

import static org.junit.Assert.*;

import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery49Resp;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery49RespVo;
import org.junit.Test;

public class StringObjectTest {

	@Test
	public void parseStringObject() {
		String value = "2019051615288600|20190516152608|990B8950900000818|6225160293976253|浦发1339591801|0|||1.00||0||test||1|";
		EBankQuery49RespVo resp = StringObject.parseStringObject(value, EBankQuery49RespVo.class, "\\|");
		assertTrue(resp.getElecChequeNo().equals("2019051615288600"));
		assertTrue(resp.getStatus().equals("1"));
	}
}