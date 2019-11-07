package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* 代收请求
*/
@Data
public class CollectReq  {
    @ApiModelProperty("支付金额(单位分)")
    private Long amount ;

    @ApiModelProperty("业务凭证号(业务方定义唯一,最长32字节)")
    private String bizNo ;

    @ApiModelProperty("订单过期时间,订单最长时效为 24 小时")
    private String expireTime ;

    @ApiModelProperty("手续费，单位:分。如果不存在,则填 0。")
    private Long fee ;

    @ApiModelProperty("行业代码（由渠道分配）")
    private String industryCode ;

    @ApiModelProperty("行业名称（由渠道分配）")
    private String industryName ;

    @ApiModelProperty("附言")
    private String note ;

    @ApiModelProperty("钱包用户登陆态ID")
    private Long payerWalletId ;

    @ApiModelProperty("收款列表")
    private List<Reciever> recievers ;

    @ApiModelProperty("交易验证方式 0：无验证 1：短信 2：密码")
    private Integer validateType ;

    @ApiModelProperty("钱包支付方式")
    private WalletPayMethod walletPayMethod ;


}

