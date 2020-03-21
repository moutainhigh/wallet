package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.wallet.domain.mapper.ext.WalletWithdrawDao;
import com.rfchina.wallet.domain.misc.EnumDef.BizValidateType;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
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
	private WalletWithdrawDao walletWithdrawDao;


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
}