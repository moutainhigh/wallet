package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletRefundMapper;
import com.rfchina.wallet.domain.model.WalletRefund;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletRefundExtDao extends WalletRefundMapper {


	@Select({
		"select * from rf_wallet_refund",
		"where apply_id #{applyId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletRefundMapper.BaseResultMap")
	List<WalletRefund> selectByApplyId(@Param("applyId") Long applyId);
}
