package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletPerson
*/
@Data
public class WalletPerson  {
    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("邮箱")
    private String email ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("证件号")
    private String idNo ;

    @ApiModelProperty("证件类型，1:身份证")
    private Integer idType ;

    @ApiModelProperty("钱包信息最后更新日期")
    private String lastUpdTime ;

    @ApiModelProperty("姓名")
    private String name ;

    @ApiModelProperty("实名认证类型，1:身份证实名,2:手机号实名")
    private Integer realLevel ;

    @ApiModelProperty("电话")
    private String tel ;

    @ApiModelProperty("关联的钱包ID")
    private Long walletId ;


}

