package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletCollectMapper;
import com.rfchina.wallet.domain.model.WalletCollect;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletCollectExtDao extends WalletCollectMapper {

	@Select({
		"select * from rf_wallet_collect",
		"where order_id = #{orderId}"
	})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletCollectMapper.BaseResultMap"})
	List<WalletCollect> selectByOrderId(@Param("orderId") Long orderId);

}
