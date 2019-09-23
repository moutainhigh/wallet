package com.rfchina.wallet.server.bank.yunst.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder = true, builderMethodName = "builder$")
@Data
@ToString(callSuper = true)
@ApiModel(description = "4.1.7 设置企业信息")
public class YunstSetCompanyInfoReq implements YunstBaseReq {
	private static final long serialVersionUID = -6990184494075116217L;
	@ApiModelProperty(value = "商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)", required = true)
	private String bizUserId;
	@ApiModelProperty(value = "是否进行线上认证 true:系统自动审核 false:人工审核", required = true)
	private Boolean isAuth = true;
	@ApiModelProperty(value = "企业会员审核结果通知回调地址", required = false)
	private String backUrl;
	@ApiModelProperty(value = "企业基本信息", required = true)
	private CompanyBasicInfo companyBasicInfo;
	@ApiModelProperty(value = "企业扩展信息 (目前不需要传)", required = false)
	private CompanyExtendInfo companyExtendInfo;

	@Override
	public String getServcieName() {
		return "MemberService";
	}

	@Override
	public String getMethodName() {
		return "setCompanyInfo";
	}

	@Data
	@Builder
	public static class CompanyBasicInfo {
		@ApiModelProperty(value = "企业名称", required = true)
		private String companyName;
		@ApiModelProperty(value = "企业地址", required = true)
		private String companyAddress;
		@ApiModelProperty(value = "认证类型 1-三证,2-一证 默认1-三证", required = true)
		private Long authType = 1L;
		@ApiModelProperty(value = "统一社会信用 一证时必传", required = false)
		private String uniCredit;
		@ApiModelProperty(value = "营业执照号 三证时必传", required = false)
		private String businessLicense;
		@ApiModelProperty(value = "组织机构代码 三证时必传", required = false)
		private String organizationCode;
		@ApiModelProperty(value = "税务登记证 三证时必传", required = false)
		private String taxRegister;
		@ApiModelProperty(value = "统一社会信用/营业执照号到期时间,格式:yyyy-MM-dd", required = false)
		private String expLicense;
		@ApiModelProperty(value = "联系电话", required = false)
		private String telephone;
		@ApiModelProperty(value = "法人姓名", required = true)
		private String legalName;
		@ApiModelProperty(value = "法人证件类型", required = true)
		private Long identityType;
		@ApiModelProperty(value = "法人证件号码(RSA 加密)", required = true)
		private String legalIds;
		@ApiModelProperty(value = "法人手机号码", required = true)
		private String legalPhone;
		@ApiModelProperty(value = "企业对公账户(RSA 加密)", required = true)
		private String accountNo;
		@ApiModelProperty(value = "开户行名称", required = true)
		private String parentBankName;
		@ApiModelProperty(value = "开户行地区码", required = false)
		private String bankCityNo;
		@ApiModelProperty(value = "开户行支行名称", required = true)
		private String bankName;
		@ApiModelProperty(value = "支行行号", required = true)
		private String unionBank;
		@ApiModelProperty(value = "开户行所在省", required = true)
		private String province;
		@ApiModelProperty(value = "开户行所在市", required = true)
		private String city;

	}

	public static class CompanyExtendInfo {

	}
}
