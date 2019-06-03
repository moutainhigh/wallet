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
public class EBankQuery49ReqBody {

	@ApiModelProperty(value = "指定授权客户号", required = true)
	private String authMasterID;

	@ApiModelProperty(value = "网银受理编号", required = true)
	private String entJnlSeqNo;

	@ApiModelProperty(value = "查询笔数 最大10", required = true)
	private String queryNumber;

	@ApiModelProperty(value = "查询的起始笔数 最小1", required = true)
	private String beginNumber;
}
