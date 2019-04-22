package com.rfchina.wallet.server.bank.pudong.Builder;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayQueryReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import okhttp3.OkHttpClient;

@Builder
public class PubPayQueryBuilder extends PpdbReqTpl implements GatewayLancher {

	private final static String transCode = "8804";
	@NotNull
	private String masterId;
	@NotNull
	private String acctNo;
	@NotNull
	private String beginDate;
	@NotNull
	private String endDate;
	@NotNull
	private String queryNumber;
	@NotNull
	private String beginNumber;
	private String acceptNo;
	private String elecChequeNo;

	@Override
	PubPayQueryReqBody buildReqBody() {
		return PubPayQueryReqBody.builder()
			.acctNo(acctNo)
			.beginDate(beginDate)
			.endDate(endDate)
			.beginNumber(beginNumber)
			.queryNumber(queryNumber)
			.singleOrBatchFlag("1")
			.acceptNo(acceptNo)
			.elecChequeNo(elecChequeNo)
			.build();
	}

	@Override
	RequestHeader buildReqestHeader() {
		return RequestHeader.builder()
			.transCode(transCode)
			.signFlag(SIGN_FLAG)
			.packetID(String.valueOf(System.currentTimeMillis()))
			.masterID(masterId)
			.timeStamp(DateUtil.formatDate(new Date()))
			.build();
	}

	@Override
	public PubPayQueryRespBody lanch(OkHttpClient client) throws Exception {
		return super.build(client, PubPayQueryReqBody.class, PubPayQueryRespBody.class);
	}

}
