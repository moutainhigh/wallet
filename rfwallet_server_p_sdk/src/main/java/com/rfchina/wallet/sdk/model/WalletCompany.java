package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletCompany
*/
@Data
public class WalletCompany  {
    @ApiModelProperty("公司名称")
    private String companyName ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("公司邮箱")
    private String email ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("钱包信息最后更新日期")
    private String lastUpdTime ;

    @ApiModelProperty("公司电话")
    private String tel ;

    @ApiModelProperty("关联的钱包ID")
    private Long walletId ;


}

