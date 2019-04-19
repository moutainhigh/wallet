package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletUserMapper;
import com.rfchina.wallet.domain.model.WalletUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletUserExtDao extends WalletUserMapper {

	@Select({
		"select * from rf_wallet_user",
		"where wallet_id = #{walletId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletUserMapper.BaseResultMap")
	WalletUser selectByWalletId(@Param("walletId") Long walletId);
}
