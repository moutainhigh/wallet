package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletCard
*/
@Data
public class WalletCard  {
    @ApiModelProperty("银行账号")
    private String bankAccount ;

    @ApiModelProperty("银行代码")
    private String bankCode ;

    @ApiModelProperty("银行名称")
    private String bankName ;

    @ApiModelProperty("银行卡类型 1-储蓄卡 2-信用卡")
    private Integer cardType ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("开户支行")
    private String depositBank ;

    @ApiModelProperty("开户名")
    private String depositName ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("是否默认银行卡: 1:是，2：否")
    private Integer isDef ;

    @ApiModelProperty("是否对公账户: 1:是，2：否")
    private Integer isPublic ;

    @ApiModelProperty("钱包信息最后更新日期")
    private String lastUpdTime ;

    @ApiModelProperty("绑定状态: 1:已绑定，2：已解绑")
    private Integer status ;

    @ApiModelProperty("预留手机号")
    private String telephone ;

    @ApiModelProperty("验证渠道。1:安全验证服务，2：通联快捷支付,3 : 升级通联快捷支付")
    private Integer verifyChannel ;

    @ApiModelProperty("验证时间")
    private String verifyTime ;

    @ApiModelProperty("钱包ID")
    private Long walletId ;


}

