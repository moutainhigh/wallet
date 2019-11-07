package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class YunstBizHandlerTest extends SpringBaseTest {

	@Autowired
	private YunstBizHandler yunstBizHandler;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;


	@Test
	public void passwordConfirm() {

		String jumpUrl = "http://www.baidu.com";
		String customerIp = "8.8.8.8";

		WalletOrder withdrawOrder = walletOrderDao.selectByOrderNo("WD20191107572072205");
		WalletTunnel payer = walletTunnelDao
			.selectByWalletId(withdrawOrder.getWalletId(), withdrawOrder.getTunnelType());
		String signedParams = yunstBizHandler
			.passwordConfirm(withdrawOrder, payer, jumpUrl, customerIp);
		signedParams = configService.getYunstPwdConfirmUrl() + "?" + signedParams;

		log.info(signedParams);
	}
}