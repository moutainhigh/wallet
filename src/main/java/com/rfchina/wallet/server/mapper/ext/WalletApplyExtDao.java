package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletApplyMapper;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.model.ext.AcceptNo;
import com.rfchina.wallet.server.model.ext.HostSeqNo;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletApplyExtDao extends WalletApplyMapper {

	@Select({
		"select distinct batch_no ",
		"from rf_wallet_apply",
		"where status = 1 and batch_no is not null",
		"limit #{batchSize}"
	})
	List<String> selectUnSendBatchNo(@Param("batchSize") Integer batchSize);

	@Select({
		"select distinct accept_no as acceptNo,ref_method as refMethod, create_time as createTime",
		"from rf_wallet_apply",
		"where status = 2 and query_time < CURRENT_TIMESTAMP and curr_try_times < max_try_times",
		"order by accept_no asc limit #{batchSize}"
	})
	List<AcceptNo> selectUnFinish(@Param("batchSize") Integer batchSize);

	@Select({
		"select distinct host_accept_no as hostAcceptNo,audit_time as auditTime",
		"from rf_wallet_apply",
		"where accept_no = #{acceptNo}",
		"limit 1"
	})
	HostSeqNo selectHostAcctNo(@Param("acceptNo") String acceptNo);

	@Select({
		"select * from rf_wallet_apply",
		"where batch_no = #{batchNo} and status = 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletApplyMapper.BaseResultMap")
	List<WalletApply> selectUnDealByBatchNo(@Param("batchNo") String batchNo);

	@Update({"update rf_wallet_apply"
		, "set err_msg = #{errMsg} , status = #{status}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	int updateStatusByAcceptNo(@Param("acceptNo") String handleSeqNo, @Param("status") Byte value,
		@Param("errMsg") String errMsg);

	@Update({"update rf_wallet_apply"
		, "set curr_try_times = curr_try_times + 1 ,query_time = #{queryTime} "
		, "where accept_no = #{acceptNo}"
	})
	void incTryTimes(@Param("acceptNo") String acceptNo, @Param("queryTime") Date queryTime);

	@Select({
		"select * from rf_wallet_apply"
		, "where host_accept_no = #{hostAcceptNo} and elec_cheque_no = #{elecChequeNo} "
		, " and status = #{status}"
		, "limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletApplyMapper.BaseResultMap")
	WalletApply selectByHostAcctAndElecNo(@Param("hostAcceptNo") String hostAcceptNo,
		@Param("elecChequeNo") String elecChequeNo, @Param("status") Byte status);


	@Select({
		"select * from rf_wallet_apply"
		, "where accept_no = #{acceptNo} and elec_cheque_no = #{elecChequeNo} "
		, " and status = #{status}"
		, "limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletApplyMapper.BaseResultMap")
	WalletApply selectByAcctAndElecNo(@Param("acceptNo") String acceptNo,
		@Param("elecChequeNo") String elecChequeNo, @Param("status") Byte status);

	@Update({"update rf_wallet_apply"
		, "set host_accept_no = #{hostAcceptNo} , audit_time = #{auditTime}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	void updateHostAcctNo(@Param("acceptNo") String acceptNo,
		@Param("hostAcceptNo") String hostJnlSeqNo, @Param("auditTime") Date auditTime);

	@Update({"update rf_wallet_apply"
		, "set stage = #{stage}, err_status = #{errStatus}, err_code = #{errCode}, user_err_msg = #{errMsg}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	void updateAcceptNoErrMsg(@Param("acceptNo") String acceptNo, @Param("stage") String stage,
		@Param("errStatus") String errStatus, @Param("errCode") String errCode,
		@Param("errMsg") String errMsg);

	@Update({"update rf_wallet_apply"
		, "set status = #{status}, audit_time = #{auditTime}, end_time = #{endTime}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	void updateAcceptNoStatus(@Param("acceptNo") String acceptNo, @Param("status") Byte status,
		@Param("auditTime") Date auditTime, @Param("endTime") Date endTime);

	@Update({"update rf_wallet_apply"
		, "set locked = #{destLocked}"
		, "where batch_no = #{batchNo} and locked = #{orgLocked}"
	})
	int updateLock(@Param("batchNo") String batchNo, @Param("orgLocked") Byte orgLocked,
		@Param("destLocked") Byte destLocked);

	@Update({"<script>"
		, "update rf_wallet_apply"
		, "set notified = #{notified}"
		, "where id in "
		,
		"<foreach item='item' collection='ids' open= '(' close= ')' separator= ','>${item}</foreach>"
		, "</script>"
	})
	void updateNotified(@Param("ids") List<Long> ids, @Param("notified") Byte notified);

	@Select({"select * from rf_wallet_apply"
		, "where status = #{status} and notified = 0"
		, "order by id desc limit #{limit}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletApplyMapper.BaseResultMap")
	List<WalletApply> selectByStatusNotified(@Param("status") Byte status,
		@Param("limit") Integer limit);

	@Update({"update rf_wallet_apply"
		, "set status = #{status}, audit_time = #{auditTime}, end_time = #{endTime}"
		, "where elec_cheque_no = #{elecChequeNo} and status = 2"
	})
	void updateElecNoStatus(@Param("elecChequeNo") String elecChequeNo,
		@Param("status") Byte status,
		@Param("auditTime") Date auditTime, @Param("endTime") Date endTime);
}
