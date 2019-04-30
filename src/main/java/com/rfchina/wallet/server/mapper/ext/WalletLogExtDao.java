package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletLogMapper;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.model.ext.AcceptNo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletLogExtDao extends WalletLogMapper {

	@Update({"update rf_wallet_log"
		, "set accept_no = #{acceptNo} , status = #{status} , ref_method = #{refMethod}"
		, "where id = #{logId} and status = 1"
	})
	void updateStatusAndAcceptNo(@Param("logId") Long logId, @Param("status") Byte status,
		@Param("acceptNo") String acceptNo, @Param("refMethod") Byte refMethod);

	@Update({"update rf_wallet_log"
		, "set err_msg = #{errMsg} , status = #{status}, seq_no = #{seqNo}"
		, "where accept_no = #{acceptNo} and elec_cheque_no = #{elecChequeNo} and status = 2"
	})
	int updateStatusAndErrMsg(@Param("acceptNo") String acceptNo,
		@Param("elecChequeNo") String elecChequeNo,
		@Param("seqNo") String seqNo,
		@Param("status") Byte status,
		@Param("errMsg") String errMsg);

	@Select({
		"select distinct accept_no as acceptNo,ref_method as refMethod, create_time as createTime",
		"from rf_wallet_log",
		"where status = 2 and query_time < CURRENT_TIMESTAMP and curr_try_times < max_try_times"
	})
	List<AcceptNo> selectUnFinish();

	@Select({
		"select distinct batch_no",
		"from rf_wallet_log",
		"where status = 1",
		"limit 10"
	})
	List<String> selectUnSendBatchNo();

	@Select({
		"select * from rf_wallet_log",
		"where batch_no = #{batchNo} and status = 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletLogMapper.BaseResultMap")
	List<WalletLog> selectByBatchNo(@Param("batchNo") String batchNo);

	@Update({"update rf_wallet_log"
		, "set err_msg = #{errMsg} , status = #{status}"
		, "where accept_no = #{acceptNo} and status = 2"
	})
	int updateStatusByAcceptNo(@Param("acceptNo") String handleSeqNo, @Param("status") Byte value,
		@Param("errMsg") String errMsg);

	@Update({"update rf_wallet_log"
		, "set curr_try_times = curr_try_times + 1 ,"
		, "query_time = date_add(CURRENT_TIMESTAMP, interval power(2 , curr_try_times) minute) "
		, "where accept_no = #{acceptNo}"
	})
	void updateTryTimes(@Param("acceptNo") String acceptNo);

	@Select({
		"select * from rf_wallet_log"
		, "where accept_no = #{acceptNo} and elec_cheque_no = #{elecChequeNo} "
		, " and status = #{status}"
		, "limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletLogMapper.BaseResultMap")
	WalletLog selectByAcctAndElecNo(@Param("acceptNo") String acceptNo,
		@Param("elecChequeNo") String elecChequeNo, @Param("status") Byte status);


}
