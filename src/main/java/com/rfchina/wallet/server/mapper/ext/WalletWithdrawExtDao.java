package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletWithdrawMapper;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletWithdrawExtDao extends WalletWithdrawMapper {

	@Select({
		"select * from rf_wallet_withdraw",
		"where apply_id = #{applyId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletWithdrawMapper.BaseResultMap")
	List<WalletWithdraw> selectByApplyId(@Param("applyId") Long applyId);

	@Select({
		"select * from rf_wallet_withdraw",
		"where order_no = #{orderNo}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletWithdrawMapper.BaseResultMap")
	WalletWithdraw selectByOrderNo(@Param("orderNo") String orderNo);
}
