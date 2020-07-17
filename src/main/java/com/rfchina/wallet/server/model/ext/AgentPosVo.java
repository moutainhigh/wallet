package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AgentPosVo {

	@ApiModelProperty(required = true, value = "钱包id")
	private Long walletId;

	@ApiModelProperty(required = true, value = "商户系统用户标识，商户系统中唯一 编号。")
	private String bizUserId;

	@ApiModelProperty(required = true, value = "收银宝集团商户号")
	private String vspMerchantid;

	@ApiModelProperty(required = true, value = "收银宝商户号")
	private String vspCusid;

	@ApiModelProperty(required = true, value = "收银宝分配的")
	private String appid;

	@ApiModelProperty(name="province", value = "省份")
	private String province;

	@ApiModelProperty(name="mch_id", value = "商家id")
	private String mchId;

	@ApiModelProperty(name="mch_name", value = "商家id")
	private String mchName;

	@ApiModelProperty(name="shop_address", value = "门店地址")
	private String shopAddress;

}
