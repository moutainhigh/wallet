package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletTerminalExt
*/
@Data
public class WalletTerminalExt  {
    @ApiModelProperty("通联appid")
    private String appId ;

    @ApiModelProperty("地区码")
    private String areaCode ;

    @ApiModelProperty("地区名")
    private String areaName ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("创建者id")
    private String creatorId ;

    @ApiModelProperty("创建者名称")
    private String creatorName ;

    @ApiModelProperty("主收款钱包id")
    private Long proxyWalletId ;

    @ApiModelProperty("状态： 0：未绑定，1：已绑定，2：已解绑")
    private Integer status ;

    @ApiModelProperty("更新时间")
    private String updateTime ;

    @ApiModelProperty("子商户号")
    private String vspCusid ;

    @ApiModelProperty("集团商户号")
    private String vspMerchantid ;

    @ApiModelProperty("终端号")
    private String vspTermid ;


}

