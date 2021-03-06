package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* StatChargingDetailVo
*/
@Data
public class StatChargingDetailVo  {
    @ApiModelProperty("业务凭证号")
    private String bizNo ;

    @ApiModelProperty("业务时间")
    private String bizTime ;

    @ApiModelProperty("事件")
    private String event ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("本地的通道手续费")
    private Long localTunnelFee ;

    @ApiModelProperty("方法名")
    private String methodName ;

    @ApiModelProperty("钱包订单号")
    private String orderNo ;

    @ApiModelProperty("第三方的通道手续费")
    private Long thirdTunnelFee ;


}

