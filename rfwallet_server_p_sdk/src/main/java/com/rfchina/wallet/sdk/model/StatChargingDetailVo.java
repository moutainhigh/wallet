package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
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

    @ApiModelProperty("计费方式，1按次收费，2按比率收费")
    private Integer chargingType ;

    @ApiModelProperty("计费单价，计费比例或金额")
    private BigDecimal chargingValue ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("是否删除 0：正常 1：已删除")
    private Integer deleted ;

    @ApiModelProperty("事件")
    private String event ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("")
    private Long localTunnelFee ;

    @ApiModelProperty("方法名")
    private String methodName ;

    @ApiModelProperty("钱包订单号")
    private String orderNo ;

    @ApiModelProperty("服务名")
    private String serviceName ;

    @ApiModelProperty("")
    private Long thirdTunnelFee ;

    @ApiModelProperty("通道次数")
    private Long tunnelCount ;

    @ApiModelProperty("渠道类型。1: 浦发银企直连，2：通联云商通")
    private Integer tunnelType ;


}

