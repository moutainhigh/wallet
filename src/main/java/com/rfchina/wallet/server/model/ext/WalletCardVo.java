package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.server.util.MaskUtil;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class WalletCardVo {

	@ApiModelProperty(name="id", value = "ID")
	private Long id;

	@ApiModelProperty(name="wallet_id", value = "钱包ID")
	private Long walletId;

	@ApiModelProperty(name="bank_code", value = "银行代码")
	private String bankCode;

	@ApiModelProperty(name="bank_name", value = "银行名称")
	private String bankName;

	@ApiModelProperty(name="bank_account", value = "银行账号")
	private String bankAccount;

	@ApiModelProperty(name="deposit_bank", value = "开户支行")
	private String depositBank;

	@ApiModelProperty(name="deposit_name", value = "开户名")
	private String depositName;

	@ApiModelProperty(name="status", value = "绑定状态: 1:已绑定，2：已解绑")
	private Byte status;

	@ApiModelProperty(name="is_def", value = "是否默认银行卡: 1:是，2：否")
	private Byte isDef;

	@ApiModelProperty(name="is_public", value = "是否对公账户: 1:是，2：否")
	private Byte isPublic;

	@ApiModelProperty(name="telephone", value = "预留手机号")
	private String telephone;

	@ApiModelProperty(name="create_time", value = "创建日期")
	private Date createTime;

	public String getBankAccount(){
		return MaskUtil.maskBankAccount(bankAccount);
	}

	public String getTelephone(){
		return MaskUtil.maskMobile(telephone);
	}
}
