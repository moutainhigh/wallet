package com.rfchina.wallet.server.model.ext;

import com.rfchina.platform.biztool.excel.PoiColumn;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import lombok.Data;

@Data
public class WalletOrderExcelVo {

	@PoiColumn(idx = 0, title = "时间")
	@ApiModelProperty(name = "start_time", value = "开始时间")
	private Date startTime;

	@PoiColumn(idx = 1, title = "明细单号")
	@ApiModelProperty(name = "tunnel_order_no", value = "渠道订单号")
	private String tunnelOrderNo;

	@PoiColumn(idx = 2, title = "类型")
	private String getOrderType() {
		switch (type) {
			case 1:
				return "财务结算";
			case 2:
				return "充值";
			case 3:
				return "提现";
			case 4:
				return "支付";
			case 5:
				return "收入";
			case 6:
				return "退款";
			case 7:
				return "支付";
			case 8:
				if (walletId.longValue() == ownerId) {
					return "扣款";
				}
				return "收入";
			default:
				return "";
		}
	}

	@PoiColumn(idx = 3, title = "状态")
	private String getOrderStatus() {
		switch (status) {
			case 2:
				return "进行中";
			case 3:
				return "交易成功";
			case 4:
				return "交易失败";
			case 5:
				return "交易关闭";
			default:
				return "";
		}
	}

	@PoiColumn(idx = 4, title = "金额")
	private String getOrderAmount() {
		String prefix;
		if (type.byteValue() == OrderType.WITHDRAWAL.getValue().byteValue()
			|| type.byteValue() == OrderType.COLLECT.getValue().byteValue()
			|| type.byteValue() == OrderType.CONSUME.getValue().byteValue()
			|| (type.byteValue() == OrderType.DEDUCTION.getValue().byteValue()
			&& walletId.longValue() == ownerId.longValue())) {
			prefix = "-";
		} else {
			prefix = "+";
		}
		BigDecimal orderAmount = BigDecimal.valueOf(amount)
			.divide(BigDecimal.valueOf(100))
			.setScale(2, RoundingMode.DOWN);
		return prefix + orderAmount.toString();
	}

	@PoiColumn(idx = 5, title = "说明")
	@ApiModelProperty(name = "note", value = "附言,长度由通道确定")
	private String note;

	@ApiModelProperty(name = "id", value = "id")
	private Long id;

	@ApiModelProperty(name = "type", value = "类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,")
	private Byte type;

	@ApiModelProperty("钱包id")
	private Long walletId;

	@ApiModelProperty("ownerId")
	private Long ownerId;

	@ApiModelProperty(name = "status", value = "交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）")
	private Byte status;

	@ApiModelProperty(name = "amount", value = "金额")
	private Long amount;
}
