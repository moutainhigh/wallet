package com.rfchina.wallet.server.service.handler;

import static org.junit.Assert.*;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Handler8800Test extends SpringBaseTest {

	@Autowired
	private Handler8800 handler8800;


	@Test
	public void p0101Pay() throws Exception {
		WalletLog req1 = WalletLog.builder()
			.walletId(3L)
			.amount(1L)
			.elecChequeNo(
				IdGenerator.createBizId("Eno", 16, (orderId) -> true))
			.note("测试")
			.payPurpose("1")
			.build();
		WalletLog req2 = WalletLog.builder()
			.walletId(2L)
			.amount(1L)
			.elecChequeNo(
				IdGenerator.createBizId("Test", 16, (orderId) -> true))
			.note("测试")
			.payPurpose("1")
			.build();

		Tuple<GatewayMethod, PayInResp> tuple = handler8800.pay(Arrays.asList(req1, req2));
		assertNotNull(tuple);
		assertNotNull(tuple.right);
		assertNotNull(tuple.right.getAcceptNo());

	}

	@Test
	public void p0102UpdatePayStatus() {
		String acceptNo = "PT19YQ0000025178";
		List<WalletLog> walletLogs = handler8800.updatePayStatus(acceptNo, new Date());
		assertTrue(walletLogs.size() > 0);
	}
}