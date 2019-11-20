package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* PaginationWalletOrder
*/
@Data
public class PaginationWalletOrder  {
    @ApiModelProperty("")
    private List<WalletOrder> list ;

    @ApiModelProperty("")
    private Integer pageLimit ;

    @ApiModelProperty("")
    private Long pageNum ;

    @ApiModelProperty("")
    private Long total ;

    @ApiModelProperty("")
    private Long totalPage ;


}

