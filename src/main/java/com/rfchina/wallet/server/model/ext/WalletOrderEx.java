package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.WalletOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "")

public class WalletOrderEx extends WalletOrder {

	@ApiModelProperty(name="pay_type", value = "pos机实际支付类型：61-建单; 62-微信支付; 63-手机QQ支付; 64-支付宝; 65-银联")
	private Integer payType;

}
