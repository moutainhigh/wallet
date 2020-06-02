package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* MchVo
*/
@Data
public class MchVo  {
    @ApiModelProperty("收款帐号数量限制")
    private Integer accountLimit ;

    @ApiModelProperty("审批信息")
    private String auditMsg ;

    @ApiModelProperty("审批次数")
    private Integer auditQty ;

    @ApiModelProperty("审批时间")
    private String auditTime ;

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

    @ApiModelProperty("是否可创建企业钱包，0：不可创建；1：可创建")
    private Integer createWalletStatus ;

    @ApiModelProperty("是否启用钱包结算 1-启用 2-禁用")
    private Integer enableWalletSettle ;

    @ApiModelProperty("平台手续费(如果支付中心服务费少于渠道服rfwalletrfpayrfwallet务费, 则支付中心垫付)，单位分")
    private Long feeAmount ;

    @ApiModelProperty("最新平台手续费，与生效时间组合使用")
    private Long feeCommingAmount ;

    @ApiModelProperty("最新手续费生效时间")
    private String feeCommingEffective ;

    @ApiModelProperty("费用级别，1：自定义服务费；2：合约服务费；")
    private Integer feeType ;

    @ApiModelProperty("商户ID,三位随机码+秒级时间戳取后五位")
    private String id ;

    @ApiModelProperty("最后更新时间")
    private String lastUpdTime ;

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

    @ApiModelProperty("商家主体表rf_mch_main_body关联字段")
    private Long mainBodyId ;

    @ApiModelProperty("商家业务类型，1：外部商家，2：内部商家，3：外部直连")
    private Integer mchBizType ;

    @ApiModelProperty("商户名称")
    private String mchName ;

    @ApiModelProperty("对公账户列表")
    private List<MchPublic> mchPublicList ;

    @ApiModelProperty("组织机构代码")
    private String organizationCode ;

    @ApiModelProperty("营销帐号数量限制")
    private Integer outlayLimit ;

    @ApiModelProperty("父商户ID, 0: 一级商户, 其它: 子商户, 默认为0")
    private String parentId ;

    @ApiModelProperty("责任人邮箱")
    private String respPerEmail ;

    @ApiModelProperty("责任人证件号")
    private String respPerIdNo ;

    @ApiModelProperty("责任人证件反面照")
    private String respPerIdOpposite ;

    @ApiModelProperty("责任人证件正面照")
    private String respPerIdPositive ;

    @ApiModelProperty("责任人证件类型，01：身份证")
    private String respPerIdType ;

    @ApiModelProperty("责任人姓名")
    private String respPerName ;

    @ApiModelProperty("责任人手机")
    private String respPerTelephoneNo ;

    @ApiModelProperty("商户结算账号类型, 1: 银行账号, 2: 微众钱包账号,3:富力钱包账号")
    private Integer settleType ;

    @ApiModelProperty("来源appid")
    private String sourceAppId ;

    @ApiModelProperty("商户状态, 1:审批中, 2:审批通过, 3:审批失败, 4:禁用")
    private Integer status ;

    @ApiModelProperty("商户电话")
    private String tel ;

    @ApiModelProperty("税务登记证件号")
    private String texRegistrationNo ;

    @ApiModelProperty("商户类型, 1:公司, 2:个人")
    private Integer type ;

    @ApiModelProperty("统一社会信用")
    private String uniCredit ;

    @ApiModelProperty("钱包结算数量限制，默认1")
    private Integer walletLimit ;


}

