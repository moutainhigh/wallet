package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletOrderMapper;
import com.rfchina.wallet.domain.model.WalletOrder;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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


	@Select({
		"select count(1) from rf_wallet_order",
		"where batch_no = #{batchNo} "
	})
	int selectCountByBatchNo(@Param("batchNo") String batchNo);


	@Select({
		"select distinct batch_no ",
		"from rf_wallet_order",
		"where type = #{orderType} and progress = 1",
		"limit #{batchSize}"
	})
	List<String> selectUnSendBatchNo(@Param("orderType") Byte orderType,
		@Param("batchSize") Integer batchSize);

	@Select({
		"select distinct batch_no ",
		"from rf_wallet_order",
		"where type = #{orderType} and progress = 2 and status = 2",
		"limit #{batchSize}"
	})
	List<String> selectUnFinishBatchNo(@Param("orderType") Byte orderType,
		@Param("batchSize") Integer batchSize);

	/**
	 * 查询指定状态的批次申请单
	 */
	@Select({
		"select * from rf_wallet_order",
		"where batch_no = #{batchNo} "
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	List<WalletOrder> selectByBatchNo(@Param("batchNo") String batchNo);



	/**
	 * 更新尝试次数
	 */
	@Update({"update rf_wallet_order"
		, "set curr_try_times = curr_try_times + 1 "
		, "where order_no = #{orderNo}"
	})
	int incTryTimes(@Param("orderNo") String orderNo);

	@Select({"select * from rf_wallet_order"
		, "where status = #{status} and notified = 0"
		, "order by id desc limit #{limit}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	List<WalletOrder> selectByStatusNotNotified(@Param("status") Byte status,
		@Param("limit") Integer limit);

	@Update({"<script>"
		, "update rf_wallet_order"
		, "set notified = #{notified}"
		, "where id in "
		,
		"<foreach item='item' collection='ids' open= '(' close= ')' separator= ','>${item}</foreach>"
		, "</script>"
	})
	int updateNotified(@Param("ids") List<Long> ids, @Param("notified") Byte notified);

	/**
	 * 更新锁
	 */
	@Update({"update rf_wallet_order"
		, "set locked = #{destLocked}"
		, "where id = #{orderId} and locked = #{orgLocked}"
	})
	int updateOrderLock(@Param("orderId") Long orderId, @Param("orgLocked") Byte orgLocked,
		@Param("destLocked") Byte destLocked);

	/**
	 * 更新锁
	 */
	@Update({"update rf_wallet_order"
		, "set locked = #{destLocked}"
		, "where batch_no = #{batchNo} and locked = #{orgLocked}"
	})
	int updateBatchLock(@Param("batchNo") String batchNo, @Param("orgLocked") Byte orgLocked,
		@Param("destLocked") Byte destLocked);

	@Select({
		"<script>",
		"select * from rf_wallet_apply where 1=1",
		"<if test=\"walletId != null\"> and wallet_id=#{walletId}</if>",
		"<if test=\"fromTime!=null\">",
		"and create_time <![CDATA[>=]]> #{fromTime}",
		"<if test=\"toTime!=null\">",
		"and create_time <![CDATA[<=]]> #{toTime}", "</if>",
		"<if test=\"tradeType != null\"> and type =#{tradeType}</if>",
		"<if test=\"status != null\"> and status =#{status}</if>",
		"<if test=\"orderNo != null\"> and order_no =#{orderNo}</if>",
		"<if test=\"bizNo != null\"> and biz_no =#{bizNo}</if>",
		"order by create_time desc", "limit #{limit} offset #{offset}",
		"</script>"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	List<WalletOrder> selectByCondition(@Param("walletId") Long walletId,
		@Param("fromTime") Date fromTime, @Param("toTime") Date toTime,
		@Param("tradeType") Integer tradeType, @Param("status") Integer status,
		@Param("orderNo") String orderNo, @Param("bizNo") String bizNo, @Param("limit") int limit,
		@Param("offset") int offset);

	@Select({
		"<script>",
		"select count(1) from rf_wallet_apply where 1=1",
		"<if test=\"walletId != null\"> and wallet_id=#{walletId}</if>",
		"<if test=\"fromTime!=null\">",
		"and create_time <![CDATA[>=]]> #{fromTime}", "</if>",
		"<if test=\"toTime!=null\">",
		"and create_time <![CDATA[<=]]> #{toTime}", "</if>",
		"<if test=\"tradeType != null\"> and type =#{tradeType}</if>",
		"<if test=\"status != null\"> and status =#{status}</if>",
		"<if test=\"orderNo != null\"> and order_no =#{orderNo}</if>",
		"<if test=\"bizNo != null\"> and biz_no =#{bizNo}</if>",
		"</script>"
	})
	int selectCountByCondition(@Param("walletId") Long walletId,
		@Param("fromTime") Date fromTime, @Param("toTime") Date toTime,
		@Param("tradeType") Integer tradeType, @Param("status") Integer status,
		@Param("orderNo") String orderNo, @Param("bizNo") String bizNo);


}
