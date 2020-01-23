package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.ApplyStatusChangeMapper;
import com.rfchina.wallet.domain.model.ApplyStatusChange;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface ApplyStatusChangeExtDao extends ApplyStatusChangeMapper {

	@Select({
			"select",
			"id, apply_id, old_val, new_val, create_time, audit_user_id, audit_user, audit_status, ",
			"audit_status_desc, audit_time, audit_comment, remark",
			"from rf_apply_status_change",
			"where apply_id = #{applyId,jdbcType=BIGINT} limit 1"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.ApplyStatusChangeMapper.BaseResultMap")
	ApplyStatusChange selectByApplyId(Long applyId);

}
