package com.rfchina.wallet.server.bank.pudong;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReqBody.Lists;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import okhttp3.OkHttpClient;

@Builder
public class PubPayReqBuilder extends PpdbReqTpl implements GatewayLancher<PubPayRespBody> {

	private final static String transCode = "8800";
	private String masterId;

	private String packetId;
	private String authMasterId;
	private String packageNo;
	private List<PubPayReq> payList;

	@Override
	PubPayReqBody buildReqBody() {
		Optional<BigDecimal> total = payList.stream()
			.map(pay -> new BigDecimal(pay.getAmount()))
			.reduce(BigDecimal::add);
		return PubPayReqBody.builder()
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
			.packetID(packetId)
			.masterID(masterId)
			.timeStamp(DateUtil.formatDate(new Date()))
			.build();
	}

	@Override
	public PubPayRespBody lanch(OkHttpClient client) throws Exception {
		return super.build(client, PubPayReqBody.class, PubPayRespBody.class);
	}
}
