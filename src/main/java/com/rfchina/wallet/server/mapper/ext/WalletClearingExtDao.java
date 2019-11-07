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
	List<WalletClearing> selectByCollectOrderNo(@Param("collectOrderNo")String collectOrderNo);
}
