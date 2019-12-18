package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.BalanceResultMapper;
import com.rfchina.wallet.domain.model.BalanceResult;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface BalanceResultExtDao  extends BalanceResultMapper {

	@Update({
		"update rf_balance_result ",
		"set deleted = 1",
		"where balance_date  >=  #{beginDate} and balance_date <= #{endDate} "
	})
	void deleteByDate(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

}
