package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletCompanyMapper;
import com.rfchina.wallet.domain.model.WalletCompany;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletCompanyExtDao extends WalletCompanyMapper {

	@Select({"select * from rf_wallet_company where wallet_id = #{walletId}"})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletCompanyMapper.BaseResultMap")
	WalletCompany selectByWalletId(@Param("walletId") Long walletId);

	@Update({"update rf_wallet_company"
		, "set company_name = #{companyName} "
		, "where wallet_id = #{walletId}"
	})
	void updateCompanyInfo(Long walletId, String companyName);
}
