package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchPublicDraft
*/
@Data
public class MchPublicDraft  {
    @ApiModelProperty("地区id")
    private String areaId ;

    @ApiModelProperty("审批类型 ，0:无需审批；1:通联审批")
    private Integer auditType ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("开户行名称")
    private String depositBank ;

    @ApiModelProperty("开户行市")
    private String depositBankCity ;

    @ApiModelProperty("开户行id")
    private String depositBankId ;

    @ApiModelProperty("开户行省")
    private String depositBankProvince ;

    @ApiModelProperty("草稿ID")
    private Long draftId ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("对公账户名")
    private String publicAccount ;

    @ApiModelProperty("开户名")
    private String publicAccountName ;

    @ApiModelProperty("对公账户证明")
    private String publicAccountPic ;

    @ApiModelProperty("对公账户id，可空")
    private Long publicId ;

    @ApiModelProperty("支行名称")
    private String subBranchBank ;

    @ApiModelProperty("支付行号")
    private String unionBank ;

    @ApiModelProperty("最后更新时间")
    private String updateTime ;

    @ApiModelProperty("钱包id，可空")
    private Long walletId ;


}

