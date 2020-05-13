package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.model.StatCharging;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class StatChargingVo extends StatCharging {

	@ApiModelProperty(name = "charging_type", value = "计费方式，1按次收费，2按比率收费")
	private Byte chargingType;

	@ApiModelProperty(name = "charging_value", value = "计费单价，计费比例或金额")
	private BigDecimal chargingValue;
}
