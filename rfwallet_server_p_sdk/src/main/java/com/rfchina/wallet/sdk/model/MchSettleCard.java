package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchSettleCard
*/
@Data
public class MchSettleCard  {
    @ApiModelProperty("银行账号")
    private String bankAccount ;

    @ApiModelProperty("银行名")
    private String bankName ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("开户行")
    private String depositBank ;

    @ApiModelProperty("开户名")
    private String depositName ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("商家ID")
    private String mchId ;

    @ApiModelProperty("状态, 1:激活, 2:失效, 3:删除")
    private Integer status ;


}

