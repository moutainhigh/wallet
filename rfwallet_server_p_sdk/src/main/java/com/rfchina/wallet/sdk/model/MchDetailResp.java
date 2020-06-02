package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchDetailResp
*/
@Data
public class MchDetailResp  {
    @ApiModelProperty("")
    private MchVo mch ;

    @ApiModelProperty("")
    private MchDraftVo mchDraft ;

    @ApiModelProperty("")
    private String sourceAppId ;


}

