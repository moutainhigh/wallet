package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* 4.1.21 会员收银宝渠道商户信息及终端信息绑定
*/
@Data
public class VspTermidResp  {
    @ApiModelProperty("商户系统用户标识，商户系统中唯一编号。")
    private String bizUserId ;

    @ApiModelProperty("绑定、查询收银宝终端号结果")
    private String result ;

    @ApiModelProperty("已绑定收银宝终端号列")
    private List<VspTermid> vspTermidList ;


}

