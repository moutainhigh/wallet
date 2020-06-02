package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* RechargeApplyExt
*/
@Data
public class RechargeApplyExt  {
    @ApiModelProperty("申请时间")
    private String applyTime ;

    @ApiModelProperty("申请用户")
    private String applyUser ;

    @ApiModelProperty("申请人姓名")
    private String applyUserName ;

    @ApiModelProperty("资料图片")
    private String auditPicUrls ;

    @ApiModelProperty("具有审核权限的角色类型")
    private Integer auditRoleType ;

    @ApiModelProperty("余额")
    private Long balance ;

    @ApiModelProperty("当前用户是否具有审批权限")
    private Boolean canAudit ;

    @ApiModelProperty("确认时间")
    private String confirmTime ;

    @ApiModelProperty("确认用户")
    private String confirmUser ;

    @ApiModelProperty("确认人姓名")
    private String confirmUserName ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("")
    private String id ;

    @ApiModelProperty("审核时间")
    private String lastAuditTime ;

    @ApiModelProperty("充值前营销账户余额")
    private Long lastOutlayAmount ;

    @ApiModelProperty("商家业务类型，1：外部商家，2：内部商家，3：外部直连")
    private Integer mchBizType ;

    @ApiModelProperty("商户ID,三位随机码+秒级时间戳取后五位")
    private String mchId ;

    @ApiModelProperty("商户名称")
    private String mchName ;

    @ApiModelProperty("充值后营销账户余额")
    private Long nextOutlayAmount ;

    @ApiModelProperty("应用订单号或标识")
    private String outerBillId ;

    @ApiModelProperty("营销账户")
    private String outlayAccountId ;

    @ApiModelProperty("营销账户名称")
    private String outlayAccountName ;

    @ApiModelProperty("资料图片")
    private String picUrls ;

    @ApiModelProperty("失败原因")
    private String reason ;

    @ApiModelProperty("充值金额")
    private Long rechargeAmount ;

    @ApiModelProperty("充值类型, 1:直充；2:充值转积分;3:充值转优惠券")
    private Integer rechargeType ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("申请状态, 1:资料待完善, 2:运营提交审核,3:支付中心运营审批通过,4:支付中心负责人审批通过,5:财务确认, 6:充值成功, 7:审核失败")
    private Integer status ;


}

