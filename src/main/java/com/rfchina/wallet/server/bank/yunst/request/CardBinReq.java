package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "4.1.9 查询卡 bin")
public class CardBinReq implements YunstBaseReq  {

	@ApiModelProperty(required = true, value = "银行卡号")
	private String cardNo ;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "getBankCardBin";
	}
}
