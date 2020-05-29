package com.rfchina.wallet.server.bank.yunst.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@Data
@ApiModel(description = "4.1.21 会员收银宝渠道商户信息及终端信息绑定")
public class VspTermidResp {


	@ApiModelProperty(value = "商户系统用户标识，商户系统中唯一编号。")
	private String bizUserId;

	@ApiModelProperty(value = "绑定、查询收银宝终端号结果")
	private String result;

	@ApiModelProperty(value = "已绑定收银宝终端号列")
	private List<VspTermid> vspTermidList;


	@Data
	@ApiModel(description = "5.14 已绑定收银宝终端号列表")
	public static class VspTermid {

		@ApiModelProperty(value = "收银宝集团商户号 集团模式：集团商户收银宝商户号单商户模式：不返")
		private String vspMerchantid;

		@ApiModelProperty(value = "收银宝商户号  单商户模式：商户收银宝商户号  集团模式：收银宝子商户号")
		private String vspCusid;

		@ApiModelProperty(value = "收银宝终端号")
		private String vspTermid;

		@ApiModelProperty(value = "绑定时间")
		private String setDate;

	}
}
