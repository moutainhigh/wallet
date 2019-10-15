package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* CollectQueryResp
*/
@Data
public class CollectQueryResp  {
    @ApiModelProperty("工单ID")
    private Long applyId ;

    @ApiModelProperty("商户订单号(支付订单)")
    private String bizNo ;

    @ApiModelProperty("商户系统用户标识 仅交易验证方式为“0”时返回")
    private String bizUserId ;

    @ApiModelProperty("通商云订单号")
    private String orderNo ;

    @ApiModelProperty("支付失败信息")
    private String payFailMessage ;

    @ApiModelProperty("支付状态")
    private String payStatus ;

    @ApiModelProperty("交易验证方式")
    private Long validateType ;

    @ApiModelProperty("钱包ID")
    private Long walletId ;


}

