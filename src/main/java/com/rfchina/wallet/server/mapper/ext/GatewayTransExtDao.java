package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.GatewayTransMapper;
import com.rfchina.wallet.domain.model.GatewayTrans;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface GatewayTransExtDao extends GatewayTransMapper {

	@Select({"<script>"
		, "select * from rf_gateway_trans"
		, "where id in "
		,
		"<foreach collection='tranIds' item='item' open='(' close=')' separator=','>#{item}</foreach>"
		, "</script>"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.GatewayTransMapper.BaseResultMap")
	List<GatewayTrans> selectByIds(@Param("tranIds") List<Long> tranIds);

	@Select({
		"select count(1) from rf_gateway_trans"
		,"where elec_cheque_no = #{elecNo}"
	})
	Integer selectCountByElecNo(@Param("elecNo") String elecNo);
}
