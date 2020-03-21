package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletCollectInfoMapper;
import com.rfchina.wallet.domain.model.WalletCollectInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletCollectInfoExtDao extends WalletCollectInfoMapper {

	@Select({
		"select * from rf_wallet_collect_info",
		"where collect_id = #{collectId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletCollectInfoMapper.BaseResultMap")
	List<WalletCollectInfo> selectByCollectId(@Param("collectId") Long collectId);

	@Update({
		"update rf_wallet_collect_info",
		"set clear_amount = clear_amount + #{amount}",
		"where id = #{collectInfoId}"
	})
	int accuClearAmount(@Param("collectInfoId") Long collectInfoId, @Param("amount") Long amount);

	@Update({
		"update rf_wallet_collect_info",
		"set refund_amount = refund_amount + #{amount}",
		"where id = #{collectInfoId}"
	})
	int accuRefundAmount(@Param("collectInfoId") Long collectInfoId, @Param("amount") Long amount);

}
