package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletTerminal
*/
@Data
public class WalletTerminal  {
    @ApiModelProperty("通联appId")
    private String appId ;

    @ApiModelProperty("绑定时间")
    private String bindTime ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("商家id")
    private String mchId ;

    @ApiModelProperty("商家id")
    private String mchName ;

    @ApiModelProperty("省份")
    private String province ;

    @ApiModelProperty("门店地址")
    private String shopAddress ;

    @ApiModelProperty("状态： 0：未绑定，1：已绑定，2：已解绑")
    private Integer status ;

    @ApiModelProperty("绑定时间")
    private String unbindTime ;

    @ApiModelProperty("子商户号")
    private String vspCusid ;

    @ApiModelProperty("集团商户号")
    private String vspMerchantid ;

    @ApiModelProperty("终端号")
    private String vspTermid ;

    @ApiModelProperty("钱包id")
    private Long walletId ;


}

