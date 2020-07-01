package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* StatCharging
*/
@Data
public class StatCharging  {
    @ApiModelProperty("统计日期")
    private String chargingDate ;

    @ApiModelProperty("公司验证次数")
    private Long companyVerifyCount ;

    @ApiModelProperty("公司认证总费用")
    private Long companyVerifyFee ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("是否删除 0：正常 1：已删除")
    private Integer deleted ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("本地的支付手续费,单位分")
    private Long localPayFee ;

    @ApiModelProperty("本地的充值手续费,单位分")
    private Long localRechargeFee ;

    @ApiModelProperty("个人验证次数")
    private Long personVerifyCount ;

    @ApiModelProperty("个人认证总费用")
    private Long personVerifyFee ;

    @ApiModelProperty("第三方的支付手续费,单位分")
    private Long thirdPayFee ;

    @ApiModelProperty("第三方的充值手续费,单位分")
    private Long thirdRechargeFee ;

    @ApiModelProperty("渠道类型。1: 浦发银企直连，2：通联云商通")
    private Integer tunnelType ;

    @ApiModelProperty("提现次数")
    private Long withdrawCount ;

    @ApiModelProperty("提现总费用")
    private Long withdrawFee ;


}

