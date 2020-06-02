package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchSettleCfg
*/
@Data
public class MchSettleCfg  {
    @ApiModelProperty("账号ID")
    private String accountId ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("最后更新时间")
    private String lastUpdTime ;

    @ApiModelProperty("商户ID")
    private String mchId ;

    @ApiModelProperty("商户结算账号ID或钱包账号")
    private String settleAccount ;

    @ApiModelProperty("商户结算账号类型, 1: 银行账号, 2: 微众钱包账号，3：富力钱包")
    private Integer settleType ;

    @ApiModelProperty("状态, 1:启用, 2:禁用")
    private Integer status ;


}

