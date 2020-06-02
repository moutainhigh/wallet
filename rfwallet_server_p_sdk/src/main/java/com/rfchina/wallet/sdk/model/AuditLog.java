package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* AuditLog
*/
@Data
public class AuditLog  {
    @ApiModelProperty("充值总额")
    private Long amount ;

    @ApiModelProperty("申请用户")
    private String applyUser ;

    @ApiModelProperty("审批人账号")
    private String auditAccount ;

    @ApiModelProperty("审批人名称")
    private String auditName ;

    @ApiModelProperty("申请状态, 1:资料待完善, 2:商户提交审核,3:支付中心运营审批,4:支付中心负责人审批,5:财务确认, 6:审核成功, 7:审核失败")
    private Integer auditStatus ;

    @ApiModelProperty("审核时间")
    private String auditTime ;

    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("")
    private Long id ;

    @ApiModelProperty("商户ID,三位随机码+秒级时间戳取后五位")
    private String mchId ;

    @ApiModelProperty("商户名称")
    private String mchName ;

    @ApiModelProperty("营销账户")
    private String outlayAccountId ;

    @ApiModelProperty("申请状态, 1:资料待完善, 2:商户提交审核,3:支付中心运营审批,4:支付中心负责人审批,5:财务确认, 6:审核成功, 7:审核失败")
    private Integer preStatus ;

    @ApiModelProperty("业务ID（充值申请id或其他）")
    private String refId ;

    @ApiModelProperty("审批类型, 1:充值")
    private Integer refType ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("角色账号")
    private String roleAccount ;

    @ApiModelProperty("角色名称")
    private String roleName ;


}

