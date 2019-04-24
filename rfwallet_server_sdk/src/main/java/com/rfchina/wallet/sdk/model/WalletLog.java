package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletLog
*/
@Data
public class WalletLog  {
    @ApiModelProperty("受理编号")
    private String acceptNo ;

    @ApiModelProperty("流水金额")
    private Long amount ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("电子凭证号")
    private String elecChequeNo ;

    @ApiModelProperty("错误信息")
    private String errMsg ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("收款方帐号")
    private String payeeAccount ;

    @ApiModelProperty("收款账户类型，1：对公账户，2：个人账户")
    private Integer payeeType ;

    @ApiModelProperty("付款方帐号")
    private String payerAccount ;

    @ApiModelProperty("关联接口方法，1：银企直连AQ52，2：银企直连8800")
    private Integer refMethod ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销")
    private Integer status ;

    @ApiModelProperty("流水类型，1：直接转帐，2：收入，3：支出")
    private Integer type ;

    @ApiModelProperty("钱包ID")
    private Long walletId ;


}

