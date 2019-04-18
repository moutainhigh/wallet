package com.rfchina.wallet.server.bank.pudong;

import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayReqBody;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import okhttp3.OkHttpClient;

public class PriPayReqBuilder extends PpdbReqTpl implements GatewayLancher {

	private final static String transCode = "AQ52";
	private String masterId;
	private String packetId;

	@ApiModelProperty(value = "交易客户号", required = true)
	private String transMasterID;

	@ApiModelProperty(value = "项目编号", required = true)
	private String projectNumber;

	@ApiModelProperty(value = "项目名称", required = true)
	private String projectName;

	@ApiModelProperty(value = "费项编码", required = true)
	private String costItemCode;

	@ApiModelProperty(value = "交易类型.1:代收 2:代付", required = true)
	private String transType;

	@ApiModelProperty(value = "电子凭证号", required = true)
	private String elecChequeNo;

	@ApiModelProperty(value = "是否指定唯一渠道.0:不指定渠道 1:指定渠道", required = false)
	private String onlyChannelFlag;

	@ApiModelProperty(value = "第三渠道ID", required = false)
	private String thirdChannelID;

	@ApiModelProperty(value = "总笔数", required = true)
	private String totalNumber;

	@ApiModelProperty(value = "总金额", required = true)
	private String totalAmount;

	@ApiModelProperty(value = "摘要", required = false)
	private String note;

	@ApiModelProperty(value = "用途或附言", required = false)
	private String purpose;

	@ApiModelProperty(value = "批次号", required = true)
	private String batchNo;

	@ApiModelProperty(value = "支付明细", required = true)
	private List<PriPayReq> payReqList;


	@Override
	public Object lanch(OkHttpClient client) throws Exception {
		return null;
	}

	@Override
	PriPayReqBody buildReqBody() {
		BigDecimal amount = payReqList.stream()
			.map(payReq -> new BigDecimal(payReq.getAmount()))
			.reduce(BigDecimal::add).get();

		return PriPayReqBody.builder()
			.transMasterID(transMasterID)
			.projectNumber(projectNumber)
			.projectName(projectName)
			.costItemCode(costItemCode)
			.transType(transType)
			.elecChequeNo(elecChequeNo)
			.totalNumber(String.valueOf(payReqList.size()))
			.totalAmount(amount.setScale(2, BigDecimal.ROUND_DOWN).toString())
			.batchNo(batchNo)
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
