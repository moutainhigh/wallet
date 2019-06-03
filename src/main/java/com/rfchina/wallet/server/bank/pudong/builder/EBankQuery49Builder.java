package com.rfchina.wallet.server.bank.pudong.builder;

import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.EBankQuery48ReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.request.EBankQuery49ReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery48RespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery49RespBody;
import io.swagger.annotations.ApiModelProperty;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import okhttp3.OkHttpClient;

/**
 * @author nzm
 */
@Builder
@Getter
public class EBankQuery49Builder extends PpdbReqTpl implements GatewayLancher<EBankQuery49RespBody> {

	private final String transCode = "DO49";
	private String masterId;

	@ApiModelProperty(value = "指定授权客户号", required = true)
	private String authMasterID;

	@ApiModelProperty(value = "网银受理编号", required = true)
	private String entJnlSeqNo;

	@Override
	EBankQuery49ReqBody buildReqBody() {
		return EBankQuery49ReqBody.builder()
			.authMasterID(authMasterID)
			.queryNumber("10")
			.beginNumber("1")
			.entJnlSeqNo(entJnlSeqNo)
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
	public EBankQuery49RespBody lanch(String hostUrl,String signUrl,OkHttpClient client) throws Exception {
		return super.build(hostUrl,signUrl,client, EBankQuery49ReqBody.class, EBankQuery49RespBody.class);
	}
}
