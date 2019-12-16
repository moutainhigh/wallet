package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.YunstSdkLogMapper;
import com.rfchina.wallet.domain.model.YunstSdkLog;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;


public interface YunstSDKLogExtDao extends YunstSdkLogMapper {

	@Select({
		"select * from yunst_sdk_log",
		"where yunst_method_name = #{methodName} and create_time <![CDATA[>=]]> #{fromTime} and create_time <![CDATA[<]]> #{endTime}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.YunstSdkLogMapper.BaseResultMap")
	List<YunstSdkLog> selectByMethodNameAndTime(@Param("methodName") String methodName, @Param("fromTime") Date fromTime, @Param("endTime") Date endTime);


	@Select({
		"select count(1) from yunst_sdk_log",
		"where yunst_method_name = #{methodName} and create_time <![CDATA[>=]]> #{fromTime} and create_time <![CDATA[<]]> #{endTime}"
	})
	long countByMethodNameAndTime(@Param("methodName") String methodName, @Param("fromTime") Date fromTime, @Param("endTime") Date endTime);
}
