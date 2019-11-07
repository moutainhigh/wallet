package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletTunnelMapper;
import com.rfchina.wallet.domain.model.WalletTunnel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;


public interface WalletTunnelExtDao extends WalletTunnelMapper {

	@Select({
		"select * from rf_wallet_tunnel",
		"where tunnel_type = #{tunnelType} and biz_user_id = #{bizUserId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletTunnelMapper.BaseResultMap")
	WalletTunnel selectByTunnelTypeAndBizUserId(@Param("tunnelType") Integer tunnelType,
		@Param("bizUserId") String bizUserId);


	@Select({
		"select * from rf_wallet_tunnel",
		"where tunnel_type = #{tunnelType} and wallet_id = #{walletId}"
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletTunnelMapper.BaseResultMap")
	WalletTunnel selectByTunnelTypeAndWalletId(@Param("tunnelType") Byte tunnelType,
		@Param("walletId") Long walletId);

	@Select({
		"select * from rf_wallet_tunnel",
		"where tunnel_type = #{tunnelType} and wallet_id = #{walletId} "
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletTunnelMapper.BaseResultMap")
	WalletTunnel selectByWalletId(@Param("walletId") Long walletId,
		@Param("tunnelType") Byte tunnelType);


	@Select({
		"select * from rf_wallet_tunnel",
		"where tunnel_type = 2 and is_sign_contact=1 and balance_protocol_req_sn = #{reqSn} "
	})
	@ResultMap("com.rfchina.wallet.domain.mapper.WalletTunnelMapper.BaseResultMap")
	WalletTunnel selectByBanlceProtocolReqSn(@Param("reqSn") String reqSn);
}
