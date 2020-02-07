package com.rfchina.wallet.server.model.ext;

import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.model.StatChargingDetail;
import com.rfchina.wallet.server.msic.EnumYunst;
import io.swagger.annotations.ApiModelProperty;

public class StatChargingDetailVo extends StatChargingDetail {

	@ApiModelProperty(name="event", value = "事件")
	public String getEvent() {
		EnumYunst.YunstMethodName name = EnumUtil
			.parse(EnumYunst.YunstMethodName.class, super.getMethodName());
		return name != null ? name.getValueName().replace("代收","支付")
				.replace("代付","收入"): null;
	}

}
