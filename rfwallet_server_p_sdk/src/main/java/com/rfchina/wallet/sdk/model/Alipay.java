package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* Alipay
*/
@Data
public class Alipay  {
    @ApiModelProperty("渠道出资额(单位分)")
    private Long amount ;

    @ApiModelProperty("31：支付宝扫码支付(正扫) 32：支付宝JS支付(生活号) 33：支付宝原生")
    private Integer payType ;

    @ApiModelProperty("支付宝JS支付user_id")
    private String userId ;

    @ApiModelProperty(required = true, value = "收银宝子商户号")
    private String vspCusid;

}

