package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.BankCodeMapper;
import com.rfchina.wallet.domain.model.BankCode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface BankCodeExtDao extends BankCodeMapper {

	@Select({
		"select * from rf_bank_code"
		, "where bank_code = #{bankCode}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.BankCodeMapper.BaseResultMap")
	BankCode selectByCode(@Param("bankCode") String bankCode);
}
