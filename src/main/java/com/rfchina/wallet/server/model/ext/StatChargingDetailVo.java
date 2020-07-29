package com.rfchina.wallet.server.model.ext;

import com.rfchina.platform.biztool.excel.PoiColumn;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.model.StatChargingDetail;
import com.rfchina.wallet.server.msic.EnumYunst;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.Data;

@Data
public class StatChargingDetailVo  {

	@PoiColumn(idx = 0, title = "业务时间")
	@ApiModelProperty("业务时间")
	private String bizTime;

	@PoiColumn(idx = 2, title = "业务凭证号")
	@ApiModelProperty("业务凭证号")
	private String bizNo;

	@PoiColumn(idx = 3, title = "钱包订单号")
	@ApiModelProperty("钱包订单号")
	private String orderNo;

	@ApiModelProperty(name="id", value = "id")
	private Long id;

	@ApiModelProperty(name="third_tunnel_fee", value = "第三方的通道手续费")
	private Long thirdTunnelFee;

	@ApiModelProperty(name="local_tunnel_fee", value = "本地的通道手续费")
	private Long localTunnelFee;

	@ApiModelProperty(name="method_name", value = "方法名")
	private String methodName;

	@PoiColumn(idx = 4, title = "本地手续费（元）")
	@ApiModelProperty("本地的通道手续费")
	public String localTunnelFeeString() {
		Long val = Optional.ofNullable(localTunnelFee).orElse(0L);
		return fenToYuan(val);
	}

	@PoiColumn(idx = 5, title = "通联手续费（元）")
	@ApiModelProperty("第三方的通道手续费")
	public String thirdTunnelFeeString() {
		Long val = Optional.ofNullable(thirdTunnelFee).orElse(0L);
		return fenToYuan(val);
	}

	private String fenToYuan(Long val) {
		BigDecimal decimal = BigDecimal.valueOf(val);
		return decimal.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_DOWN).toString();
	}

	@PoiColumn(idx = 1, title = "事件")
	@ApiModelProperty(name="event", value = "事件")
	public String getEvent() {
		EnumYunst.YunstMethodName name = EnumUtil
			.parse(EnumYunst.YunstMethodName.class, getMethodName());
		return name != null ? name.getValueName().replace("代收","支付")
				.replace("代付","收入"): null;
	}

}
