package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchDraftVo
*/
@Data
public class MchDraftVo  {
    @ApiModelProperty("收款帐号数量限制")
    private Integer accountLimit ;

    @ApiModelProperty("申请人")
    private String applicant ;

    @ApiModelProperty("申请时间")
    private String applicationTime ;

    @ApiModelProperty("审批信息")
    private String auditMsg ;

    @ApiModelProperty("审批时间")
    private String auditTime ;

    @ApiModelProperty("审批人")
    private String auditor ;

    @ApiModelProperty("营业地址")
    private String businessAddress ;

    @ApiModelProperty("营业执照号，数字编号")
    private String businessLicense ;

    @ApiModelProperty("营业执照图片")
    private String businessLicensePic ;

    @ApiModelProperty("修改资料申请函图片")
    private String changeApplyPic ;

    @ApiModelProperty("创建日期")
    private String createTime ;

    @ApiModelProperty("草稿类型, 1:注册类型草稿, 2:修改类型草稿")
    private Integer draftType ;

    @ApiModelProperty("是否启用钱包结算 1-启用 2-禁用")
    private Integer enableWalletSettle ;

    @ApiModelProperty("错误信息")
    private String errMsg ;

    @ApiModelProperty("主键id")
    private Long id ;

    @ApiModelProperty("法人身份证号")
    private String legalPerIdNo ;

    @ApiModelProperty("法人证件反面照")
    private String legalPerIdOpposite ;

    @ApiModelProperty("法人证件正面照")
    private String legalPerIdPositive ;

    @ApiModelProperty("法人证件类型")
    private String legalPerIdType ;

    @ApiModelProperty("企业法人")
    private String legalPerName ;

    @ApiModelProperty("法人手机号")
    private String legalPerTelephoneNo ;

    @ApiModelProperty("商户ID,三位随机码+秒级时间戳取后五位")
    private String mchId ;

    @ApiModelProperty("商户名称")
    private String mchName ;

    @ApiModelProperty("商户对公账户草稿列表")
    private List<MchPublicDraft> mchPublicDraftList ;

    @ApiModelProperty("组织机构代码")
    private String organizationCode ;

    @ApiModelProperty("责任人邮箱")
    private String respPerEmail ;

    @ApiModelProperty("责任人证件号")
    private String respPerIdNo ;

    @ApiModelProperty("责任人证件反面照")
    private String respPerIdOpposite ;

    @ApiModelProperty("责任人证件正面照")
    private String respPerIdPositive ;

    @ApiModelProperty("责任人证件类型")
    private String respPerIdType ;

    @ApiModelProperty("责任人姓名")
    private String respPerName ;

    @ApiModelProperty("责任人手机")
    private String respPerTelephoneNo ;

    @ApiModelProperty("商户结算账号类型, 1: 银行账号, 2: 微众钱包账号,3:富力钱包账号")
    private Integer settleType ;

    @ApiModelProperty("来源appid")
    private String sourceAppId ;

    @ApiModelProperty("审批状态, 1:填写中 2:审批中, 3:审批通过, 4:审批失败, 5: 用户撤回修改 6:运营审批通过")
    private Integer status ;

    @ApiModelProperty("商户电话")
    private String tel ;

    @ApiModelProperty("税务登记证件号")
    private String texRegistrationNo ;

    @ApiModelProperty("商户类型, 1:公司, 2:个人")
    private Integer type ;

    @ApiModelProperty("统一社会信用")
    private String uniCredit ;


}

