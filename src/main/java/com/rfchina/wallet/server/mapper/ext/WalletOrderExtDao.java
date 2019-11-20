package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletOrderMapper;
import com.rfchina.wallet.domain.model.WalletOrder;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletOrderExtDao extends WalletOrderMapper {

	@Select({
		"select * from rf_wallet_order",
		"where order_no = #{orderNo}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	WalletOrder selectByOrderNo(@Param("orderNo") String orderNo);

	@Select({
		"select count(1) from rf_wallet_order",
		"where order_no = #{orderNo}"
	})
	int selectCountByOrderNo(@Param("orderNo") String orderNo);

	/**
	 * 查询指定状态的批次申请单
	 */
	@Select({
		"select count(1) from rf_wallet_apply",
		"where batch_no = #{batchNo} "
	})
	int selectCountByBatchNo(@Param("batchNo") String batchNo);


	@Select({
		"<script>",
		"select * from rf_wallet_apply where wallet_id=#{walletId}",
		"<if test=\"fromTime!=null\">",
		"and create_time <![CDATA[>=]]> #{fromTime}", "</if>",
		"<if test=\"toTime!=null\">",
		"and create_time <![CDATA[<=]]> #{toTime}", "</if>",
		"<if test=\"tradeType != null\"> and type =#{tradeType}</if>",
		"<if test=\"status != null\"> and status =#{status}</if>",
		"<if test=\"tradeNo != null\"> and trade_no =#{tradeNo}</if>",
		"order by create_time desc", "limit #{limit} offset #{offset}",
		"</script>"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	List<WalletOrder> selectByCondition(@Param("walletId") Long walletId,
		@Param("fromTime") Date fromTime, @Param("toTime") Date toTime,
		@Param("tradeType") Integer tradeType,
		@Param("status") Integer status, @Param("limit") int limit, @Param("offset") int offset);

	@Select({
		"<script>",
		"select count(1) from rf_wallet_apply where wallet_id=#{walletId}",
		"<if test=\"fromTime!=null\">",
		"and create_time <![CDATA[>=]]> #{fromTime}", "</if>",
		"<if test=\"toTime!=null\">",
		"and create_time <![CDATA[<=]]> #{toTime}", "</if>",
		"<if test=\"tradeType != null\"> and type =#{tradeType}</if>",
		"<if test=\"status != null\"> and status =#{status}</if>",
		"<if test=\"tradeNo != null\"> and trade_no =#{tradeNo}</if>",
		"</script>"
	})
	int selectCountByCondition(@Param("walletId") Long walletId,
		@Param("fromTime") Date fromTime, @Param("toTime") Date toTime,
		@Param("tradeType") Integer tradeType,
		@Param("status") Integer status);
}
