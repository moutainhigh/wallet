package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* Bank
*/
@Data
public class Bank  {
    @ApiModelProperty("支行编码")
    private String bankCode ;

    @ApiModelProperty("支行名称")
    private String bankName ;


}

