package com.rfchina.wallet.server.bank.pudong.domain.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class EBankQuery49RespBody {

	@ApiModelProperty(value = "总笔数")
	private String totalCount;

	@ApiModelProperty(value = "实际返回笔数")
	private String pageCount;

	@ApiModelProperty(value = "包号")
	private String packageNo;

	private Lists lists;

	@Data
	public static class Lists {

		private List<EBankQuery49Resp> list;
	}
}
