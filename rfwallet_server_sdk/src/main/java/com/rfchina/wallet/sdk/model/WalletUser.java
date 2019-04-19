package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletUser
*/
@Data
public class WalletUser  {
    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("帐号ID")
    private Long id ;

    @ApiModelProperty("钱包信息最后更新日期")
    private String lastUpdTime ;

    @ApiModelProperty("登录手机号")
    private String mobile ;

    @ApiModelProperty("注册进度, 1:已通过身份验证, 2:已开通钱包")
    private Integer registerProgress ;

    @ApiModelProperty("帐号状态: 1:正常，2：禁用")
    private Integer status ;

    @ApiModelProperty("用户ID")
    private Long uid ;

    @ApiModelProperty("关联的钱包ID")
    private Long walletId ;


}

