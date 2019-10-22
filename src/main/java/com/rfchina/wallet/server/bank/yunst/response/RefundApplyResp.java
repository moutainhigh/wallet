package com.rfchina.wallet.server.bank.yunst.response;

import com.rfchina.wallet.server.bank.yunst.request.YunstBaseReq;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class RefundApplyResp {

	@ApiModelProperty( value = "支付状态")
	private String payStatus;

	@ApiModelProperty( value = "支付失败信息")
	private String payFailMessage;

	@ApiModelProperty( value = "通商云订单编号")
	private String orderNo;

	@ApiModelProperty( value = "商户订单号(支付订")
	private String bizOrderNo;

	@ApiModelProperty(value = "本次退款总金额")
	private Long amount;

	@ApiModelProperty( value = "代金券退款金额")
	private Long couponAmount;

	@ApiModelProperty(value = "手续费退款金额")
	private Long feeAmount;

	@ApiModelProperty(value = "扩展信息")
	private String extendInfo;
}
