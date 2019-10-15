package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* CodePay
*/
@Data
public class CodePay  {
    @ApiModelProperty("渠道出资额(单位分)")
    private Long amount ;

    @ApiModelProperty("支付授权码，支付宝被扫刷卡支付时,用户的付款二维码")
    private String authcode ;

    @ApiModelProperty("41：收银宝刷卡支付（被扫）")
    private Integer payType ;


}

