package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AgentPayReq {

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@Max(100)
	@ApiModelProperty(required = true, value = "源托管代收订单付款信息")
	private List<CollectPay> collectPayList;


	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@ApiModel
	public static class CollectPay {

		@ApiModelProperty(value = "相关代收交易的“商户订单号")
		private String bizOrderNo;

		@ApiModelProperty(value = "金额,单位:分")
		private Long amount;
	}

	@ApiModelProperty(required = true, value = "商户系统用户标识,商户系统中唯一编号。")
	private String bizUserId;

	@ApiModelProperty(required = true,value = "收款方的账户集编号")
	private String accountSetNo;

	@ApiModelProperty(required = true,value = "后台通知地址")
	private String backUrl;

	@ApiModelProperty(required = true,value = "金额,单位:分")
	private Long amount;

	@ApiModelProperty(required = true,value = "手续费,单位:分")
	private Long fee;

	@ApiModelProperty(value = "内扣。支持分账到会员或者平台账户")
	private List<SplitRule> splitRuleList;

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SplitRule {

		@ApiModelProperty(value = "商户系统用户标识,商户系统中唯一编号。")
		private String bizUserId;

		@ApiModelProperty(value = "如果向会员分账,不上送,默认为唯一托管账户集。如果向平台分账,请填写平台的标准账户集编号(不支持 100003-准备金额度账户集)。")
		private String accountSetNo;

		@ApiModelProperty(value = "金额,单位:分")
		private Long amount;

		@ApiModelProperty(value = "手续费,内扣,单位:分")
		private Long fee;

		@ApiModelProperty(value = "备注,最长 50 个字符")
		private String remark;

		@ApiModelProperty(value = "分账列表")
		private List<SplitRule> splitRuleList;
	}

	@ApiModelProperty(value = "摘要 最多20个字符")
	private String summary;

//	@ApiModelProperty(value = "商品类型")
//	private Long goodsType;
//
//	@ApiModelProperty(value = "商户系统商品编号")
//	private String bizGoodsNo;
//
//	@ApiModelProperty(value = "业务码")
//	private String tradeCode;


	@ApiModelProperty(value = "最多50个字符,商户拓展参数,用于透传给商户")
	private String extendInfo;
}
