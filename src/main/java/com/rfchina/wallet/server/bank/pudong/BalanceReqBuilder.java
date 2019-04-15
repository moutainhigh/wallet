package com.rfchina.wallet.server.bank.pudong;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.BalanceReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.request.BalanceReqBody.AcctNo;
import com.rfchina.wallet.server.bank.pudong.domain.response.BalanceRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.BalanceRespBody.Balance;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import lombok.Builder;
import okhttp3.OkHttpClient;

@Builder
public class BalanceReqBuilder extends PpdbReqTpl implements GatewayLancher<Balance> {

	private final static String transCode = "4402";
	private String masterId;
	private String packetId;
	private String acctNo;

	@Override
	protected BalanceReqBody buildReqBody() {
		return BalanceReqBody.builder()
			.lists(BalanceReqBody.Lists.builder()
				.list(Collections.singletonList(
					AcctNo.builder().acctNo(acctNo).build())
				)
				.build()
			)
			.build();
	}

	@Override
	protected RequestHeader buildReqestHeader() {
		return RequestHeader.builder()
			.transCode(transCode)
			.packetID(packetId)
			.masterID(masterId)
			.signFlag(SIGN_FLAG)
			.timeStamp(DateUtil.formatDate(new Date()))
			.build();
	}

	@Override
	public Balance lanch(OkHttpClient client) throws Exception {
		BalanceRespBody respBody = super.build(client, BalanceReqBody.class, BalanceRespBody.class);

		Optional<Balance> first = respBody.getLists().getList().stream()
			.filter(o -> acctNo.equals(o.getAcctNo()))
			.findFirst();

		return first.isPresent() ? first.get() : null;
	}
}
