package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.StatChargingMapper;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface StatChargingExtDao extends StatChargingMapper {

	@Update({
		"update rf_stat_charging ",
		"set deleted = 1",
		"where tunnel_type = #{tunnelType} and charging_date >= #{firstDay} and charging_date <= #{lastDay} ",
		"and deleted = 0"
	})
	void deleteByTimeRange(@Param("tunnelType") Byte value, @Param("firstDay") Date firstDay,
		@Param("lastDay") Date lastDay);

}
