package com.rfchina.wallet.server.bank.pudong.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class PubPayQueryReqBody {

	@ApiModelProperty(value = "电子凭证号", required = false)
	private String elecChequeNo;

	@ApiModelProperty(value = "账号", required = true)
	private String acctNo;

	@ApiModelProperty(value = "开始日期", required = true)
	private String beginDate;

	@ApiModelProperty(value = "结束日期", required = true)
	private String endDate;

	@ApiModelProperty(value = "受理编号", required = false)
	private String acceptNo;

	@ApiModelProperty(value = "序号", required = false)
	private String serialNo;

	@ApiModelProperty(value = "查询的笔数", required = true)
	private String queryNumber;

	@ApiModelProperty(value = "查询的起始笔数", required = true)
	private String beginNumber;

	@ApiModelProperty(value = "批量单笔标志 0：表示单笔 1：表示批量", required = false)
	private String singleOrBatchFlag;

}
