package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* OutlayAccount
*/
@Data
public class OutlayAccount  {
    @ApiModelProperty("账号余额")
    private Long accountAmount ;

    @ApiModelProperty("账户名称")
    private String accountName ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("商户ID,三位随机码+秒级时间戳取后五位")
    private String mchId ;

    @ApiModelProperty("营销账号")
    private String outlayAccountId ;

    @ApiModelProperty("累计支出金额")
    private Long payAmount ;

    @ApiModelProperty("累计支出次数")
    private Integer payCount ;

    @ApiModelProperty("累计充值金额")
    private Long rechargeAmount ;

    @ApiModelProperty("累计充值次数")
    private Integer rechargeCount ;

    @ApiModelProperty("账户状态, 1:正常,2:冻结")
    private Integer status ;


}

