package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletClearing
*/
@Data
public class WalletClearing  {
    @ApiModelProperty("中间账户钱包id")
    private Long agentWalletId ;

    @ApiModelProperty("金额")
    private Long amount ;

    @ApiModelProperty("分帐id")
    private Long collectInfoId ;

    @ApiModelProperty("代收单号")
    private String collectOrderNo ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("订单id")
    private Long orderId ;


}

