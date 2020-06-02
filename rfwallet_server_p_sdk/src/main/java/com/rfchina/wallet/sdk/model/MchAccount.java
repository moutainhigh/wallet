package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchAccount
*/
@Data
public class MchAccount  {
    @ApiModelProperty("账号ID, 秒级时间戳取后五位，加三位随机码")
    private String accountId ;

    @ApiModelProperty("确认结算状态, 0:业务发起确认, 1:系统自动确认")
    private Integer confirmSettleStatus ;

    @ApiModelProperty("账号创建时间")
    private String createTime ;

    @ApiModelProperty("账号说明")
    private String desc ;

    @ApiModelProperty("最后一次结算日")
    private String lastSettleTime ;

    @ApiModelProperty("商户ID")
    private String mchId ;

    @ApiModelProperty("是否存在进行中的结算申请, 1: 存在进行中的结算, 2: 不存在进行中的结算")
    private Integer settle ;

    @ApiModelProperty("结算类型:1-银行卡 2-微众钱包 3-富力钱包")
    private Integer settleType ;

    @ApiModelProperty("防伪签名, 加密字串: account_id+mch_id+settle+last_settle_time(yyyyMMddHHmmss)+create_time(yyyyMMddHHmmss)")
    private String sign ;

    @ApiModelProperty("账号状态, 1:正常, 2:冻结")
    private Integer status ;


}

