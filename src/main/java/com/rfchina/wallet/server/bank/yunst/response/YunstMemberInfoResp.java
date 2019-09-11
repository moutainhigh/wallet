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
	public static class MemberInfoResult {
		@ApiModelProperty(value = "会员信息")
		private Object memberInfo;
	}

	@Data
	public static class PersonInfoResult {
		@ApiModelProperty(value = "姓名")
		private String name;
		@ApiModelProperty(value = "地址")
		private String address;
		@ApiModelProperty(value = "用户状态")
		private Long userState;
		@ApiModelProperty(value = "云商通用户id")
		private String userId;
		@ApiModelProperty(value = "国家")
		private String country;
		@ApiModelProperty(value = "省份")
		private String province;
		@ApiModelProperty(value = "县市")
		private String area;
		@ApiModelProperty(value = "手机号码")
		private String phone;
		@ApiModelProperty(value = "身份证号码")
		private String identityCardNo;
		@ApiModelProperty(value = "是否绑定手机")
		private Boolean isPhoneChecked;
		@ApiModelProperty(value = "创建时间")
		private String registerTime;
		@ApiModelProperty(value = "创建ip")
		private String registerIp;
		@ApiModelProperty(value = "支付失败次数")
		private String payFailAmount;
		@ApiModelProperty(value = "是否进行实名认证")
		private String isIdentityChecked;
		@ApiModelProperty(value = "实名认证时间")
		private String realNameTime;
		@ApiModelProperty(value = "备注")
		private String remark;
		@ApiModelProperty(value = "访问终端类型")
		private Long source;
		@ApiModelProperty(value = "是否已设置支付密码")
		private String isSetPayPwd;
		@ApiModelProperty(value = "是否已签电子协议")
		private String isSignContract;
	}

	@Data
	public static class CompanyInfoResult {
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
		@ApiModelProperty(value = "是否已签电子协议")
		private Boolean isSignContract;
		@ApiModelProperty(value = "审核状态")
		private Long status;
		@ApiModelProperty(value = "审核时间")
		private String checkTime;
		@ApiModelProperty(value = "备注")
		private String remark;
		@ApiModelProperty(value = "审核失败原因")
		private String failReason;

	}

}
