package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletLogMapper;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.model.ext.AcceptNo;
import com.rfchina.wallet.server.model.ext.BatchNo;
import com.rfchina.wallet.server.model.ext.HostSeqNo;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletLogExtDao extends WalletLogMapper {

	@Select({
		"select distinct batch_no ",
		"from rf_wallet_log",
		"where status = 1",
		"limit #{batchSize}"
	})
	List<String> selectUnSendBatchNo(@Param("batchSize") Integer batchSize);

	@Select({
		"select distinct accept_no as acceptNo,ref_method as refMethod, create_time as createTime",
		"from rf_wallet_log",
		"where status = 2 and query_time < CURRENT_TIMESTAMP and curr_try_times < max_try_times",
		"limit #{batchSize}"
	})
	List<AcceptNo> selectUnFinish(@Param("batchSize") Integer batchSize);

	@Select({
		"select distinct host_accept_no as hostAcceptNo,audit_time as auditTime",
		"from rf_wallet_log",
		"where accept_no = #{acceptNo}",
		"limit 1"
	})
	HostSeqNo selectHostAcctNo(@Param("acceptNo") String acceptNo);

	@Select({
		"select * from rf_wallet_log",
		"where batch_no = #{batchNo} and status = 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletLogMapper.BaseResultMap")
	List<WalletLog> selectUnDealByBatchNo(@Param("batchNo") String batchNo);

	@Update({"update rf_wallet_log"
		, "set err_msg = #{errMsg} , status = #{status}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	int updateStatusByAcceptNo(@Param("acceptNo") String handleSeqNo, @Param("status") Byte value,
		@Param("errMsg") String errMsg);

	@Update({"update rf_wallet_log"
		, "set curr_try_times = curr_try_times + 1 ,query_time = #{queryTime} "
		, "where accept_no = #{acceptNo}"
	})
	void incTryTimes(@Param("acceptNo") String acceptNo, @Param("queryTime") Date queryTime);

	@Select({
		"select * from rf_wallet_log"
		, "where host_accept_no = #{acceptNo} and elec_cheque_no = #{elecChequeNo} "
		, " and status = #{status}"
		, "limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletLogMapper.BaseResultMap")
	WalletLog selectByHostAcctAndElecNo(@Param("acceptNo") String acceptNo,
		@Param("elecChequeNo") String elecChequeNo, @Param("status") Byte status);


	@Update({"update rf_wallet_log"
		, "set host_accept_no = #{hostAcceptNo} , audit_time = #{auditTime}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	void updateHostAcctNo(@Param("acceptNo") String acceptNo,
		@Param("hostAcceptNo") String hostJnlSeqNo, @Param("auditTime") Date auditTime);

	@Update({"update rf_wallet_log"
		, "set status = 4 , err_code = #{errCode}, err_msg = #{errMsg}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	void updateAcceptNoError(@Param("acceptNo") String acceptNo, @Param("errCode") String errCode,
		@Param("errMsg") String errMsg);

	@Update({"update rf_wallet_log"
		, "set locked = #{destLocked}"
		, "where batch_no = #{batchNo} and locked = #{orgLocked}"
	})
	int updateLock(@Param("batchNo") String batchNo, @Param("orgLocked") Byte orgLocked,
		@Param("destLocked") Byte destLocked);


}
