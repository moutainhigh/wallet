package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* Order
*/
@Data
public class Order  {
    @ApiModelProperty("业务类型, 1:余额账号;2:积分业务;现金券业务")
    private Integer bizType ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("优惠金额")
    private Long discountAmount ;

    @ApiModelProperty("状态, 1:扣款成功;2:扣款失败")
    private Integer discountStatus ;

    @ApiModelProperty("")
    private String id ;

    @ApiModelProperty("最后更新日期")
    private String lastUpdateTime ;

    @ApiModelProperty("营销类型, 1:零售;2:套餐;3:组合套餐")
    private Integer marketType ;

    @ApiModelProperty("商户ID,三位随机码+秒级时间戳取后五位")
    private String mchId ;

    @ApiModelProperty("订单金额")
    private Long orderAmount ;

    @ApiModelProperty("应用订单号或标识")
    private String outerBillId ;

    @ApiModelProperty("营销账号")
    private String outlayAccountId ;

    @ApiModelProperty("套餐内商品数量")
    private Integer packageCount ;

    @ApiModelProperty("套餐ID")
    private Long packageId ;

    @ApiModelProperty("套餐名称")
    private String packageName ;

    @ApiModelProperty("套餐单价")
    private Long packagePrice ;

    @ApiModelProperty("购买数量")
    private Long purchaseCount ;

    @ApiModelProperty("购买用户")
    private String purchaseUser ;

    @ApiModelProperty("购买人姓名")
    private String purchaseUserName ;

    @ApiModelProperty("失败原因")
    private String reason ;

    @ApiModelProperty("实收金额")
    private Long receiptAmount ;

    @ApiModelProperty("业务ID")
    private String refBizId ;

    @ApiModelProperty("状态, 1:进行中;2:成功;3:失败")
    private Integer status ;


}

