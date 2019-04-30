package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayInReq {

	@ApiModelProperty(value = "钱包ID", required = true, example = "1")
	private Long walletId;

	@ApiModelProperty(value = "支付金额(单位分)", required = true, example = "1")
	private Long amount;

	@ApiModelProperty(value = "业务凭证号(业务方定义唯一,最长32字节)", required = true, example = "123")
	private String bizNo;

	@ApiModelProperty(value = "钱包批次号", required = false, hidden = true)
	private String batchNo;

	@ApiModelProperty(value = "电子凭证号(最长16字节)", required = false, hidden = true)
	private String elecChequeNo;

	@ApiModelProperty(value = "附言", required = false, example = "收入")
	private String note;

	@ApiModelProperty(required = false, value = "支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入")
	private Byte payPurpose;

}
