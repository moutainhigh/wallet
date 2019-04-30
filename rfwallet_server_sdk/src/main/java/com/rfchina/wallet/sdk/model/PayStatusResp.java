package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* PayStatusResp
*/
@Data
public class PayStatusResp  {
    @ApiModelProperty("支付金额")
    private Long amount ;

    @ApiModelProperty("钱包批次号")
    private String batchNo ;

    @ApiModelProperty("业务凭证号")
    private String bizNo ;

    @ApiModelProperty("失败原因")
    private String errMsg ;

    @ApiModelProperty("交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销")
    private Integer status ;

    @ApiModelProperty("交易日期")
    private String transDate ;


}

