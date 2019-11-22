package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletVerifyHisMapper;
import com.rfchina.wallet.domain.model.WalletVerifyHis;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;


public interface WalletVerifyHisExtDao extends WalletVerifyHisMapper {

	@Select({
		"select * from rf_wallet_verify_his",
		"where wallet_id = #{walletId} and ref_id=#{refId} and type=#{type} limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletVerifyHisMapper.BaseResultMap")
	WalletVerifyHis selectByWalletIdAndRefIdAndType(@Param("walletId") Long walletId,@Param("refId") Long refId, @Param("type") Byte type);
}
