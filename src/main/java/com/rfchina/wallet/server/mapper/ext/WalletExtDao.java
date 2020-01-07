package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletMapper;
import com.rfchina.wallet.domain.model.Wallet;
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
		, "set status = #{status}, audit_type = #{auditType}"
		, "where id = #{walletId}"
	})
	void updateActiveStatus(@Param("walletId") Long walletId, @Param("status") Byte status,
		@Param("auditType") Long auditType);


	@Select({
		"<script>",
		"select id,type,level,status,title,wallet_balance,freeze_amount from rf_wallet",
		"where 1=1",
		"<if test=\"title != null\"> and title =#{title}</if>",
		"<if test=\"type != null\"> and type =#{type}</if>",
		"<if test=\"walletLevel != null\"> and level =#{walletLevel}</if>",
		"<if test=\"status != null\"> and status =#{status}</if>",
		"order by create_time desc", "limit #{limit} offset #{offset}",
		"</script>"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletMapper.BaseResultMap")
	List<Wallet> selectByCondition(@Param("title") String title, @Param("type") Byte type,
		@Param("walletLevel") Byte walletLevel, @Param("status") Byte status,
		@Param("limit") Integer limit, @Param("offset") Integer offset);


	@Select({
		"<script>",
		"select count(1) from rf_wallet",
		"where 1=1",
		"<if test=\"title != null\"> and title =#{title}</if>",
		"<if test=\"type != null\"> and type =#{type}</if>",
		"<if test=\"walletLevel != null\"> and level =#{walletLevel}</if>",
		"<if test=\"status != null\"> and status =#{status}</if>",
		"</script>"
	})
	long countByCondition(@Param("title") String title, @Param("type") Byte type,
		@Param("walletLevel") Byte walletLevel, @Param("status") Byte status);

	@Update({
		"update rf_wallet ",
		"set recharge_amount = recharge_amount + #{amount} , recharge_count = recharge_count + 1",
		"where id = #{walletId}"
	})
	int accRecharge(@Param("walletId") Long walletId, @Param("amount") Long amount);

	@Update({
		"update rf_wallet ",
		"set pay_amount = pay_amount + #{amount} , pay_count = pay_count + 1",
		"where id = #{walletId}"
	})
	int accPay(@Param("walletId") Long walletId, @Param("amount") Long amount);
}
