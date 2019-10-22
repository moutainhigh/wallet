package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* RefundInfo
*/
@Data
public class RefundInfo  {
    @ApiModelProperty("金额,单位:分")
    private Long amount ;

    @ApiModelProperty("钱包ID")
    private Long walletId ;


}

