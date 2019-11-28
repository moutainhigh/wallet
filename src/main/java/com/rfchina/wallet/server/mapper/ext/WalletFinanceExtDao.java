package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletFinanceMapper;
import com.rfchina.wallet.domain.model.WalletFinance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletFinanceExtDao extends WalletFinanceMapper {

	@Select({
		"select * from rf_wallet_finance",
		"where order_id = #{orderId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletFinanceMapper.BaseResultMap")
	WalletFinance selectByOrderId(@Param("orderId") Long orderId);
}
