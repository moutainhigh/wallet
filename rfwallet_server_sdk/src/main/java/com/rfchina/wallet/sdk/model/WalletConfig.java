package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletConfig
*/
@Data
public class WalletConfig  {
    @ApiModelProperty("审批用户id")
    private String auditUserId ;

    @ApiModelProperty("审批用户名称")
    private String auditUserName ;

    @ApiModelProperty("审批用户角色")
    private String auditUserRole ;

    @ApiModelProperty("自动提现状态。1：开启，2：关闭")
    private Integer autoWithdrawStatus ;

    @ApiModelProperty("自动提现金额")
    private Long autoWithdrawThreshold ;

    @ApiModelProperty("自动提现类型。1：按余额提现 2：按支付单提现")
    private Integer autoWithdrawType ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("企业手动提现最低金额")
    private Long manualWithdrawCompanyMin ;

    @ApiModelProperty("个人手动提现最低金额")
    private Long manualWithdrawPersonMin ;

    @ApiModelProperty("配置状态。1：正常，2：失效")
    private Integer status ;

    @ApiModelProperty("钱包ID")
    private Long walletId ;


}

