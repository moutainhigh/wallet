package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* BankArea
*/
@Data
public class BankArea  {
    @ApiModelProperty("地区编码")
    private String areaCode ;

    @ApiModelProperty("市")
    private String cityName ;

    @ApiModelProperty("省")
    private String provinceName ;


}

