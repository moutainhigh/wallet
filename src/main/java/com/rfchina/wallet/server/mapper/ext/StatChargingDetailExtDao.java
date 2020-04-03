package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.StatChargingDetailMapper;
import com.rfchina.wallet.server.model.ext.SumOfFeeVo;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface StatChargingDetailExtDao extends StatChargingDetailMapper {

	@Select({"<script>",
		"select ifnull(sum(tunnel_count),0) localTunnelCount,ifnull(sum(local_tunnel_fee),0) localTunnelFee,ifnull(sum(third_tunnel_fee),0) thirdTunnelFee from rf_stat_charging_detail",
		"where tunnel_type = #{tunnelType} and biz_time <![CDATA[ >= ]]> #{startTime} and biz_time <![CDATA[ <= ]]> #{endTime} ",
		"and service_name = #{serviceName} and deleted = 0 ",
		"and method_name in <foreach item='m' collection='methodName' open='(' separator=',' close=')'>#{m}</foreach>",
		"</script>"
	})
	SumOfFeeVo sumOfFeeByMethod(@Param("tunnelType") Byte tunnelType,
		@Param("startTime") Date startTime, @Param("endTime") Date endTime,
		@Param("serviceName") String serviceName, @Param("methodName") List<String> methodName);

	@Update({
		"update rf_stat_charging_detail ",
		"set deleted = 1",
		"where tunnel_type = #{tunnelType} and biz_time >= #{firstDay} and biz_time <= #{lastDay} ",
		"and deleted = 0"
	})
	void deleteByTimeRange(@Param("tunnelType") Byte tunnelType, @Param("firstDay") Date firstDay,
		@Param("lastDay") Date lastDay);

	@Update({
		"update rf_stat_charging_detail a,rf_balance_tunnel_detail b",
		"set a.third_tunnel_fee = case when b.tunnel_order_type = '退款' and b.channel_fee_amount > 0 then 0 - b.channel_fee_amount else b.channel_fee_amount end ",
		"where a.biz_time >= #{startTime} and a.biz_time <= #{endTime} and a.deleted = 0 and a.order_no = b.order_no and b.deleted = 0"
	})
	void updateTunnelDetail(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
