package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.YunstFeeReportMapper;
import com.rfchina.wallet.domain.model.YunstFeeReport;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface YunstFeeReportExtDao extends YunstFeeReportMapper {

	@Select({
		"select * from yunst_fee_report",
		"where stat_time = #{statTime} and data_status=1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.YunstFeeReportMapper.BaseResultMap")
	YunstFeeReport selectByStatTime(@Param("statTime") String statTime);


	@Select({
		"select * from yunst_fee_report",
		"where data_status=1",
		"order by stat_time desc", "limit #{limit} offset #{offset}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.YunstFeeReportMapper.BaseResultMap")
	List<YunstFeeReport> selectByPage(@Param("limit") int limit, @Param("offset") int offset);


	@Select({
		"select count(1) from yunst_fee_report",
		"where data_status=1"
	})
	int count();

	@Update({
		"update yunst_fee_report set data_status=2",
		"where stat_time = #{statTime} and data_status=1"
	})
	int updateToHistory(@Param("statTime") String statTime);
}
