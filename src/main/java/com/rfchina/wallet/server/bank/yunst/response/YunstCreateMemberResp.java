package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YunstCreateMemberResp {
	@ApiModelProperty(value = "结果数据")
	private CreateMemeberResult data;

	@Data
	public static class CreateMemeberResult {
		@ApiModelProperty(value = "云商通用户唯一标识")
		private String userId;
		@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:C+mchId)")
		private String bizUserId;
	}

}
