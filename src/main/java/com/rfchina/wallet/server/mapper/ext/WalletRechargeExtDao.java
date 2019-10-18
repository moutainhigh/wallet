package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletRechargeMapper;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRecharge;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletRechargeExtDao extends WalletRechargeMapper {

	@Select({
		"select * from rf_wallet_recharge",
		"where apply_id = #{applyId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletRechargeMapper.BaseResultMap")
	List<WalletRecharge> selectByApplyId(@Param("applyId") Long applyId);

	@Select({
		"select count(1) from rf_wallet_recharge",
		"where order_no = #{orderNo}"
	})
	int selectCountByOrderNo(@Param("orderNo") String orderNo);

	@Select({
		"select * from rf_wallet_recharge",
		"where order_no = #{orderNo}",
		"limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletRechargeMapper.BaseResultMap")
	WalletRecharge selectByOrderNo(@Param("orderNo") String orderNo);
}
