package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* SettleResp
*/
@Data
public class SettleResp  {
    @ApiModelProperty("分帐记录")
    private WalletClearing clearing ;

    @ApiModelProperty("订单")
    private WalletOrder order ;


}

