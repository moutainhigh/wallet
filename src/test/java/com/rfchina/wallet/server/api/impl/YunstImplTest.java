package com.rfchina.wallet.server.api.impl;

import com.rfchina.wallet.server.SpringApiTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class YunstImplTest extends SpringApiTest {




	private String randomPersonCompany() {
		return new Random().nextInt(2) < 1 ? "U" : "M";
	}
}
