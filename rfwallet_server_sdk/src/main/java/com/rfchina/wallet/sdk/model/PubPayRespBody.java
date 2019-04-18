package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* PubPayRespBody
*/
@Data
public class PubPayRespBody  {
    @ApiModelProperty("受理编号")
    private String acceptNo ;

    @ApiModelProperty("失败笔数")
    private String failCount ;

    @ApiModelProperty("柜员流水号")
    private String seqNo ;

    @ApiModelProperty("成功笔数")
    private String successCount ;


}

