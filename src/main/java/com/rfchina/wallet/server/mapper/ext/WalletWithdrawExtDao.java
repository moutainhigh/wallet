package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletWithdrawMapper;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletWithdrawExtDao extends WalletWithdrawMapper {

	@Select({
		"select * from rf_wallet_withdraw",
		"where order_id = #{orderId}",
		"limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletWithdrawMapper.BaseResultMap")
	WalletWithdraw selectByOrderId(@Param("orderId") Long orderId);
}
