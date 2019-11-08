package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.BalanceTunnelDetailMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface BalanceChannelDetailExtDao extends BalanceTunnelDetailMapper {

	@Delete({
		"delete from rf_balance_channel_detail",
		"where wallet_balance_date  >=  concat(#{dateString},' 00:00:00') and wallet_balance_date <= concat(#{dateString},' 59:59:59') "
	})
	void deleteByDate(@Param("dateString") String date);
}
