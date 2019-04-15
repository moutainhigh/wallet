package com.rfchina.wallet.server.bank.pudong;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PayResultReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PayRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PayResultRespBody;
import java.util.Date;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import okhttp3.OkHttpClient;

@Builder
public class PayResultReqBuilder extends PpdbReqTpl implements GatewayLancher {

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
	private Integer queryNumber;
	@NotNull
	private Integer beginNumber;

	@Override
	PayResultReqBody buildReqBody() {
		return PayResultReqBody.builder()
			.acctNo(acctNo)
			.beginDate(beginDate)
			.endDate(endDate)
			.beginNumber(String.valueOf(Optional.ofNullable(beginNumber).orElse(1)))
			.queryNumber(String.valueOf(Optional.ofNullable(queryNumber).orElse(1)))
			.singleOrBatchFlag("1")
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
	public PayResultRespBody lanch(OkHttpClient client) throws Exception {
		return super.build(client, PayResultReqBody.class, PayResultRespBody.class);
	}

}
