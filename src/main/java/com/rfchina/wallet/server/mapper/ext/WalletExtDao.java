package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletExtDao extends WalletMapper {


	@Select({
		"<script>",
		"select distinct type from rf_wallet",
		"where id in  ",
		"<foreach item='item' collection='walletIds' open='(' separator=',' close=')'>#{item}</foreach>",
		"</script>"
	})
	List<Byte> selectWalletType(@Param("walletIds") List<Long> walletIds);

	@Update({"update rf_wallet"
		, "set status = #{status} , audit_type = #{auditType}"
		, "where id = #{walletId}"
	})
	void updateStatus(@Param("walletId") Long walletId, @Param("status") Byte status,
		@Param("auditType") Long auditType);

}
