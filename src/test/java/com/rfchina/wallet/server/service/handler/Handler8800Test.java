package com.rfchina.wallet.server.service.handler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.mapper.ext.WalletFinanceExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.service.handler.pudong.Handler8800;
import java.util.Arrays;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Handler8800Test extends SpringBaseTest {

	@Autowired
	private Handler8800 handler8800;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private WalletFinanceExtDao walletFinanceDao;


	@Test
	public void p0101Pay() throws Exception {
		WalletOrder walletOrder = walletOrderDao.selectByPrimaryKey(202L);
		WalletFinance walletFinance = walletFinanceDao.selectByOrderId(walletOrder.getId());

		Tuple<GatewayMethod, PayTuple> tuple = handler8800
			.finance(Arrays.asList(new Tuple<>(walletOrder, walletFinance)));
		assertNotNull(tuple);
		assertNotNull(tuple.right.getAcceptNo());

	}

	@Test
	public void p0102UpdatePayStatus() {
//		String batchNo = "SLW20190902664975993";
//		List<Tuple<WalletApply, GatewayTrans>> walletLogs = handler8800.updatePayStatus(batchNo);
//		assertTrue(walletLogs != null);
	}

	@Test
	public void extractErrCode() {
		String note = "推荐成交金域中央C-1栋1148|&lt;错误原因:EGG0042 处理失败[EGG0044][帐户[990B8950900000818   ]余额不足&gt;";
		IGatewayError err = handler8800.extractErrCode(note);
		assertTrue(err.getErrCode().equals("EGG0042"));
	}

	@Test
	public void genPkgId() {
		String pkgId = handler8800.genPkgId();
		logStack(pkgId);
		assertTrue(pkgId != null);
	}
}