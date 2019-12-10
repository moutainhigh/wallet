package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* BankClass
*/
@Data
public class BankClass  {
    @ApiModelProperty("银行编码")
    private String classCode ;

    @ApiModelProperty("银行名称")
    private String className ;


}

