package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* SettleQueryResp
*/
@Data
public class SettleQueryResp  {
    @ApiModelProperty("工单ID")
    private Long applyId ;

    @ApiModelProperty("业务凭证号")
    private String bizNo ;

    @ApiModelProperty("交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败")
    private Integer status ;

    @ApiModelProperty("系统错误信息")
    private String sysErrMsg ;

    @ApiModelProperty("用户错误信息")
    private String userErrMsg ;

    @ApiModelProperty("钱包ID")
    private Long walletId ;


}

