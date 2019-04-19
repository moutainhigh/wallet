package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface WalletExtDao extends WalletMapper {


	@Select({
		"<script>",
		"select distinct wallet_type from rf_wallet",
		"where id in  ",
		"<foreach item='item' collection='walletIds' open='(' separator=',' close=')'>#{item}</foreach>",
		"</script>"
	})
	List<Byte> selectWalletType(@Param("walletIds") List<Long> walletIds);
}
