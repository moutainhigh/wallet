package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletRefund
*/
@Data
public class WalletRefund  {
    @ApiModelProperty("中间账户钱包id")
    private Long agentWalletId ;

    @ApiModelProperty("退款金额")
    private Long amount ;

    @ApiModelProperty("工单id")
    private Long applyId ;

    @ApiModelProperty("原代收金额")
    private Long collectAmount ;

    @ApiModelProperty("代收单id")
    private Long collectId ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("结束时间")
    private String endTime ;

    @ApiModelProperty("通道错误码")
    private String errCode ;

    @ApiModelProperty("系统错误信息")
    private String errMsg ;

    @ApiModelProperty("过期时间")
    private String expireTime ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("订单号")
    private String orderNo ;

    @ApiModelProperty("付款人钱包id")
    private Long payerWalletId ;

    @ApiModelProperty("进度。1：待发送 2：已发送 3：已接收结果")
    private Integer progress ;

    @ApiModelProperty("开始时间")
    private String startTime ;

    @ApiModelProperty("退款状态。1：未退款 2：已退款 3:交易失败")
    private Integer status ;

    @ApiModelProperty("渠道订单号")
    private String tunnelOrderNo ;

    @ApiModelProperty("通道状态")
    private String tunnelStatus ;

    @ApiModelProperty("通道成功时间")
    private String tunnelSuccTime ;

    @ApiModelProperty("通道类型。1: 浦发银企直连，2：通联云商通")
    private Integer tunnelType ;


}

