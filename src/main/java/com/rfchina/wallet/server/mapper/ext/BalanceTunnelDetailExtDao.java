package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.BalanceTunnelDetailMapper;
import java.util.Date;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface BalanceTunnelDetailExtDao extends BalanceTunnelDetailMapper {

	@Update({
		"update rf_balance_tunnel_detail",
		"set deleted = 1",
		"where wallet_balance_date  >=  #{beginDate} and wallet_balance_date <= #{endDate} "
	})
	void deleteByBalanceDate(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);
}
