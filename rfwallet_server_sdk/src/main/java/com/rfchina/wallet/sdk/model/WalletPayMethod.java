package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletPayMethod
*/
@Data
public class WalletPayMethod  {
    @ApiModelProperty("支付宝支付")
    private Alipay alipay ;

    @ApiModelProperty("余额支付")
    private Balance balance ;

    @ApiModelProperty("刷卡支付")
    private CodePay codePay ;

    @ApiModelProperty("")
    private Integer methods ;

    @ApiModelProperty("微信支付")
    private Wechat wechat ;

    @ApiModelProperty(value = "银行卡支付")
    private BankCard bankCard;


}

