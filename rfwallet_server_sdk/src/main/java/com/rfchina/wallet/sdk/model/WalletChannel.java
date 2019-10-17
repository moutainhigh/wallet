package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* WalletChannel
*/
@Data
public class WalletChannel  {
    @ApiModelProperty("银行余额")
    private Long balance ;

    @ApiModelProperty("业务用户标识")
    private String bizUserId ;

    @ApiModelProperty("渠道类型。1: 浦发银企直连，2：通联云商通")
    private Integer channelType ;

    @ApiModelProperty("银行用户标识")
    private String channelUserId ;

    @ApiModelProperty("审核时间")
    private String checkTime ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("失败原因")
    private String failReason ;

    @ApiModelProperty("id")
    private Long id ;

    @ApiModelProperty("签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议")
    private Integer isSignContact ;

    @ApiModelProperty("银行用户类型。2：企业会员 3：个人会员")
    private Integer memberType ;

    @ApiModelProperty("审核图片地址")
    private String picUrl ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("安全手机")
    private String securityTel ;

    @ApiModelProperty("资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败")
    private Integer status ;

    @ApiModelProperty("钱包id")
    private Long walletId ;


}

