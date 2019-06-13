package com.rfchina.wallet.server.bank.pudong.builder;

import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayQueryReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import okhttp3.OkHttpClient;

/**
 * @author nzm
 */
@Builder
@Getter
public class PubPayQueryBuilder extends PpdbReqTpl implements GatewayLancher<PubPayQueryRespBody> {

	private final String transCode = "8804";
	@NotNull
	private String masterId;
	private String packetId;
	@NotNull
	private String acctNo;
	@NotNull
	private String beginDate;
	@NotNull
	private String endDate;
	private String acceptNo;
	private String elecChequeNo;

	@Override
	PubPayQueryReqBody buildReqBody() {
		return PubPayQueryReqBody.builder()
			.acctNo(acctNo)
			.beginDate(beginDate)
			.endDate(endDate)
			.beginNumber("1")
			.queryNumber("30")
			.singleOrBatchFlag("1")
			.acceptNo(acceptNo)
			.elecChequeNo(elecChequeNo)
			.build();
	}

	@Override
	RequestHeader buildReqestHeader() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return RequestHeader.builder()
			.transCode(transCode)
			.signFlag(SIGN_FLAG)
			.packetID(packetId)
			.masterID(masterId)
			.timeStamp(format.format(new Date()))
			.build();
	}

	@Override
	public PubPayQueryRespBody lanch(String hostUrl, String signUrl,OkHttpClient client) throws Exception {
		return super.build(hostUrl, signUrl,client, PubPayQueryReqBody.class, PubPayQueryRespBody.class);
	}

}
