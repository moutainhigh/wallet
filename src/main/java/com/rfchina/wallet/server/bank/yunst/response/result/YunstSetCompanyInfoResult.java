package com.rfchina.wallet.server.bank.yunst.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YunstSetCompanyInfoResult {
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)")
	private String bizUserId;
	@ApiModelProperty(value = "审核结果 2-成功 3-审核失败  仅自动审核时返回")
	private Long result;
	@ApiModelProperty(value = "失败原因")
	private String failReason;
	@ApiModelProperty(value = "备注")
	private String remark;
}
