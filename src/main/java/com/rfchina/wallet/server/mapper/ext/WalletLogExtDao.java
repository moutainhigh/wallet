package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletLogMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface WalletLogExtDao extends WalletLogMapper {

	@Update({"update rf_wallet_log"
		, "set accept_no = #{acceptNo} , status = #{status}"
		, "where id = #{logId}"
	})
	void updateStatusAndAcceptNo(@Param("logId") Long logId,
		@Param("status") Byte status, @Param("acceptNo") String acceptNo);

	@Update({"update rf_wallet_log"
		, "set err_msg = #{errMsg} , status = #{status}"
		, "where id = #{logId}"
	})
	void updateStatusAndErrMsg(@Param("logId") Long logId, @Param("status") Byte status,
		@Param("errMsg") String errMsg);
}
