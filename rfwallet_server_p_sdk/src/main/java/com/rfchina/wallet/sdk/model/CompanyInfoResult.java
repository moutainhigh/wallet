package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* CompanyInfoResult
*/
@Data
public class CompanyInfoResult  {
    @ApiModelProperty("企业对公账户账号")
    private String accountNo ;

    @ApiModelProperty("认证类型(三证或一证)")
    private Long authType ;

    @ApiModelProperty("开户行地区代码")
    private String bankCityNo ;

    @ApiModelProperty("开户行支行名称")
    private String bankName ;

    @ApiModelProperty("营业执照号(三证)")
    private String businessLicense ;

    @ApiModelProperty("审核时间")
    private String checkTime ;

    @ApiModelProperty("开户行所在市")
    private String city ;

    @ApiModelProperty("企业地址")
    private String companyAddress ;

    @ApiModelProperty("企业名称")
    private String companyName ;

    @ApiModelProperty("统一社会信用/营业执照号到期时间 格式:yyyy-MM-dd")
    private String expLicense ;

    @ApiModelProperty("审核失败原因")
    private String failReason ;

    @ApiModelProperty("法人证件类型")
    private Long identityType ;

    @ApiModelProperty("是否已签电子协议")
    private Boolean isSignContract ;

    @ApiModelProperty("法人证件号码")
    private String legalIds ;

    @ApiModelProperty("法人姓名")
    private String legalName ;

    @ApiModelProperty("法人手机号码")
    private String legalPhone ;

    @ApiModelProperty("组织机构代码(三证)")
    private String organizationCode ;

    @ApiModelProperty("开户银行名称")
    private String parentBankName ;

    @ApiModelProperty("手机号码")
    private String phone ;

    @ApiModelProperty("开户行所在省")
    private String province ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("审核状态")
    private Long status ;

    @ApiModelProperty("税务登记证(三证)")
    private String taxRegister ;

    @ApiModelProperty("联系电话")
    private String telephone ;

    @ApiModelProperty("统一社会信用(一证)")
    private String uniCredit ;

    @ApiModelProperty("支付行号,12位数字")
    private String unionBank ;


}

