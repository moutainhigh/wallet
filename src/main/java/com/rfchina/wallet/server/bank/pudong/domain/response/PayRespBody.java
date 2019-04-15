package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量支付结果 （8800）
 *
 * <body>
 * <acceptNo >20</ acceptNo >
 * < seqNo >209999</ seqNo >
 * <successCount >1</ successCount >
 * <failCount>0</ failCount>
 * </body>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel
public class PayRespBody {

	@ApiModelProperty(value = "受理编号")
	private String acceptNo;

	@ApiModelProperty(value = "柜员流水号")
	private String seqNo;

	@ApiModelProperty(value = "成功笔数")
	private String successCount;

	@ApiModelProperty(value = "失败笔数")
	private String failCount;
}
