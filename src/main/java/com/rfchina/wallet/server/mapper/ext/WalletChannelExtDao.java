package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletChannelMapper;
import com.rfchina.wallet.domain.model.WalletChannel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;


public interface WalletChannelExtDao extends WalletChannelMapper {

	/**
	 * 查询指定高级钱包账号
	 */
	@Select({
			"select * from rf_wallet_channel",
			"where channel_type = #{channelType} and biz_user_id = #{bizUserId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletChannelMapper.BaseResultMap")
	WalletChannel selectByChannelTypeAndBizUserId(@Param("channelType") Integer channelType,
			@Param("bizUserId") String bizUserId);
}
