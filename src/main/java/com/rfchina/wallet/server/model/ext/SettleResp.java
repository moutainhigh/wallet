package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SettleResp {

	@ApiModelProperty(value = "代收单")
	private WalletCollect collect;

	@ApiModelProperty(value = "分帐列表")
	private List<WalletClearing> clearings;

}