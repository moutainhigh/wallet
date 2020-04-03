package com.rfchina.wallet.server.service;

import com.rfchina.app.model.App;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletOrderDao;
import com.rfchina.wallet.domain.model.WalletConfig;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.mapper.ext.WalletConfigExtDao;
import com.rfchina.wallet.server.model.ext.WithdrawResp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ScheduleServiceTest extends SpringBaseTest {

	@Autowired
	@InjectMocks
	private ScheduleService scheduleService;


	@MockBean
	private SeniorPayService seniorPayService;

	@Autowired
	private WalletConfigExtDao walletConfigDao;

	@Autowired
	private SessionThreadLocal sessionThreadLocal;

	@Autowired
	private WalletOrderDao walletOrderDao;


	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		App app = new App() {
			@Override
			public Long getId() {
				return 0L;
			}
		};
		sessionThreadLocal.get().put("APP", app);
		WalletOrder walletOrder = walletOrderDao.selectByPrimaryKey(1424L);
		Mockito.doReturn(BeanUtil.newInstance(walletOrder, WithdrawResp.class))
			.when(seniorPayService)
			.doWithdraw(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
	}

	@Test
	public void quartzUpdateJunior() {
		scheduleService.quartzUpdateJunior(100);
	}

	@Test
	public void quartzUpdateSenior() {
		scheduleService.quartzUpdateSenior(100);
	}

	@Test
	public void quartzPay() {
		scheduleService.doTunnelFinanceJob("");
	}

	@Test
	public void sendEmail() {
		scheduleService.sendEmail("测试", "测试邮件", "niezengming@rfchina.com,xiejueheng@rfchina.com");
	}

	@Test
	public void doWithdraw() {
		WalletConfig config = walletConfigDao.selectByPrimaryKey(1L);
		scheduleService.doWithdraw(config);
	}
}