package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletConfigMapper;
import com.rfchina.wallet.domain.model.WalletConfig;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface WalletConfigExtDao extends WalletConfigMapper {

	@Select({
		"select * from rf_wallet_config",
		"where wallet_id = 0 and status = 1",
		" order by id asc limit 1 "
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletConfigMapper.BaseResultMap")
	WalletConfig selectUniCfg();

}
