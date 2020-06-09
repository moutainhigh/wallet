package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class WalletVo {

	@ApiModelProperty(name = "id", value = "钱包ID")
	private Long id;

	@ApiModelProperty(name = "type", value = "钱包类型， 1：企业钱包，2：个人钱包")
	private Byte type;

	@ApiModelProperty(name = "title", value = "钱包标题，通常是姓名或公司名")
	private String title;

	@ApiModelProperty(name = "wallet_balance", value = "钱包余额")
	private Long walletBalance;

	@ApiModelProperty(name = "recharge_amount", value = "累计充值金额")
	private Long rechargeAmount;

	@ApiModelProperty(name = "recharge_count", value = "累计充值次数")
	private Integer rechargeCount;

	@ApiModelProperty(name = "pay_amount", value = "累计支付金额")
	private Long payAmount;

	@ApiModelProperty(name = "pay_count", value = "累计支付次数")
	private Integer payCount;

	@ApiModelProperty(name = "status", value = "钱包状态: 1:待激活，2：激活,3：禁用")
	private Byte status;

	@ApiModelProperty(name = "audit_type", value = "审核方式，1：运营，2：银企直连，4：通联")
	private Byte auditType;

	@ApiModelProperty(name = "balance_upd_time", value = "钱包余额最后更新日期")
	private Date balanceUpdTime;

	@ApiModelProperty(name = "create_time", value = "创建日期")
	private Date createTime;

	@ApiModelProperty(name = "last_upd_time", value = "钱包信息最后更新日期")
	private Date lastUpdTime;

	@ApiModelProperty(name = "level", value = "钱包等级，1： 初级钱包，2： 高级钱包")
	private Byte level;

	@ApiModelProperty(name = "source", value = "钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
	private Byte source;

	@ApiModelProperty(name = "freeze_amount", value = "冻结金额")
	private Long freezeAmount;

	@ApiModelProperty(name = "progress", value = "钱包进度，组合字段 1:通道已注册会员 2:通道已绑定手机 4:通道已实名 8:通道已签约 16:已设置支付密码 32:已绑卡")
	private Integer progress;

	@ApiModelProperty(name = "biz_user_id", value = "通道会员id")
	private String bizUserId;

	@ApiModelProperty(name="tunnel_user_id", value = "银行用户标识")
	private String tunnelUserId;
}
