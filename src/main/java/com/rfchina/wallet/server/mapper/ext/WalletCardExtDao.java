package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletCardMapper;
import com.rfchina.wallet.domain.model.WalletCard;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface WalletCardExtDao extends WalletCardMapper {

	@Select({"<script>",
		"select * from rf_wallet_card where wallet_id = #{walletId} and status = 1 and is_public = 1",
		"</script>"})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletCardMapper.BaseResultMap"})
	List<WalletCard> selectPubAccountByWalletId(@Param("walletId") Long walletId);

	@Select({"<script>",
		"select * from rf_wallet_card where wallet_id = #{walletId} and status = 1 and is_public = 1 and verify_time is null limit 1",
		"</script>"})
	@ResultMap({"com.rfchina.wallet.domain.mapper.WalletCardMapper.BaseResultMap"})
	WalletCard selectNonVerifyPubAccountByWalletId(@Param("walletId") Long walletId);

	@Update({"<script>", "update rf_wallet_card set status = #{newStatus}",
		"<if test=\"isPublic != null\">, is_public = #{isPublic} </if>",
		" where wallet_id = #{walletId} and status = #{oldStatus}", "</script>"})
	int updateWalletCard(@Param("walletId") Long walletId, @Param("newStatus") Integer newStatus,
		@Param("oldStatus") Integer var3, @Param("isPublic") Integer isPublic);
}
