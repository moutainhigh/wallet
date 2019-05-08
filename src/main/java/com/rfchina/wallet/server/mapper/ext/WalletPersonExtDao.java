package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletPersonMapper;
import com.rfchina.wallet.domain.model.WalletPerson;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletPersonExtDao extends WalletPersonMapper {

	@Select({"select * from rf_wallet_person where wallet_id = #{walletId}"})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletPersonMapper.BaseResultMap")
	WalletPerson selectByWalletId(@Param("walletId") Long walletId);
}
