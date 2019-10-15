package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* Wechat
*/
@Data
public class Wechat  {
    @ApiModelProperty("渠道出资额(单位分)")
    private Long amount ;

    @ApiModelProperty("用户下单及调起支付的终端IP")
    private String cusip ;

    @ApiModelProperty("微信JS支付openid")
    private String openId ;

    @ApiModelProperty("21:微信小程序支付 22:微信原生APP支付 23:微信原生H5支付 24:微信JS支付(公众号) 25:微信扫码支付(正扫)")
    private Integer payType ;

    @ApiModelProperty("场景信息（H5支付）")
    private String sceneInfo ;

    @ApiModelProperty("微信端应用ID:appid（H5支付）")
    private String subAppId ;


}

