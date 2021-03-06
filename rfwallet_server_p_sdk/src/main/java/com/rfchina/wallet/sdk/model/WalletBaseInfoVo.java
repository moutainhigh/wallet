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
public class WalletBaseInfoVo  {
    @ApiModelProperty("钱包主要信息")
    private Wallet wallet ;

    @ApiModelProperty("所有者列表")
    private List<WalletOwner> walletOwnerList ;


}

