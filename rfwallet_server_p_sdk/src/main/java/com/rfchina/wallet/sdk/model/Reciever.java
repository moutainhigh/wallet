package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* Reciever
*/
@Data
public class Reciever  {
    @ApiModelProperty("金额,单位:分")
    private Long amount ;

    @ApiModelProperty("代付接口手续费（代收无效）,单位:分")
    private Long feeAmount ;

    @ApiModelProperty("收款人钱包ID")
    private Long walletId ;


}

