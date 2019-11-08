package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.BalanceTunnelDetailMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface BalanceTunnelDetailExtDao extends BalanceTunnelDetailMapper {

	@Update({
		"update rf_balance_channel_detail",
		"set deleted = 1",
		"where wallet_balance_date  >=  concat(#{dateString},' 00:00:00') and wallet_balance_date <= concat(#{dateString},' 59:59:59') "
	})
	void deleteByDate(@Param("dateString") String date);
}
