package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletCollectMethodMapper;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletCollectMethodExtDao extends WalletCollectMethodMapper {


	@Select({
		"select * from rf_wallet_collect_method",
		"where type = #{type} and ref_id = #{collectId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletCollectMethodMapper.BaseResultMap")
	List<WalletCollectMethod> selectByCollectId(@Param("collectId") Long collectId,
		@Param("type") Byte type);

	@Select({
		"select * from rf_wallet_collect_method",
		"where order_id in ",
		"(select id from rf_wallet_order where order_no = #{orderNo})"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletCollectMethodMapper.BaseResultMap")
	WalletCollectMethod getByOrderNo(@Param("orderNo") String orderNo);

}
