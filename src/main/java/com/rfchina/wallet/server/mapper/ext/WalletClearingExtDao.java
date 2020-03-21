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
		"where order_id = #{orderId}"
	})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletClearingMapper.BaseResultMap"})
	WalletClearing selectByOrderId(@Param("orderId") Long orderId);

	@Select({
		"select * from rf_wallet_clearing",
		"where collect_order_no = #{collectOrderNo}"
	})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletClearingMapper.BaseResultMap"})
	List<WalletClearing> selectByCollectOrderNo(@Param("collectOrderNo") String collectOrderNo);

	@Select({
		"select b.* from rf_wallet_order a join rf_wallet_clearing b on a.id = b.order_id",
		"where b.id > #{maxId} and a.wallet_id = #{walletId} and a.status = 3 and b.amount > b.withdraw_amount",
		"order by id asc limit 100"
	})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletClearingMapper.BaseResultMap"})
	List<WalletClearing> selectUnWithdraw(@Param("walletId") Long walletId,
		@Param("maxId") Long maxId);

	@Update({
		"update rf_wallet_clearing ",
		"set withdraw_amount = withdraw_amount + #{withdrawAmount}",
		"where id = #{clearingId}"
	})
	void accWithdrawAmount(@Param("clearingId") Long clearingId,
		@Param("withdrawAmount") Long withdrawAmount);
}
