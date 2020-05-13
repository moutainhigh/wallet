package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletOwner
*/
@Data
public class WalletOwner  {
    @ApiModelProperty("是否删除 0：正常 1：已删除")
    private Integer deleted ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("所有者id")
    private String ownerId ;

    @ApiModelProperty("所有者类型,1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
    private Integer ownerType ;

    @ApiModelProperty("钱包id")
    private Long walletId ;


}

