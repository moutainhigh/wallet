package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchAccountAndSettleCfgDetail
*/
@Data
public class MchAccountAndSettleCfgDetail  {
    @ApiModelProperty("收款账号创建时间")
    private String accountCreateTime ;

    @ApiModelProperty("收款账户ID")
    private String accountId ;

    @ApiModelProperty("收款账户名称")
    private String accountName ;

    @ApiModelProperty("账号状态, 1:正常, 2:冻结")
    private Integer accountStatus ;

    @ApiModelProperty("结算账号绑定时间")
    private String createTime ;

    @ApiModelProperty("商户ID")
    private String mchId ;

    @ApiModelProperty("商户结算账号ID或钱包账号")
    private String settleAccount ;

    @ApiModelProperty("商户结算账号类型, 1: 银行账号, 2: 微众钱包账号， 3： 富力钱包帐号")
    private Integer settleType ;

    @ApiModelProperty("按照结算类型区分，银行卡结算：银行卡的开户名;微众/富力钱包结算：钱包ID")
    private String value1 ;

    @ApiModelProperty("按照结算类型区分，银行卡结算：银行卡的开户行;微众/富力钱包结算：钱包的余额")
    private String value2 ;

    @ApiModelProperty("按照结算类型区分，银行卡结算：银行卡的卡号;微众钱包结算：钱包状态（N:正常, L:锁定, O:未激活, F:失败）; 富力钱包结算：钱包状态（1:待审核，2：激活,3：禁用）")
    private String value3 ;

    @ApiModelProperty("富力钱包详情（结算类型为富力钱包时不为空）")
    private WalletInfoResp wallet ;


}

