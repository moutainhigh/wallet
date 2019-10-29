package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletChannelMapper;
import com.rfchina.wallet.domain.model.WalletChannel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;


public interface WalletChannelExtDao extends WalletChannelMapper {

	@Select({
		"select * from rf_wallet_channel",
		"where channel_type = #{channelType} and biz_user_id = #{bizUserId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletChannelMapper.BaseResultMap")
	WalletChannel selectByChannelTypeAndBizUserId(@Param("channelType") Integer channelType,
		@Param("bizUserId") String bizUserId);


	@Select({
		"select * from rf_wallet_channel",
		"where channel_type = #{channelType} and wallet_id = #{walletId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletChannelMapper.BaseResultMap")
	WalletChannel selectByChannelTypeAndWalletId(@Param("channelType") Integer channelType,
		@Param("walletId") Long walletId);

	@Select({
		"select * from rf_wallet_channel",
		"where channel_type = #{channelType} and wallet_id = #{walletId} "
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletChannelMapper.BaseResultMap")
	WalletChannel selectByWalletId(@Param("walletId") Long walletId,
		@Param("channelType") Byte channelType);


	@Select({
		"select * from rf_wallet_channel",
		"where channel_type = 2 and is_sign_contact=1 and balance_protocol_req_sn = #{reqSn} "
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletChannelMapper.BaseResultMap")
	WalletChannel selectByBanlceProtocolReqSn(@Param("reqSn") String reqSn);
}
