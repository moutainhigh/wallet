package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* 钱包信息，包含企业钱包和个人钱包两种
*/
@Data
public class WalletInfoResp  {
    @ApiModelProperty("银行卡数量")
    private Integer bankCardCount ;

    @ApiModelProperty("钱包公司信息")
    private WalletCompany companyInfo ;

    @ApiModelProperty("钱包默认卡")
    private WalletCard defWalletCard ;

    @ApiModelProperty("钱包个人信息")
    private WalletPerson personInfo ;

    @ApiModelProperty("钱包主要信息")
    private Wallet wallet ;


}

