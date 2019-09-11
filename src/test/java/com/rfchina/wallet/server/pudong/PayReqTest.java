package com.rfchina.wallet.server.pudong;

import static com.rfchina.wallet.server.pudong.TestingData.*;
import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import java.math.BigDecimal;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient.Builder;
import org.junit.Test;

@Slf4j
public class PayReqTest {

	private String signUrl = "http://192.168.197.217:5666";

	private String hostUrl = "http://192.168.197.217:5777";

	@Test
	public void lanch() throws Exception {
		PubPayReq payB = PubPayReq.builder()
			.elecChequeNo(PACKET_ID)
			.acctNo(CMP_ACCT_ID)
			.acctName(CMP_ACCT_NAME)
			.payeeAcctNo(ACCT_B_ID)
			.payeeName(ACCT_B_NAME)
//			.payeeType(PayeeType.PUBLIC_ACCOUNT.getValue())
//			.payeeBankName("") // 跨行转帐时必须输入
			.amount(BigDecimal.ONE.divide(BigDecimal.TEN)
				.setScale(2, BigDecimal.ROUND_DOWN).toString())
			.sysFlag(SysFlag.SELF.getValue())
			.remitLocation(RemitLocation.SELF.getValue())
			.note("测试")
			.build();

		PubPayReqBuilder req = PubPayReqBuilder
			.builder()
			.masterId(MASTER_ID)
			.packetId(PACKET_ID)
			.payList(Arrays.asList(payB))
			.build();
		PubPayRespBody resp = req.lanch(hostUrl, signUrl, new Builder().build());

		log.info(JSON.toJSONString(resp));
		assertTrue(resp != null);


	}
}