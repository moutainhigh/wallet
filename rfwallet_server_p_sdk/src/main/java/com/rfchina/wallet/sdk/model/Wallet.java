package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* Wallet
*/
@Data
public class Wallet  {
    @ApiModelProperty("审核方式，1：运营，2：银企直连，4：通联")
    private Integer auditType ;

    @ApiModelProperty("钱包余额最后更新日期")
    private String balanceUpdTime ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("冻结金额")
    private Long freezeAmount ;

    @ApiModelProperty("钱包ID")
    private Long id ;

    @ApiModelProperty("钱包信息最后更新日期")
    private String lastUpdTime ;

    @ApiModelProperty("钱包等级，1： 初级钱包，2： 高级钱包")
    private Integer level ;

    @ApiModelProperty("累计支付金额")
    private Long payAmount ;

    @ApiModelProperty("累计支付次数")
    private Integer payCount ;

    @ApiModelProperty("钱包进度，组合字段 1:通道已注册会员 2:通道已绑定手机 4:通道已实名 8:通道已签约 16:已设置支付密码 32:已绑卡")
    private Integer progress ;

    @ApiModelProperty("累计充值金额")
    private Long rechargeAmount ;

    @ApiModelProperty("累计充值次数")
    private Integer rechargeCount ;

    @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
    private Integer source ;

    @ApiModelProperty("钱包状态: 1:待激活，2：激活,3：禁用")
    private Integer status ;

    @ApiModelProperty("钱包标题，通常是姓名或公司名")
    private String title ;

    @ApiModelProperty("钱包类型， 1：企业钱包，2：个人钱包")
    private Integer type ;

    @ApiModelProperty("钱包余额")
    private Long walletBalance ;


}

