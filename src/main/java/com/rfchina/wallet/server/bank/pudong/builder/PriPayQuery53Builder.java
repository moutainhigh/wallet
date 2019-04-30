package com.rfchina.wallet.server.bank.pudong.builder;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayQuery53ReqBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayQuery53RespBody;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Builder;
import okhttp3.OkHttpClient;

/**
 * @author nzm
 */
@Builder
public class PriPayQuery53Builder extends PpdbReqTpl implements GatewayLancher {

	private final static String transCode = "AQ53";
	private String masterId;
	private String packetId;

	@ApiModelProperty(value = "交易客户号", required = true)
	private String transMasterID;

	@ApiModelProperty(value = "项目编号", required = true)
	private String projectNumber;

	@ApiModelProperty(value = "受理编号", required = false)
	private String handleSeqNo;

	@ApiModelProperty(value = "起始日期", required = true)
	private String beginDate;

	@ApiModelProperty(value = "截止日期", required = true)
	private String endDate;

	@ApiModelProperty(value = "查询笔数,最高20笔", required = true)
	private String queryNumber;

	@ApiModelProperty(value = "起始笔数", required = true)
	private String beginNumber;

	@Override
	public PriPayQuery53RespBody lanch(OkHttpClient client) throws Exception {
		return super.build(client, PriPayQuery53ReqBody.class, PriPayQuery53RespBody.class);
	}

	@Override
	PriPayQuery53ReqBody buildReqBody() {

		return PriPayQuery53ReqBody.builder()
			.transMasterID(transMasterID)
			.projectNumber(projectNumber)
			.handleSeqNo(handleSeqNo)
			.beginDate(beginDate)
			.endDate(endDate)
			.queryNumber("20")
			.beginNumber("1")
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
