package com.rfchina.wallet.server.service.handler;

import static org.junit.Assert.*;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.service.handler.pudong.Handler8800;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Handler8800Test extends SpringBaseTest {

	@Autowired
	private Handler8800 handler8800;

	@Autowired
	private WalletApplyExtDao walletApplyDao;


	@Test
	public void p0101Pay() throws Exception {
		WalletApply walletApply = walletApplyDao.selectByPrimaryKey(202L);

		Tuple<GatewayMethod, PayTuple> tuple = handler8800.pay(Arrays.asList(walletApply));
		assertNotNull(tuple);
		assertNotNull(tuple.right);
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