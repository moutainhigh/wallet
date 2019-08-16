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
    @ApiModelProperty("业务定制内容")
    private String businessRemark ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("钱包信息最后更新日期")
    private String lastUpdTime ;

    @ApiModelProperty("登录手机号")
    private String mobile ;

    @ApiModelProperty("注册进度,二进制形式 0:初始化帐号 1:已通过身份验证 2:已创建钱包 4:已绑定银行")
    private Integer registerProgress ;

    @ApiModelProperty("帐号状态: 1:正常，2：禁用")
    private Integer status ;

    @ApiModelProperty("用户ID")
    private Long userId ;

    @ApiModelProperty("关联的钱包ID")
    private Long walletId ;


}

