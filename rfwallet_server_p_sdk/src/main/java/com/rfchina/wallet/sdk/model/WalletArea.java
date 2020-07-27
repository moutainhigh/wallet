package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletArea
*/
@Data
public class WalletArea  {
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

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("主收款会员")
    private String proxyBizUserId ;

    @ApiModelProperty("主收款钱包id")
    private Long proxyWalletId ;

    @ApiModelProperty("更新时间")
    private String updateTime ;

    @ApiModelProperty("子商户号")
    private String vspCusid ;

    @ApiModelProperty("集团商户号")
    private String vspMerchantid ;


}

