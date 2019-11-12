package com.rfchina.wallet.server.model.ext;

import com.rfchina.platform.biztool.mapper.string.StringIndex;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class WalletOrderVo {

	@StringIndex(1)
	@ApiModelProperty(name="order_no", value = "订单号")
	private String orderNo;

	@StringIndex(2)
	@ApiModelProperty(name="batch_no", value = "钱包批次号")
	private String batchNo;

	@StringIndex(3)
	@ApiModelProperty(name="biz_no", value = "业务凭证号")
	private String bizNo;

	@StringIndex(4)
	@ApiModelProperty(name="wallet_id", value = "钱包id")
	private Long walletId;

	@StringIndex(5)
	@ApiModelProperty(name="type", value = "类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费")
	private Byte type;

	@StringIndex(6)
	@ApiModelProperty(name="pay_method", value = "支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡")
	private Byte payMethod;

	@StringIndex(7)
	@ApiModelProperty(name="amount", value = "金额")
	private Long amount;

	@StringIndex(8)
	@ApiModelProperty(name="status", value = "交易状态。 2：进行中，3：交易成功，4：交易失败")
	private Byte status;

	@StringIndex(9)
	@ApiModelProperty(name="note", value = "备注，最长1024")
	private String note;

	@StringIndex(10)
	@ApiModelProperty(name="user_err_msg", value = "用户端错误提示")
	private String userErrMsg;

	@StringIndex(11)
	@ApiModelProperty(name="end_time", value = "结束时间")
	private Date endTime;

	@StringIndex(12)
	@ApiModelProperty(name="create_time", value = "创建日期")
	private Date createTime;

}
