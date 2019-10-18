package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletClearingMapper;
import com.rfchina.wallet.domain.model.WalletClearing;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletClearingExtDao extends WalletClearingMapper {

	@Select({
		"select * from rf_wallet_clearing",
		"where apply_id = #{applyId}"
	})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletClearingMapper.BaseResultMap"})
	List<WalletClearing> selectByApplyId(@Param("applyId") Long applyId);

	@Update({
		"update rf_wallet_clearing",
		"set handling_amount = handling_amount + ${amount}",
		"where collect_id = #{collectId} and wallet_id = #{walletId} and amount >= clear_amount + handling_amount + ${amount}"
	})
	int updateClearAmount(@Param("collectId") Long collectId, @Param("walletId") Long walletId,
		@Param("amount") Long amount);

	@Select({
		"select * from rf_wallet_clearing",
		"where order_no = #{orderNo}"
	})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletClearingMapper.BaseResultMap"})
	WalletClearing selectByOrderNo(@Param("orderNo") String orderNo);

	@Select({
		"select * from rf_wallet_clearing",
		"where collect_id = #{collectId}"
	})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletClearingMapper.BaseResultMap"})
	List<WalletClearing> selectByCollectId(@Param("collectId")Long collectId);
}
