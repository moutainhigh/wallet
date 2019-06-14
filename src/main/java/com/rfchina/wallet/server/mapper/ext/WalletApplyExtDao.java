package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletApplyMapper;
import com.rfchina.wallet.domain.model.WalletApply;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletApplyExtDao extends WalletApplyMapper {

	/**
	 * 查询未提交到银行的
	 */
	@Select({
		"select distinct batch_no ",
		"from rf_wallet_apply",
		"where status = 1 and batch_no is not null",
		"limit #{batchSize}"
	})
	List<String> selectUnSendBatchNo(@Param("batchSize") Integer batchSize);

	/**
	 * 查询已提交未结束的
	 */
	@Select({
		"select distinct batch_no as batchNo",
		"from rf_wallet_apply",
		"where status = 2 and query_time < CURRENT_TIMESTAMP and curr_try_times < max_try_times",
		"order by batch_no asc limit #{batchSize}"
	})
	List<String> selectUnFinishBatchNo(@Param("batchSize") Integer batchSize);

	/**
	 * 查询指定状态的批次申请单
	 */
	@Select({
		"select * from rf_wallet_apply",
		"where batch_no = #{batchNo} and status = #{status}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletApplyMapper.BaseResultMap")
	List<WalletApply> selectByBatchNo(@Param("batchNo") String batchNo,
		@Param("status") Byte status);

	/**
	 * 更新尝试次数
	 */
	@Update({"update rf_wallet_apply"
		, "set curr_try_times = curr_try_times + 1 ,query_time = #{queryTime} "
		, "where batch_no = #{batchNo}"
	})
	int incTryTimes(@Param("batchNo") String batchNo, @Param("queryTime") Date queryTime);

	/**
	 * 更新锁
	 */
	@Update({"update rf_wallet_apply"
		, "set locked = #{destLocked}"
		, "where batch_no = #{batchNo} and locked = #{orgLocked}"
	})
	int updateLock(@Param("batchNo") String batchNo, @Param("orgLocked") Byte orgLocked,
		@Param("destLocked") Byte destLocked);

	/**
	 * 更新通知结果
	 */
	@Update({"<script>"
		, "update rf_wallet_apply"
		, "set notified = #{notified}"
		, "where id in "
		,
		"<foreach item='item' collection='ids' open= '(' close= ')' separator= ','>${item}</foreach>"
		, "</script>"
	})
	int updateNotified(@Param("ids") List<Long> ids, @Param("notified") Byte notified);

	/**
	 * 查询未通知的申请单
	 */
	@Select({"select * from rf_wallet_apply"
		, "where status = #{status} and notified = 0"
		, "order by id desc limit #{limit}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletApplyMapper.BaseResultMap")
	List<WalletApply> selectByStatusNotNotified(@Param("status") Byte status,
		@Param("limit") Integer limit);

	/**
	 * 更新当前网关事务
	 */
	@Update({"update rf_wallet_apply"
		, "set curr_trans_id = #{transId}"
		, "where id = #{walletApplyId}"
	})
	void updateCurrTransId(@Param("walletApplyId") Long walletApplyId,
		@Param("transId") Long transId);


}
