package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletCollectMethodMapper;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletCollectMethodExtDao extends WalletCollectMethodMapper {


	@Select({
		"select * from rf_wallet_collect_method",
		"where ref_id = #{collectId} and type = #{type}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletCollectMethodMapper.BaseResultMap")
	List<WalletCollectMethod> selectByCollectId(@Param("collectId") Long collectId,
		@Param("type") Byte type);
}
