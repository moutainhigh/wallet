package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* BankCode
*/
@Data
public class BankCode  {
    @ApiModelProperty("所属地区")
    private String areaCode ;

    @ApiModelProperty("银行行号")
    private String bankCode ;

    @ApiModelProperty("银行名称")
    private String bankName ;

    @ApiModelProperty("所属省市")
    private String cityName ;

    @ApiModelProperty("所属分类代码")
    private String classCode ;

    @ApiModelProperty("所属分类行名")
    private String className ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("所属地区")
    private String provinceName ;

    @ApiModelProperty("")
    private Integer weight ;


}

