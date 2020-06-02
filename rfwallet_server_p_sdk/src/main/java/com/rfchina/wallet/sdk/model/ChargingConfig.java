package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* ChargingConfig
*/
@Data
public class ChargingConfig  {
    @ApiModelProperty("收费业务主键")
    private String chargingKey ;

    @ApiModelProperty("收费区间结束")
    private Long chargingRegionEnd ;

    @ApiModelProperty("收费区间开始")
    private Long chargingRegionStart ;

    @ApiModelProperty("收费值，跟type相关")
    private BigDecimal chargingValue ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("最后更新时间")
    private String lastUpdTime ;

    @ApiModelProperty("最大收费款项")
    private Long maxFee ;

    @ApiModelProperty("最小收费款项")
    private Long minFee ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("标题说明")
    private String title ;

    @ApiModelProperty("收费方式，1按次收费，2按比率收费")
    private Integer type ;


}

