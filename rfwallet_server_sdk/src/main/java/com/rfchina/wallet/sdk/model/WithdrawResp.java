package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WithdrawResp
*/
@Data
public class WithdrawResp  {
    @ApiModelProperty("金额")
    private Long amount ;

    @ApiModelProperty("钱包批次号")
    private String batchNo ;

    @ApiModelProperty("业务凭证号")
    private String bizNo ;

    @ApiModelProperty("业务标识。1: 有退款 2: 已记流水")
    private Integer bizTag ;

    @ApiModelProperty("商户系统用户标识")
    private String bizUserId ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("当前尝试次数")
    private Integer currTryTimes ;

    @ApiModelProperty("结束时间")
    private String endTime ;

    @ApiModelProperty("过期时间")
    private String expireTime ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("行业代码（由渠道分配")
    private String industryCode ;

    @ApiModelProperty("行业名称（由渠道分配）")
    private String industryName ;

    @ApiModelProperty("1:未锁 2：锁定")
    private Integer locked ;

    @ApiModelProperty("附言,长度由通道确定")
    private String note ;

    @ApiModelProperty("1:已通知技术 2:已通知业务")
    private Integer notified ;

    @ApiModelProperty("订单号")
    private String orderNo ;

    @ApiModelProperty("支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡")
    private Integer payMethod ;

    @ApiModelProperty("进度。1：待发送 2：已发送 3：已接收结果")
    private Integer progress ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("密码确认时输入参数")
    private String signedParams ;

    @ApiModelProperty("开始时间")
    private String startTime ;

    @ApiModelProperty("交易状态。 2：进行中，3：交易成功，4：交易失败")
    private Integer status ;

    @ApiModelProperty("0：默认 1:待人工处理 2:等待重新发起")
    private Integer subStatus ;

    @ApiModelProperty("业务票据")
    private String ticket ;

    @ApiModelProperty("通道错误码")
    private String tunnelErrCode ;

    @ApiModelProperty("系统错误信息")
    private String tunnelErrMsg ;

    @ApiModelProperty("渠道订单号")
    private String tunnelOrderNo ;

    @ApiModelProperty("通道状态")
    private String tunnelStatus ;

    @ApiModelProperty("通道成功时间")
    private String tunnelSuccTime ;

    @ApiModelProperty("通道类型。1: 浦发银企直连，2：通联云商通")
    private Integer tunnelType ;

    @ApiModelProperty("类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,")
    private Integer type ;

    @ApiModelProperty("用户端错误提示")
    private String userErrMsg ;

    @ApiModelProperty("钱包id")
    private Long walletId ;


}

