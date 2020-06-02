package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchAccountAndSettleCfgDetailAggr
*/
@Data
public class MchAccountAndSettleCfgDetailAggr  {
    @ApiModelProperty("钱包余额")
    private Long balance ;

    @ApiModelProperty("配置列表")
    private List<MchAccountAndSettleCfgDetail> list ;


}

