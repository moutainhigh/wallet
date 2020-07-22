package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* PaginationWalletTerminal
*/
@Data
public class PaginationWalletTerminal  {
    @ApiModelProperty("")
    private List<WalletTerminal> list ;

    @ApiModelProperty("")
    private Integer pageLimit ;

    @ApiModelProperty("")
    private Long pageNum ;

    @ApiModelProperty("")
    private Long total ;

    @ApiModelProperty("")
    private Long totalPage ;


}

