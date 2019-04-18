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
    @ApiModelProperty("受理编号")
    private String acceptNo ;

    @ApiModelProperty("支付金额")
    private Long amount ;

    @ApiModelProperty("电子凭证号")
    private String elecChequeNo ;

    @ApiModelProperty("失败原因")
    private String errMsg ;

    @ApiModelProperty("支付状态。1：受理中，2：交易成功。3：交易失败")
    private Integer status ;

    @ApiModelProperty("交易日期")
    private String transDate ;


}

