package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostSeqNo {

	@ApiModelProperty(name="host_accept_no", value = "核心受理编号")
	private String hostAcceptNo;

	@ApiModelProperty(name="audit_time", value = "网银授权日期")
	private Date auditTime;
}
