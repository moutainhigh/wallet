package com.rfchina.wallet.server.model.ext;

import com.rfchina.platform.biztool.mapper.string.StringIndex;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class WalletOrderVo {

	@StringIndex(value = 1, title = "订单号")
	@ApiModelProperty(name = "order_no", value = "订单号")
	private String orderNo;

	@StringIndex(value = 2, title = "钱包批次号")
	@ApiModelProperty(name = "batch_no", value = "钱包批次号")
	private String batchNo;

	@StringIndex(value = 3, title = "业务凭证号")
	@ApiModelProperty(name = "biz_no", value = "业务凭证号")
	private String bizNo;

	@StringIndex(value = 4, title = "钱包id")
	@ApiModelProperty(name = "wallet_id", value = "钱包id")
	private Long walletId;

	@StringIndex(value = 5, title = "类型")
	@ApiModelProperty(name = "type", value = "类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费")
	private Byte type;

	@StringIndex(value = 6, title = "支付方式")
	@ApiModelProperty(name = "pay_method", value = "支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡")
	private Byte payMethod;

	@StringIndex(value = 7, title = "金额")
	@ApiModelProperty(name = "amount", value = "金额")
	private Long amount;

	@StringIndex(value = 8, title = "交易状态")
	@ApiModelProperty(name = "status", value = "交易状态。 2：进行中，3：交易成功，4：交易失败")
	private Byte status;

	@StringIndex(value = 9, title = "备注")
	@ApiModelProperty(name = "note", value = "备注，最长1024")
	private String note;


	@StringIndex(value = 10, title = "用户端错误提示")
	@ApiModelProperty(name = "user_err_msg", value = "用户端错误提示")
	private String userErrMsg;

	@StringIndex(value = 11, title = "结束时间")
	@ApiModelProperty(name = "end_time", value = "结束时间")
	private Date endTime;

	@StringIndex(value = 12, title = "创建日期")
	@ApiModelProperty(name = "create_time", value = "创建日期")
	private Date createTime;

	@StringIndex(value = 13, title = "来源APPID")
	@ApiModelProperty(name = "source_app_id", value = "来源APPID")
	private Long sourceAppId;
}
