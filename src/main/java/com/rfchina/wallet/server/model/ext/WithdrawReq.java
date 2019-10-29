package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "提现请求")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawReq {

	@ApiModelProperty(required = true, name = "wallet_id", value = "钱包用户登陆态ID")
	private Long payerWalletId;

	@ApiModelProperty(required = true, name = "card_id", value = "银行卡id")
	private Long cardId;

	@ApiModelProperty(required = true, name = "biz_no", value = "业务凭证号(业务方定义唯一,最长32字节)", example = "123")
	private String bizNo;

	@ApiModelProperty(required = true, value = "支付金额(单位分)", example = "1")
	private Long amount;

	@ApiModelProperty(required = true,value = "手续费，单位:分。如果不存在,则填 0。")
	private Long fee;

	@ApiModelProperty(name = "expire_time", value = "订单过期时间,订单最长时效为 24 小时")
	private Date expireTime;

	@ApiModelProperty(name = "industry_code", value = "行业代码（由渠道分配）")
	private String industryCode;

	@ApiModelProperty(name = "industry_name", value = "行业名称（由渠道分配）")
	private String industryName;

}
