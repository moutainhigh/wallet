package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.response.GetOrderDetailResp;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletWithdrawExtDao;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
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

	@Autowired
	private WalletWithdrawExtDao walletWithdrawDao;


	@Test
	public void passwordConfirm() {

		String jumpUrl = "http://www.baidu.com";
		String customerIp = "8.8.8.8";

		WalletOrder withdrawOrder = walletOrderDao.selectByOrderNo("WD20191107572072205");
		WalletTunnel payer = walletTunnelDao
			.selectByWalletId(withdrawOrder.getWalletId(), withdrawOrder.getTunnelType());
		String signedParams = yunstBizHandler
			.pwdGwConfirm(withdrawOrder, payer, jumpUrl, customerIp);
		signedParams = configService.getYunstPwdConfirmUrl() + "?" + signedParams;

		log.info(signedParams);
	}


	@Test
	public void autoWithdraw() {
		WalletOrder order = walletOrderDao.selectByOrderNo("WD20191107572072205");
		WalletWithdraw withdraw = walletWithdrawDao.selectByOrderId(order.getId());
		WalletTunnel payer = walletTunnelDao
			.selectByWalletId(order.getWalletId(), order.getTunnelType());

		WithdrawResp resp = yunstBizHandler.withdraw(order, withdraw, payer);
		log.info("{}", resp);

	}

	@Test
	public void cardBin() {
		String result = yunstBizHandler.cardBin("6214850201481956");
		log.info("cardBin = {}", result);
	}

	@Test
	public void queryOrderDetail() {
		GetOrderDetailResp resp = yunstBizHandler
			.queryOrderDetail("TWC2020041738884328");
		log.info("{}", resp);
	}
}