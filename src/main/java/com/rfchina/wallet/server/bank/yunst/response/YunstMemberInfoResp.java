package com.rfchina.wallet.server.bank.yunst.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YunstMemberInfoResp {
	@ApiModelProperty(value = "错误信息")
	private String errorMsg;
	@ApiModelProperty(value = "结果数据")
	private Object data;

	@Data
	public static class PersonInfoResult{
		@ApiModelProperty(value = "云商通用户唯一标识")
		private String userId;
		@ApiModelProperty(value = "商户系统用户标识(个人用户:U+userId,企业用户:C+mchId)")
		private String bizUserId;
	}

	@Data
	public static class CompanyInfoResult{
		@ApiModelProperty(value = "企业名称")
		private String companyName;
		@ApiModelProperty(value = "企业地址")
		private String companyAddress;
		@ApiModelProperty(value = "认证类型(三证或一证)")
		private Long authType;
		@ApiModelProperty(value = "营业执照号(三证)")
		private String businessLicense;
		@ApiModelProperty(value = "组织机构代码(三证)")
		private String organizationCode;
		@ApiModelProperty(value = "统一社会信用(一证)")
		private String uniCredit;
		@ApiModelProperty(value = "税务登记证(三证)")
		private String taxRegister;
		@ApiModelProperty(value = "统一社会信用/营业执照号到期时间 格式:yyyy-MM-dd")
		private String expLicense;
		@ApiModelProperty(value = "联系电话")
		private String telephone;
		@ApiModelProperty(value = "手机号码")
		private String phone;
		@ApiModelProperty(value = "法人姓名")
		private String legalName;
		@ApiModelProperty(value = "法人证件类型")
		private Long identityType;
		@ApiModelProperty(value = "法人证件号码")
		private String legalIds;
		@ApiModelProperty(value = "法人手机号码")
		private String legalPhone;
		@ApiModelProperty(value = "企业对公账户账号")
		private String accountNo;
		@ApiModelProperty(value = "开户银行名称")
		private String parentBankName;
		@ApiModelProperty(value = "开户行地区代码")
		private String bankCityNo;
		@ApiModelProperty(value = "开户行支行名称")
		private String bankName;
		@ApiModelProperty(value = "支付行号,12位数字")
		private String unionBank;
		@ApiModelProperty(value = "开户行所在省")
		private String province;
		@ApiModelProperty(value = "开户行所在市")
		private String city;
	}

}
