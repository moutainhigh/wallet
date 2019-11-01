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
		"where order_id = #{orderId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletRefundMapper.BaseResultMap")
	List<WalletRefund> selectByOrderId(@Param("orderId") Long orderId);


	@Select({
		"select * from rf_wallet_refund",
		"where collect_order_no = #{collectOrderNo}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletRefundMapper.BaseResultMap")
	List<WalletRefund> selectByCollectOrderNo(@Param("collectOrderNo") String collectId);
}
