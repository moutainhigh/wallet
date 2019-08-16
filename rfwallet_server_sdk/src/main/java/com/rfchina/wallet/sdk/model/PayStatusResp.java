package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* PayStatusResp
*/
@Data
public class PayStatusResp  {
    @ApiModelProperty("支付金额")
    private Long amount ;

    @ApiModelProperty("钱包批次号")
    private String batchNo ;

    @ApiModelProperty("业务凭证号")
    private String bizNo ;

    @ApiModelProperty("银行交易终态时间")
    private String bizTime ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("交易结束时间（浦发只有时分秒，查询成功定为交易结束时间）")
    private String endTime ;

    @ApiModelProperty("错误码")
    private String errCode ;

    @ApiModelProperty("银行发起时间")
    private String lanchTime ;

    @ApiModelProperty("附言(不超过100)")
    private String note ;

    @ApiModelProperty("收款方帐号")
    private String payeeAccount ;

    @ApiModelProperty("收款银行行号")
    private String payeeBankCode ;

    @ApiModelProperty("收款方户名")
    private String payeeName ;

    @ApiModelProperty("收款账户类型，1：对公账户，2：个人账户")
    private Integer payeeType ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销，6：待处理")
    private Integer status ;

    @ApiModelProperty("系统错误信息")
    private String sysErrMsg ;

    @ApiModelProperty("用户错误信息")
    private String userErrMsg ;


}

