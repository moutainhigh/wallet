package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletCollectResp
*/
@Data
public class WalletCollectResp  {
    @ApiModelProperty("中间账户钱包id")
    private Long agentWalletId ;

    @ApiModelProperty("金额")
    private Long amount ;

    @ApiModelProperty("工单id")
    private Long applyId ;

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

    @ApiModelProperty("扫码支付信息/ JS 支付串信息/微信原生 H5 支付串信息")
    private String payInfo ;

    @ApiModelProperty("支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡")
    private Integer payMethod ;

    @ApiModelProperty("付款人钱包id")
    private Long payerWalletId ;

    @ApiModelProperty("进度。1：待发送 2：已发送 3：已接收结果")
    private Integer progress ;

    @ApiModelProperty("可退总额")
    private Long refundLimit ;

    @ApiModelProperty("开始时间")
    private String startTime ;

    @ApiModelProperty("代收状态。1：待支付 2：已支付 3：交易失败 4：交易关闭（超时或其他）")
    private Integer status ;

    @ApiModelProperty("渠道订单号")
    private String tunnelOrderNo ;

    @ApiModelProperty("通道状态")
    private String tunnelStatus ;

    @ApiModelProperty("通道成功时间")
    private String tunnelSuccTime ;

    @ApiModelProperty("通道类型。1: 浦发银企直连，2：通联云商通")
    private Integer tunnelType ;

    @ApiModelProperty("微信 APP 支付信息")
    private String weChatAPPInfo ;


}

