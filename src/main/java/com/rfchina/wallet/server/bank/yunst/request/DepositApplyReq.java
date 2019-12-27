package com.rfchina.wallet.server.bank.yunst.request;

import com.rfchina.wallet.server.bank.yunst.request.CollectApplyReq.CollectPayMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@ApiModel(description = "4.2.2 充值申请")
@Getter
@Builder
public class DepositApplyReq implements YunstBaseReq {

	@Override
	public String getServcieName() {
		return "OrderService";
	}

	@Override
	public String getMethodName() {
		return "depositApply";
	}

	@ApiModelProperty(required = true, value = "商户订单号(支付订单)")
	private String bizOrderNo;

	@ApiModelProperty(required = true, value = "商户系统用户标识")
	private String bizUserId;

	@ApiModelProperty(required = true, value = "账户集编号")
	private String accountSetNo;

	@ApiModelProperty(required = true, value = "订单金额")
	private Long amount;

	@ApiModelProperty(required = true, value = "手续费")
	private Long fee;

	@ApiModelProperty(value = "交易验证方式")
	private Long validateType;

	@ApiModelProperty(value = "前台通知地址")
	private String frontUrl;

	@ApiModelProperty(required = true, value = "后台通知地址")
	private String backUrl;

	@ApiModelProperty(value = "订单过期时间  格式：yyyy-MM-dd HH:mm:ss")
	private String orderExpireDatetime;

	@ApiModelProperty(required = true, value = "支付方式")
	private Map<String, Object> payMethod;

	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	@ApiModelProperty(required = true, value = "行业代码")
	private String industryCode;

	@ApiModelProperty(required = true, value = "行业名称")
	private String industryName;

	@ApiModelProperty(required = true, value = "访问终端类型")
	private Long source;

	@ApiModelProperty(value = "摘要")
	private String summary;

	@ApiModelProperty(value = "扩展信息")
	private String extendInfo;

}
