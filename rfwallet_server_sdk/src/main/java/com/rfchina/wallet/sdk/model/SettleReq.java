package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* SettleReq
*/
@Data
public class SettleReq  {
    @ApiModelProperty("收款人列表")
    private List<Reciever> receivers ;


}

