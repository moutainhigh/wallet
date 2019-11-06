package com.rfchina.wallet.server.bank.yunst.response;

import com.rfchina.platform.biztool.mapper.string.StringIndex;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class CheckAccount {

	@StringIndex(1)
	@ApiModelProperty(name="tunnel_order_no", value = "通商云订单号")
	private String tunnelOrderNo;

	@StringIndex(2)
	@ApiModelProperty(name="tunnel_order_type", value = "订单类型")
	private String tunnelOrderType;

	@StringIndex(3)
	@ApiModelProperty(name="total_amount", value = "交易金额(单位:分)")
	private Long totalAmount;

	@StringIndex(4)
	@ApiModelProperty(name="fee_amount", value = "手续费金额(单位:分")
	private Long feeAmount;

	@StringIndex(5)
	@ApiModelProperty(name="channel_finish_time", value = "交易时间")
	private Date channelFinishTime;

	@StringIndex(6)
	@ApiModelProperty(name="order_no", value = "商户订单编号")
	private String orderNo;

	@StringIndex(7)
	@ApiModelProperty(name="channel_total_amount", value = "渠道金额")
	private Long channelTotalAmount;

	@StringIndex(8)
	@ApiModelProperty(name="channel_order_no", value = "渠道流水号")
	private String channelOrderNo;

	@StringIndex(9)
	@ApiModelProperty(name="channel_fee_amount", value = "渠道手续费(单位:分)")
	private Long channelFeeAmount;

	@StringIndex(10)
	@ApiModelProperty(name="tunnel_fee_amount", value = "通商云手续费(单位:分)")
	private Long tunnelFeeAmount;

	@StringIndex(11)
	@ApiModelProperty(name="cust_instalment_amount", value = "分期金额(单位:分)")
	private Long custInstalmentAmount;

	@StringIndex(12)
	@ApiModelProperty(name="cust_fee_amount", value = "持卡人手续费")
	private Long custFeeAmount;

	@StringIndex(13)
	@ApiModelProperty(name="extend_info", value = "扩展参数")
	private String extendInfo;

	@StringIndex(14)
	@ApiModelProperty(name="credit_card_settle", value = "预付卡是否结算")
	private String creditCardSettle;

	@StringIndex(15)
	@ApiModelProperty(name="credit_card_amount", value = "预付卡交易金额(单位:分)")
	private Long creditCardAmount;

	@StringIndex(16)
	@ApiModelProperty(name="pay_method", value = "支付方式")
	private String payMethod;

	@StringIndex(17)
	@ApiModelProperty(name="origin_order_no", value = "原商户订单号")
	private String originOrderNo;

	@StringIndex(18)
	@ApiModelProperty(name="origin_tunnel_order_no", value = "原通商云订单号")
	private String originTunnelOrderNo;

	@StringIndex(19)
	@ApiModelProperty(name="channel_type", value = "渠道交易类型")
	private String channelType;

	@StringIndex(20)
	@ApiModelProperty(name="channel_card", value = "卡号")
	private String channelCardNo;

	@StringIndex(21)
	@ApiModelProperty(name="channel_card_type", value = "卡类别")
	private String channelCardType;
}
