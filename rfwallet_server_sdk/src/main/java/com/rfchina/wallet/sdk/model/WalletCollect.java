package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletCollect
*/
@Data
public class WalletCollect  {
    @ApiModelProperty("金额")
    private Long amount ;

    @ApiModelProperty("工单id")
    private Long applyId ;

    @ApiModelProperty("支付渠道 1：余额 2：微信 4：支付宝 8：银行卡")
    private Integer channelType ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("收款人钱包id（中间账户）")
    private Long payeeWalletId ;

    @ApiModelProperty("付款人钱包id（可为空）")
    private Long payerWalletId ;

    @ApiModelProperty("进度。1：待发送 2：已发送 3：已接收结果")
    private Integer progress ;

    @ApiModelProperty("代收状态。1：待支付 2：已支付 3：交易失败 4：交易关闭（超时或其他）")
    private Integer status ;

    @ApiModelProperty("通道类型。1: 浦发银企直连，2：通联云商通")
    private Integer tunnelType ;


}

