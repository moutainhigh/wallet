package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* BalanceJob
*/
@Data
public class BalanceJob  {
    @ApiModelProperty("钱包对账日期")
    private String balanceDate ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("是否删除 0：正常 1：已删除")
    private Integer deleted ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("状态 1：进行中 2：对账完成 3:对账失败")
    private Integer status ;

    @ApiModelProperty("对账文件")
    private String walletFileUrl ;


}

