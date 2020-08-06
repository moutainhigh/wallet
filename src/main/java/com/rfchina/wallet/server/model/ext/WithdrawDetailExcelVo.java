package com.rfchina.wallet.server.model.ext;

import com.rfchina.biztools.functional.Optionals;
import com.rfchina.platform.biztool.excel.PoiColumn;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayInvokeStatus;
import com.rfchina.wallet.server.msic.EnumYunst;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import lombok.Data;

@Data
public class WithdrawDetailExcelVo {

	@PoiColumn(idx = 0, title = "明细单号")
	@ApiModelProperty(name = "tunnel_order_no", value = "渠道订单号")
	private String tunnelOrderNo;

	@PoiColumn(idx = 1, title = "钱包ID")
	@ApiModelProperty(name = "wallet_id", value = "钱包id")
	private Long walletId;

	@PoiColumn(idx = 2, title = "会员编号")
	@ApiModelProperty(name="biz_user_id", value = "业务用户标识")
	private String bizUserId;

	@PoiColumn(idx = 3, title = "用户姓名")
	@ApiModelProperty(name = "name", value = "用户名")
	private String name;

	@PoiColumn(idx = 4, title = "交易类型")
	@ApiModelProperty(name = "event", value = "事件")
	public String getEvent() {
		EnumYunst.YunstMethodName name = EnumUtil
			.parse(EnumYunst.YunstMethodName.class, methodName);
		return name != null ? name.getValueName().replace("代收", "支付")
			.replace("代付", "收入") : null;
	}

	@PoiColumn(idx = 5, title = "交易日期")
	@ApiModelProperty("业务时间")
	private String getBizTimeStr() {
		return Optionals.select(bizTime != null,
			() -> DateUtil.formatDate(bizTime, DateUtil.STANDARD_DTAETIME_PATTERN), null);
	}

	@PoiColumn(idx = 6, title = "交易金额")
	private String getAmountStr() {
		if (amount != null) {
			BigDecimal total = BigDecimal.valueOf(amount);
			return total.divide(BigDecimal.valueOf(100))
				.setScale(2, RoundingMode.DOWN)
				.toString();
		}
		return null;
	}

	@PoiColumn(idx = 7, title = "交易状态")
	private String getSuccStr() {
		if (isSucc != null) {
			return Optionals.select(GatewayInvokeStatus.SUCC.getValue().byteValue() == isSucc,
				() -> "交易成功", () -> "交易失败");
		}
		return null;
	}

	@PoiColumn(idx = 8, title = "费用金额")
	private String getFeeStr() {
		if (localTunnelFee != null) {
			BigDecimal fee = BigDecimal.valueOf(localTunnelFee);
			return fee.divide(BigDecimal.valueOf(100))
				.setScale(2, RoundingMode.DOWN)
				.toString();
		}
		return null;
	}

	@PoiColumn(idx = 9, title = "关联交易号")
	@ApiModelProperty(name = "order_no", value = "钱包订单号")
	private String orderNo;

	@ApiModelProperty(name = "method_name", value = "方法名")
	private String methodName;

	@ApiModelProperty(name = "amount", value = "金额")
	private Long amount;

	@ApiModelProperty(name = "local_tunnel_fee", value = "本地的通道手续费")
	private Long localTunnelFee;

	@ApiModelProperty(name = "biz_time", value = "业务时间")
	private Date bizTime;

	@ApiModelProperty(name = "is_succ", value = "是否业务成功")
	private Byte isSucc;


}

