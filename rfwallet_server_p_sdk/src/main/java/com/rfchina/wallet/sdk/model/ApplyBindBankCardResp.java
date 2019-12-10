package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* ApplyBindBankCardResp
*/
@Data
public class ApplyBindBankCardResp  {
    @ApiModelProperty("银行代码")
    private String bankCode ;

    @ApiModelProperty("银行名称")
    private String bankName ;

    @ApiModelProperty("商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)")
    private String bizUserId ;

    @ApiModelProperty("银行卡类型 1-储蓄卡 2-信用卡")
    private Long cardType ;

    @ApiModelProperty("流水号")
    private String tranceNum ;

    @ApiModelProperty("申请时间 YYYYMMDD")
    private String transDate ;


}

