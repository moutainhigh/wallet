package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.server.msic.EnumYunst.YunstMethodName;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class ChargingVo {

	@ApiModelProperty(name = "order_no", value = "订单号")
	private String orderNo;

	@ApiModelProperty(name = "biz_no", value = "业务凭证号")
	private String bizNo;

	@ApiModelProperty(name = "type", value = "类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,")
	private Byte type;

	@ApiModelProperty(name = "tunnel_fee", value = "通道手续费")
	private Long tunnelFee;

	@ApiModelProperty(name = "tunnel_succ_time", value = "通道成功时间")
	private Date tunnelSuccTime;

	@ApiModelProperty(name="charging_type", value = "计费方式，1按次收费，2按比率收费")
	private Byte chargingType;

	@ApiModelProperty(name="charging_value", value = "计费单价，计费比例或金额")
	private BigDecimal chargingValue;

	public String getMethodName() {
		if (OrderType.RECHARGE.getValue().equals(type)) {
			return YunstMethodName.RECHARGE.getValue();
		} else if (OrderType.WITHDRAWAL.getValue().equals(type)) {
			return YunstMethodName.WITHDRAW.getValue();
		} else if (OrderType.COLLECT.getValue().equals(type)) {
			return YunstMethodName.COLLECT.getValue();
		} else if (OrderType.AGENT_PAY.getValue().equals(type)) {
			return YunstMethodName.AGENT_PAY.getValue();
		} else if (OrderType.REFUND.getValue().equals(type)) {
			return YunstMethodName.REFUND.getValue();
		} else if (OrderType.CONSUME.getValue().equals(type)) {
			return YunstMethodName.CONSUME.getValue();
		} else if (OrderType.DEDUCTION.getValue().equals(type)) {
			return YunstMethodName.DEDUCTION.getValue();
		}
		return null;
	}

	public Long getTunnelCount() {
		if (OrderType.WITHDRAWAL.getValue().equals(type)) {
			return 1L;
		}
		return null;
	}

}
