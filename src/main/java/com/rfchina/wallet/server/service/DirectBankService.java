package com.rfchina.wallet.server.service;

import com.rfchina.wallet.server.bank.pudong.PayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.request.PayReqBody.PayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PayRespBody;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class DirectBankService {

	private static final String MASTER_ID = "2000040752";
	private static final String PACKET_ID = String.valueOf(System.currentTimeMillis());

	public PayRespBody toPay(List<PayReq> payReqs) {
		PayReqBuilder req = PayReqBuilder
			.builder()
			.masterId(MASTER_ID)
			.packetId(PACKET_ID)
			.payList(payReqs)
			.build();

		try {
			PayRespBody resp = req.lanch(new Builder().build());

			return resp;
		}catch (Exception e){
			log.error("支付错误",e);
			throw new RuntimeException(e);
		}
	}
}
