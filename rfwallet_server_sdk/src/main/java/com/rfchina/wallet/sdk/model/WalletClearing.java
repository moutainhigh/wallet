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
    @ApiModelProperty("总金额")
    private Long amount ;

    @ApiModelProperty("工单id")
    private Long applyId ;

    @ApiModelProperty("代收单id")
    private Long collectId ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("最后清算结束时间")
    private String endTime ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("订单号")
    private String orderNo ;

    @ApiModelProperty("最后清算发起时间")
    private String startTime ;

    @ApiModelProperty("清算状态。1：未清算 2：已清算  3:交易失败")
    private Integer status ;

    @ApiModelProperty("渠道订单号")
    private String tunnelOrderNo ;

    @ApiModelProperty("钱包id")
    private Long walletId ;


}

