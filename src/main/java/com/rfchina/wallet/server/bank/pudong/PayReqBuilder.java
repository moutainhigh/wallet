package com.rfchina.wallet.server.bank.pudong;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PayReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.request.PayReqBody.Lists;
import com.rfchina.wallet.server.bank.pudong.domain.response.PayRespBody;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import okhttp3.OkHttpClient;

@Builder
public class PayReqBuilder extends PpdbReqTpl implements GatewayLancher<PayRespBody> {

	private final static String transCode = "8800";
	private String masterId;

	private String packetId;
	private String authMasterId;
	private String packageNo;
	private List<PayReqBody.PayReq> payList;

	@Override
	PayReqBody buildReqBody() {
		Optional<BigDecimal> total = payList.stream()
			.map(pay -> new BigDecimal(pay.getAmount()))
			.reduce(BigDecimal::add);
		return PayReqBody.builder()
			.authMasterID(authMasterId)
			.packageNo(packageNo)
			.totalNumber(String.valueOf(payList.size()))
			.totalAmount(total.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.DOWN).toString())
			.lists(Lists.builder().list(payList).build())
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
	public PayRespBody lanch(OkHttpClient client) throws Exception {
		return super.build(client, PayReqBody.class, PayRespBody.class);
	}
}
