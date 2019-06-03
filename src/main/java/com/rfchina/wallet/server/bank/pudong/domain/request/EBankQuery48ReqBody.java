package com.rfchina.wallet.server.bank.pudong.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class EBankQuery48ReqBody {

	@ApiModelProperty(value = "指定授权客户号", required = true)
	private String authMasterID;

	@ApiModelProperty(value = "查询起始日期 据当前日期1个月内", required = true)
	private String beginDate;

	@ApiModelProperty(value = "查询结束日期 不超过当前日", required = true)
	private String endDate;

	@ApiModelProperty(value = "查询笔数 最大10", required = true)
	private String queryNumber;

	@ApiModelProperty(value = "查询的起始笔数 最小1", required = true)
	private String beginNumber;

	@ApiModelProperty(value = "网银受理编号", required = false)
	private String entJnlSeqNo;

	@ApiModelProperty(value = "包号", required = false)
	private String packageNo;
}
