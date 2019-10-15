package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.ChannelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "代收请求")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeReq {

	@ApiModelProperty(required = true, name = "wallet_id", value = "钱包用户登陆态ID")
	private Long payerWalletId;

	@ApiModelProperty(required = true, name = "biz_no", value = "业务凭证号(业务方定义唯一,最长32字节)", example = "123")
	private String bizNo;

	@ApiModelProperty(required = true, value = "支付金额(单位分)", example = "1")
	private Long amount;

	@ApiModelProperty(required = true,value = "手续费，单位:分。如果不存在,则填 0。")
	private Long fee;

	@ApiModelProperty(required = true,name = "validate_type", value = "交易验证方式 1：短信 2：密码")
	private Byte validateType;

	@ApiModelProperty(name = "expire_time", value = "订单过期时间,订单最长时效为 24 小时")
	private Date expireTime;

	@ApiModelProperty(name = "industry_code", value = "行业代码（由渠道分配）")
	private String industryCode;

	@ApiModelProperty(name = "industry_name", value = "行业名称（由渠道分配）")
	private String industryName;

	@ApiModelProperty(name = "wallet_pay_method", value = "钱包支付方式")
	private WalletPayMethod walletPayMethod;
}
