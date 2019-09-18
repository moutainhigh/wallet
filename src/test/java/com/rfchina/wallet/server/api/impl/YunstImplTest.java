package com.rfchina.wallet.server.api.impl;

import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.YunstApi;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class YunstImplTest extends SpringApiTest {
	@Autowired
	private YunstApi yunstApi;



	private String randomPersonCompany() {
		return new Random().nextInt(2) < 1 ? "U" : "M";
	}
}
