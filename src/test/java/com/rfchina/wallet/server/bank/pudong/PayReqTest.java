package com.rfchina.wallet.server.bank.pudong;

import static com.rfchina.wallet.server.bank.pudong.TestingData.*;
import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.domain.request.PayReqBody.PayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PayRespBody;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import java.math.BigDecimal;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient.Builder;
import org.junit.Test;

@Slf4j
public class PayReqTest {

	@Test
	public void lanch() throws Exception {
		PayReq payB = PayReq.builder()
			.elecChequeNo(PACKET_ID)
			.acctNo(CMP_ACCT_ID)
			.acctName(CMP_ACCT_NAME)
			.payeeAcctNo(ACCT_B_ID)
			.payeeName(ACCT_B_NAME)
//			.payeeType(PayeeType.PUBLIC_ACCOUNT.getValue())
//			.payeeBankName("") // 跨行转帐时必须输入
			.amount(
				BigDecimal.ONE.divide(BigDecimal.TEN).setScale(2, BigDecimal.ROUND_DOWN).toString())
			.sysFlag(SysFlag.SELF.getValue())
			.remitLocation(RemitLocation.SELF.getValue())
			.note("测试")
			.build();

		PayReqBuilder req = PayReqBuilder
			.builder()
			.masterId(MASTER_ID)
			.packetId(PACKET_ID)
			.payList(Arrays.asList(payB))
			.build();
		PayRespBody resp = req.lanch(new Builder().build());

		log.info(JSON.toJSONString(resp));
		assertTrue(resp != null);
	}
}