package com.rfchina.wallet.server.bank.pudong.builder;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayQuery53ReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayQuery54ReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayQuery54RespBody;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import okhttp3.OkHttpClient;

/**
 * @author nzm
 */
@Builder
@Getter
public class PriPayQuery54Builder extends PpdbReqTpl implements GatewayLancher {

	private final String transCode = "AQ54";
	private String masterId;
	private String packetId;

	@ApiModelProperty(value = "交易客户号", required = true)
	private String transMasterID;

	@ApiModelProperty(value = "项目编号", required = true)
	private String projectNumber;

	@ApiModelProperty(value = "费项编码", required = false)
	private String costItemCode;

	@ApiModelProperty(value = "交易日期", required = true)
	private String transDate;

	@ApiModelProperty(value = "电子凭证号", required = false)
	private String elecChequeNo;

	@ApiModelProperty(value = "受理编号", required = false)
	private String handleSeqNo;


	@Override
	public PriPayQuery54RespBody lanch(String hostUrl, String signUrl, OkHttpClient client)
		throws Exception {
		return super.build(hostUrl, signUrl, client, PriPayQuery53ReqBody.class,
			PriPayQuery54RespBody.class);
	}

	@Override
	PriPayQuery54ReqBody buildReqBody() {

		return PriPayQuery54ReqBody.builder()
			.transMasterID(transMasterID)
			.projectNumber(projectNumber)
			.costItemCode(costItemCode)
			.transDate(transDate)
			.elecChequeNo(elecChequeNo)
			.handleSeqNo(handleSeqNo)
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
}
