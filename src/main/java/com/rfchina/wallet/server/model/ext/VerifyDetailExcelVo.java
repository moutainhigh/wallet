package com.rfchina.wallet.server.model.ext;

import com.rfchina.biztools.functional.Optionals;
import com.rfchina.platform.biztool.excel.PoiColumn;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayInvokeStatus;
import com.rfchina.wallet.server.msic.EnumYunst;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class VerifyDetailExcelVo {

	@PoiColumn(idx = 0, title = "用户姓名")
	@ApiModelProperty(name = "name", value = "用户名")
	private String name;

	@PoiColumn(idx = 1, title = "钱包ID")
	@ApiModelProperty(name = "wallet_id", value = "钱包id")
	private Long walletId;

	@PoiColumn(idx = 2, title = "会员编号")
	@ApiModelProperty(name="biz_user_id", value = "业务用户标识")
	private String bizUserId;

	@PoiColumn(idx = 3, title = "服务类别")
	@ApiModelProperty(name = "event", value = "事件")
	public String getEvent() {
		EnumYunst.YunstMethodName name = EnumUtil
			.parse(EnumYunst.YunstMethodName.class, methodName);
		return name != null ? name.getValueName().replace("代收", "支付")
			.replace("代付", "收入") : null;
	}

	@PoiColumn(idx = 4, title = "认证时间")
	private String getBizTimeStr() {
		return Optionals.select(bizTime != null,
			() -> DateUtil.formatDate(bizTime, DateUtil.STANDARD_DTAETIME_PATTERN), null);
	}

	@PoiColumn(idx = 5, title = "认证状态")
	private String getSuccStr() {
		if (isSucc != null) {
			return Optionals.select(GatewayInvokeStatus.SUCC.getValue().byteValue() == isSucc,
				() -> "认证成功", () -> "认证失败");
		}
		return null;
	}


	@ApiModelProperty(name = "is_succ", value = "是否业务成功")
	private Byte isSucc;

	@ApiModelProperty(name = "method_name", value = "方法名")
	private String methodName;

	@ApiModelProperty(name = "biz_time", value = "业务时间")
	private Date bizTime;
}

