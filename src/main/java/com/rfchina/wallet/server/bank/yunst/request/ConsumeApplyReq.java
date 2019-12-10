package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "4.2.4 消费申请")
public class ConsumeApplyReq implements YunstBaseReq {

	@ApiModelProperty(required = true, value = "商户系统用户标识,商户系统中唯一编号。付款用户的 bizUserId,支持个人会员、企业会员")
	private String payerId;

	@ApiModelProperty(required = true, value = "支付协议号")
	private String protocolNo;

	@ApiModelProperty(required = true, value = "商户系统用户标识")
	private String receiverId ;

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(required = true, value = "支付方式")
	private Map<String, Object> payMethod;

	@ApiModelProperty(required = true, value = "订单金额")
	private Long amount;

	@ApiModelProperty(required = true, value = "手续费")
	private Long fee;

	@ApiModelProperty(value = "内扣。支持分账到会员或者平台账户")
	private SplitRule splitRule;

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
		private List<AgentPayReq.SplitRule> splitRuleList;

	}

	@ApiModelProperty(required = true, value = "后台通知地址")
	private String backUrl;

	@ApiModelProperty(value = "订单过期时间")
	private String orderExpireDatetime;

	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	@ApiModelProperty(value = "商品描述")
	private String goodsDesc;

	@ApiModelProperty(value = "商品类型")
	private Long goodsType;

	@ApiModelProperty(value = "商户系统商品编号")
	private String goodsNo;

	@ApiModelProperty(required = true, value = "行业代码")
	private String industryCode;

	@ApiModelProperty(required = true, value = "行业名称")
	private String industryName;

	@ApiModelProperty(required = true, value = "访问终端类型")
	private Long source;

	@ApiModelProperty(value = "摘要")
	private String summary;

	@ApiModelProperty(value = "扩展参数")
	private String extendInfo;


	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "consumeProtocolApply";
	}
}
