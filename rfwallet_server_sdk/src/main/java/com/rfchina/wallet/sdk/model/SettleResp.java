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
    @ApiModelProperty("工单Id")
    private Long applyId ;

    @ApiModelProperty("分帐列表")
    private List<WalletClearing> clearings ;


}

