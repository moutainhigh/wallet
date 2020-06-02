package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchWalletInfo
*/
@Data
public class MchWalletInfo  {
    @ApiModelProperty("是否有绑定钱包收款账号 1-是 2-否")
    private Integer bindMchAccount ;

    @ApiModelProperty("钱包id")
    private Long walletId ;


}

