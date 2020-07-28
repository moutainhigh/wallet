package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.ParamVerify;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletAreaDao;
import com.rfchina.wallet.domain.mapper.ext.WalletTerminalDao;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.WalletArea;
import com.rfchina.wallet.domain.model.WalletAreaCriteria;
import com.rfchina.wallet.domain.model.WalletTerminal;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.ext.WalletTerminalExt;
import com.rfchina.wallet.server.api.WalletTerminalApi;
import com.rfchina.wallet.server.bank.yunst.response.VspTermidResp;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.msic.EnumYunst.EnumTerminalStatus;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WalletTerminalApiImpl implements WalletTerminalApi {

	@Autowired
	private WalletAreaDao walletAreaDao;

	@Autowired
	private WalletTerminalDao walletTerminalDao;

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	@Override
	public void bindTerminal(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String areaCode,
		@ParamValid(nullable = false) String vspTermid,
		@ParamValid(nullable = false) String creatorId,
		@ParamValid(nullable = false) String creatorName) {

		WalletArea walletArea = walletAreaDao.selectOneByAreaCode(areaCode);
		Optional.ofNullable(walletArea)
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.AREACODE_NOT_EXIST));

		if (Objects.isNull(walletArea.getProxyBizUserId())
			|| Objects.isNull(walletArea.getVspMerchantid())
			|| Objects.isNull(walletArea.getVspCusid())
			|| Objects.isNull(walletArea.getAppId())
		) {
			throw new WalletResponseException(EnumWalletResponseCode.TERMINAL_VSPINFO_EMPTY);
		}
		VspTermidResp resp = yunstUserHandler.vspTermid(walletArea.getProxyBizUserId()
			, walletArea.getVspMerchantid(), walletArea.getVspCusid(), walletArea.getAppId(),
			vspTermid, "set");
		if (resp.getResult().equalsIgnoreCase("OK")) {

			WalletTerminal terminal = WalletTerminal.builder()
				.status(EnumTerminalStatus.BIND.getValue())
				.areaCode(areaCode)
				.vspTermid(vspTermid)
				.creatorId(creatorId)
				.creatorName(creatorName)
				.createTime(new Date())
				.build();
			walletTerminalDao.insertSelective(terminal);

			log.info("地区[{}]绑定终端[{}]成功", areaCode, vspTermid);
		} else {
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				resp.getResult());
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	@Override
	public void bindVspId(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) String appId,
		@ParamValid(nullable = false) String areaCode,
		@ParamValid(nullable = false) String vspMerchantid,
		@ParamValid(nullable = false) String vspCusid,
		@ParamValid(nullable = false) Long proxyWalletId,
		@ParamValid(nullable = false) String creatorId,
		@ParamValid(nullable = false) String creatorName) {

		WalletArea walletArea = walletAreaDao.selectOneByAreaCode(areaCode);
		Optional.ofNullable(walletArea)
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.AREACODE_NOT_EXIST));

		WalletTunnel tunnel = walletTunnelDao
			.selectByWalletId(proxyWalletId, TunnelType.YUNST.getValue());
		Optional.ofNullable(tunnel)
			.orElseThrow(
				() -> new WalletResponseException(EnumWalletResponseCode.TUNNEL_INFO_NOT_EXISTS));

		walletArea.setAppId(appId);
		walletArea.setVspMerchantid(vspMerchantid);
		walletArea.setVspCusid(vspCusid);
		walletArea.setProxyWalletId(proxyWalletId);
		walletArea.setProxyBizUserId(tunnel.getBizUserId());
		walletArea.setCreatorId(creatorId);
		walletArea.setCreatorName(creatorName);
		walletArea.setUpdateTime(new Date());

		walletAreaDao.updateByPrimaryKeySelective(walletArea);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	@Override
	public Pagination<WalletArea> queryArea(
		@ParamValid(nullable = false) String accessToken,
		@ParamValid(nullable = false) Integer limit,
		@ParamValid(nullable = false) Integer offset) {

		WalletAreaCriteria example = new WalletAreaCriteria();
		example.setOrderByClause("area_code asc");
		List<WalletArea> data = walletAreaDao
			.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
		long count = walletAreaDao.countByExample(example);

		return new Pagination.PaginationBuilder()
			.data(data)
			.total(count)
			.pageLimit(limit)
			.offset(offset)
			.build();
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@ParamVerify
	@Override
	public Pagination<WalletTerminalExt> queryTerminalExt(String accessToken, String areaCode,
		Long proxyWalletId, String vspCusid, String vspTermid, Byte status, Integer limit,
		Integer offset) {

		List<WalletTerminalExt> data = walletTerminalDao.selectTerminalExt(areaCode, proxyWalletId,
			vspCusid, vspTermid, status, limit, offset);
		int count = walletTerminalDao.countTerminalExt(areaCode, proxyWalletId,
			vspCusid, vspTermid, status);
		return new Pagination.PaginationBuilder<WalletTerminalExt>()
			.data(data)
			.total(count)
			.pageLimit(limit)
			.offset(offset)
			.build();

	}
}
