package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletClearInfoMapper;
import com.rfchina.wallet.domain.model.WalletClearInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletClearInfoExtDao extends WalletClearInfoMapper {

	@Select({
		"select * from rf_wallet_clear_info",
		"where collect_id = #{collectId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletClearInfoMapper.BaseResultMap")
	List<WalletClearInfo> selectByCollectId(@Param("collectId") Long collectId);
}
