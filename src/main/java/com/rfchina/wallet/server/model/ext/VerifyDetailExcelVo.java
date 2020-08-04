package com.rfchina.wallet.server.model.ext;

import com.rfchina.platform.biztool.excel.PoiColumn;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.server.msic.EnumYunst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VerifyDetailExcelVo {

	@PoiColumn(idx = 0, title = "用户姓名")
	@ApiModelProperty(name = "name", value = "用户名")
	private String name;

	@PoiColumn(idx = 1, title = "钱包ID")
	@ApiModelProperty(name = "wallet_id", value = "钱包id")
	private Long walletId;

	@ApiModelProperty(name = "method_name", value = "方法名")
	private String methodName;

	@PoiColumn(idx = 2, title = "服务类别")
	@ApiModelProperty(name = "event", value = "事件")
	public String getEvent() {
		EnumYunst.YunstMethodName name = EnumUtil
			.parse(EnumYunst.YunstMethodName.class, getMethodName());
		return name != null ? name.getValueName().replace("代收", "支付")
			.replace("代付", "收入") : null;
	}

	@PoiColumn(idx = 3, title = "认证时间")
	@ApiModelProperty("业务时间")
	private String bizTime;

}

