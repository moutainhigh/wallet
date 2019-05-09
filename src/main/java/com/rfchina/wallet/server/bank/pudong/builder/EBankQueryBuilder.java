package com.rfchina.wallet.server.bank.pudong.builder;

import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.EBankQueryReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQueryRespBody;
import io.swagger.annotations.ApiModelProperty;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Builder;
import okhttp3.OkHttpClient;

/**
 * @author nzm
 */
@Builder
public class EBankQueryBuilder extends PpdbReqTpl implements GatewayLancher<EBankQueryRespBody> {

	private final static String transCode = "DO48";
	private String masterId;

	@ApiModelProperty(value = "指定授权客户号", required = true)
	private String authMasterID;

	@ApiModelProperty(value = "查询起始日期 据当前日期1个月内", required = true)
	private String beginDate;

	@ApiModelProperty(value = "查询结束日期 不超过当前日", required = true)
	private String endDate;

	@ApiModelProperty(value = "网银受理编号", required = false)
	private String acceptNo;

	@Override
	EBankQueryReqBody buildReqBody() {
		return EBankQueryReqBody.builder()
			.authMasterID(authMasterID)
			.beginDate(beginDate)
			.endDate(endDate)
			.queryNumber("10")
			.beginNumber("1")
			.entJnlSeqNo(acceptNo)
			.build();
	}

	@Override
	RequestHeader buildReqestHeader() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return RequestHeader.builder()
			.transCode(transCode)
			.signFlag(SIGN_FLAG)
			.packetID(String.valueOf(System.currentTimeMillis()))
			.masterID(masterId)
			.timeStamp(format.format(new Date()))
			.build();
	}

	@Override
	public EBankQueryRespBody lanch(String hostUrl,String signUrl,OkHttpClient client) throws Exception {
		return super.build(hostUrl,signUrl,client, EBankQueryReqBody.class, EBankQueryRespBody.class);
	}
}
