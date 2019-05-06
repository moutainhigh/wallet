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

    @ApiModelProperty("钱包批次号")
    private String batchNo ;

    @ApiModelProperty("业务凭证号")
    private String bizNo ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("当前尝试次数")
    private Integer currTryTimes ;

    @ApiModelProperty("电子凭证号")
    private String elecChequeNo ;

    @ApiModelProperty("交易结束时间（浦发只有时分秒，查询成功定为交易结束时间）")
    private String endTime ;

    @ApiModelProperty("错误信息")
    private String errMsg ;

    @ApiModelProperty("ID")
    private Long id ;

    @ApiModelProperty("最大尝试次数")
    private Integer maxTryTimes ;

    @ApiModelProperty("附言(不超过100)")
    private String note ;

    @ApiModelProperty("支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入")
    private String payPurpose ;

    @ApiModelProperty("收款方帐号")
    private String payeeAccount ;

    @ApiModelProperty("收款账户类型，1：对公账户，2：个人账户")
    private Integer payeeType ;

    @ApiModelProperty("付款方帐号")
    private String payerAccount ;

    @ApiModelProperty("计划查询时间")
    private String queryTime ;

    @ApiModelProperty("关联接口方法，1：银企直连AQ52，2：银企直连8800")
    private Integer refMethod ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("交易流水号")
    private String seqNo ;

    @ApiModelProperty("交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销")
    private Integer status ;

    @ApiModelProperty("流水类型，1：直接转帐，2：收入，3：支出")
    private Integer type ;

    @ApiModelProperty("钱包ID")
    private Long walletId ;


}

