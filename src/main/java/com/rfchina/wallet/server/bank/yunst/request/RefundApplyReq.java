package com.rfchina.wallet.server.bank.yunst.request;

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
public class RefundApplyReq implements YunstBaseReq {

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "refund";
	}

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(required = true, value = "商户原订单号")
	private String oriBizOrderNo;

	@ApiModelProperty(required = true, value = "商户系统用户标识,商户系统中唯一编号。退款收款人。")
	private String bizUserId;

	@ApiModelProperty(required = true, value = "退款方式 D1:D+1 14:30 向渠道发起退款 D0:D+0 实时向渠道发起退款")
	private String refundType;

	@ApiModelProperty(value = "托管代收订单中的收款人的退款金额")
	private List<RefundInfo> refundList;

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RefundInfo {

		@ApiModelProperty(value = "账户集编号(标准版代收付需原扣减原 recieverList金额)")
		private String accountSetNo;

		@ApiModelProperty(value = "商户系统用户标识,商户系统中唯一编号。")
		private String bizUserId;

		@ApiModelProperty(value = "金额,单位:分")
		private Long amount;
	}

	@ApiModelProperty(value = "后台通知地址")
	private String backUrl;

	@ApiModelProperty(required = true, value = "本次退款总金额")
	private Long amount;

	@ApiModelProperty(value = "代金券退款金额")
	private Long couponAmount;

	@ApiModelProperty(value = "手续费退款金额")
	private Long feeAmount;

	@ApiModelProperty(value = "扩展信息")
	private String extendInfo;
}
