package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollectSyncResp {
	@ApiModelProperty(value = "工单Id")
	private Long applyId;

	@ApiModelProperty(value = "代收单Id")
	private List<CollectVo> collects;
}