package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* UserResponse
*/
@Data
public class UserResponse  {
    @ApiModelProperty("创建时间")
    private String createTime ;

    @ApiModelProperty("摘要类型,1:商家摘要,2:社区摘要")
    private Integer digestType ;

    @ApiModelProperty("联系邮箱")
    private String email ;

    @ApiModelProperty("主键id")
    private Long id ;

    @ApiModelProperty("")
    private String lastActiveIp ;

    @ApiModelProperty("")
    private String lastActiveTime ;

    @ApiModelProperty("最后更新时间")
    private String lastUpdTime ;

    @ApiModelProperty("所属商户草稿")
    private Long mchDraftId ;

    @ApiModelProperty("所属商户ID")
    private String mchId ;

    @ApiModelProperty("联系手机")
    private String mobile ;

    @ApiModelProperty("用户昵称, 姓名")
    private String nickname ;

    @ApiModelProperty("提供给外部应用的USERID")
    private Long outerId ;

    @ApiModelProperty("父账号, 0: 主账号, 其它: 某主账号下的子账号, 默认为0")
    private Long parentId ;

    @ApiModelProperty("注册完成进度, MCH_INFO :待完成商家资料, BINDING_MOBILE_OR_MAIL :待绑定手机或邮箱, HOME: 已完成, 待渠道审批: WAIT_AUDIT,  待签订协议: WAIT_SIGN_CONTACT")
    private String regProgress ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("角色ID, 主账号默认为超级权限, 子账号一定要关联角色, 默认为0")
    private Integer roleId ;

    @ApiModelProperty("")
    private String roleName ;

    @ApiModelProperty("账号状态, 1:正常, 2:冻结")
    private Integer status ;

    @ApiModelProperty("联系电话")
    private String tel ;

    @ApiModelProperty("用户账号")
    private String uname ;


}

