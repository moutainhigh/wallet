package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* 5.14 已绑定收银宝终端号列表
*/
@Data
public class VspTermid  {
    @ApiModelProperty("绑定时间")
    private String setDate ;

    @ApiModelProperty("收银宝商户号  单商户模式：商户收银宝商户号  集团模式：收银宝子商户号")
    private String vspCusid ;

    @ApiModelProperty("收银宝集团商户号 集团模式：集团商户收银宝商户号单商户模式：不返")
    private String vspMerchantid ;

    @ApiModelProperty("收银宝终端号")
    private String vspTermid ;


}

