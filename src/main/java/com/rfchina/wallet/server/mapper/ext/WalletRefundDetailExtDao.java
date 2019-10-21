package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletRefundDetailMapper;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletRefundDetailExtDao extends WalletRefundDetailMapper {

	@Select({
		"select * from rf_wallet_refund_detail",
		"where refund_id = #{refundId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletRefundDetailMapper.BaseResultMap")
	List<WalletRefundDetail> selectByRefundId(@Param("refundId") Long refundId);
}
