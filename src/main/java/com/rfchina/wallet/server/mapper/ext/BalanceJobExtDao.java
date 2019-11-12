package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.BalanceJobMapper;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface BalanceJobExtDao extends BalanceJobMapper {

	@Update({
		"update rf_balance_job ",
		"set deleted = 1",
		"where balance_date  >=  #{beginDate} and balance_date <= #{endDate} "
	})
	void deleteByDate(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);
}
