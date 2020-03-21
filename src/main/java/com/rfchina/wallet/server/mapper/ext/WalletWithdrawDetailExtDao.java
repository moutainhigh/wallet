package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletWithdrawDetailMapper;
import com.rfchina.wallet.domain.model.WalletWithdrawDetail;
import java.util.List;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletWithdrawDetailExtDao extends WalletWithdrawDetailMapper {

	@Select({
		"select * from rf_wallet_withdraw_detail",
		"where withdraw_id = #{withdrawId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletWithdrawDetailMapper.BaseResultMap")
	List<WalletWithdrawDetail> selectByWithdrawId(Long withdrawId);
}
