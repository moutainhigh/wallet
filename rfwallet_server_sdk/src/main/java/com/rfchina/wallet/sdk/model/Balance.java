package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* Balance
*/
@Data
public class Balance  {
    @ApiModelProperty("渠道出资额(单位分)")
    private Long amount ;


}

