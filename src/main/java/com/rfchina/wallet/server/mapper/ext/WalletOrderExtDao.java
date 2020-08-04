package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.ext.WalletOrderDao;
import com.rfchina.wallet.domain.model.WalletOrder;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletOrderExtDao extends WalletOrderDao {

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
		"select * from rf_wallet_order where 1=1",
		"<if test=\"walletId != null\"> and wallet_id=#{walletId}</if>",
		"<if test=\"fromTime!=null\">",
		"and create_time <![CDATA[>=]]> #{fromTime}", "</if>",
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
		@Param("tradeType") Byte tradeType, @Param("status") Byte status,
		@Param("orderNo") String orderNo, @Param("bizNo") String bizNo, @Param("limit") int limit,
		@Param("offset") int offset);

	@Select({
		"<script>",
		"select count(1) from rf_wallet_order where 1=1",
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
		@Param("tradeType") Byte tradeType, @Param("status") Byte status,
		@Param("orderNo") String orderNo, @Param("bizNo") String bizNo);

	/**
	 * 更新尝试次数
	 */
	@Update({"update rf_wallet_order"
		, "set status = 4"
		, "where id = #{applyId} and status = 3"
	})
	int setApplyStatusFail(@Param("applyId") Long applyId);

	@Select({
		"select * from rf_wallet_order",
		"where batch_no = #{batchNo} and biz_no = #{bizNo}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	WalletOrder selectByBatchNoAndBizNo(@Param("batchNo") String batchNo,
		@Param("bizNo") String bizNo);

	/**
	 * 查询指定时间范围内结算不成功订单
	 */
	@Select({"select * from rf_wallet_order",
		"where create_time between #{orderStartDate} and #{orderEndDate}",
		"and type = 1 and status = 2 and !(notified & 4) "})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	List<WalletOrder> selectSattleFailedOrder(@Param("orderStartDate") String orderStartDate,
		@Param("orderEndDate") String orderEndDate);

	/**
	 * 批量更新通知状态
	 *
	 * @param walletOrderIds 订单id列表
	 */
	@Update({
		"<script>",
		"update rf_wallet_order set notified = (notified | 4)",
		"where id in",
		"<foreach collection=\"walletOrderIds\" index=\"index\" item=\"item\"",
		" separator=\",\" open=\"(\" close=\")\">",
		"#{item}",
		"</foreach>",
		"</script>"
	})
	int batchUpdateNotifiedByIds(@Param("walletOrderIds") List<Long> walletOrderIds);

	@Update({
		"update rf_wallet_order ",
		"set charging_type = #{chargingType} , charging_value = #{chargingValue}, tunnel_fee = #{fee}",
		"where order_no = #{orderNo}"
	})
	int updateFee(@Param("orderNo") String orderNo, @Param("chargingType") Integer chargingType,
		@Param("chargingValue") BigDecimal chargingValue, @Param("fee") Long fee);

	@Select({
		"<script>",
		"select distinct a.* from rf_wallet_order a left outer join rf_wallet_consume b on a.id = b.order_id",
		"<where>",
		"<if test='maxId != null '>",
		"and a.id <![CDATA[>]]> #{maxId}",
		"</if>",
		"<if test='types != null and types.size()>0'>",
		"and a.type in <foreach item='t' collection='types' open='(' separator=',' close=')' >#{t}</foreach>",
		"</if>",
		"<if test='statusList != null and statusList.size()>0'>",
		"and a.status in <foreach item='s' collection='statusList' open='(' separator=',' close=')' >#{s}</foreach>",
		"</if>",
		"and (a.wallet_id = #{walletId} or b.payee_wallet_id =  #{walletId} ) ",
		"<if test='fromTime != null'>",
		"and a.create_time <![CDATA[>=]]> #{fromTime}",
		"</if>",
		"<if test='toTime != null'>",
		"and a.create_time <![CDATA[<]]> #{toTime}",
		"</if>",
		"</where>",
		"order by a.create_time desc,a.id desc limit 1000 offset 0 ",
		"</script>"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletOrderMapper.BaseResultMap")
	List<WalletOrder> selectByMaxId(
		@Param("maxId") Long maxId, @Param("walletId") Long walletId,
		@Param("types") List<Byte> types, @Param("statusList") List<Byte> statusList,
		@Param("fromTime") Date fromTime, @Param("toTime") Date toTime);
}
