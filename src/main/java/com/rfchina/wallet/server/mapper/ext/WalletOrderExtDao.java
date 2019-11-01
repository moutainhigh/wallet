package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletOrderMapper;
import com.rfchina.wallet.domain.model.WalletOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletOrderExtDao extends WalletOrderMapper {

	@Select({
		"select * from rf_wallet_order",
		"where order_no = #{orderNo}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	WalletOrder selectByOrderNo(@Param("orderNo") String orderNo);

	@Select({
		"select count(1) from rf_wallet_order",
		"where order_no = #{orderNo}"
	})
	int selectCountByOrderNo(@Param("orderNo") String orderNo);

	/**
	 * 查询指定状态的批次申请单
	 */
	@Select({
		"select count(1) from rf_wallet_apply",
		"where batch_no = #{batchNo} "
	})
	int selectCountByBatchNo(@Param("batchNo") String batchNo);
}
