package com.rfchina.wallet.server.api.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.biztools.fileserver.FileServer;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.SeniorOrderApi;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.service.WalletOrderService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeniorOrderApiImpl implements SeniorOrderApi {

	@Autowired
	private FileServer fileServer;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private WalletOrderService walletOrderService;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	public void exportOrderDetail(String accessToken, Long walletId, String beginTime, String endTime,
		Byte tradeType, Byte status, String fileName, String uniqueCode) {

		walletOrderService
			.exportOrderDetail(uniqueCode,fileName, walletId, tradeType, status, beginTime, endTime);

	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<WalletOrder> queryWalletOrderDetail(String accessToken, Long walletId,
		Date fromTime, Date endTime, Byte tradeType, Byte status, int limit, int offset,
		Boolean stat) {

		List<Byte> types = tradeType != null ? Arrays.asList(tradeType) : null;
		List<Byte> statusList = status != null ? Arrays.asList(status) : null;

		List<WalletOrder> walletOrderList = walletOrderDao.selectByWalletIdStatus(walletId, types,
			statusList, fromTime, endTime, offset, limit);

		long total = 0;
		if (Objects.nonNull(stat) && stat) {
			total = walletOrderDao
				.countByWalletIdStatus(walletId, types, statusList, fromTime, endTime);
		}
		return new Pagination.PaginationBuilder<WalletOrder>().data(walletOrderList)
			.total(total)
			.offset(offset)
			.pageLimit(limit)
			.build();
	}


}
