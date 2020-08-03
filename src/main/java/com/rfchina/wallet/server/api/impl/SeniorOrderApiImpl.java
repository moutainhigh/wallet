package com.rfchina.wallet.server.api.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.biztools.fileserver.EnumFileAcl;
import com.rfchina.platform.biztools.fileserver.FileServer;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.DownloadStatus;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.SeniorOrderApi;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.ReportDownloadVo;
import com.rfchina.wallet.server.msic.RedisConstant;
import com.rfchina.wallet.server.service.WalletOrderService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
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
	@Async
	public void exportOrderDetail(String accessToken, Long walletId, Date beginTime, Date endTime,
		Byte tradeType, Byte status, String fileName, String uniqueCode) {

		String threadName = Thread.currentThread().getName();
		log.info("线程[{}]正在导出钱包[{}]订单明细报表[{}]", threadName, walletId, fileName);

		byte[] bytes = walletOrderService
			.exportOrderDetail(fileName, walletId, tradeType, status, beginTime, endTime);
		String fileKey = "wallet/" + walletId + "/" + fileName;
		fileServer
			.upload(fileKey, bytes, "application/octet-stream", EnumFileAcl.PUBLIC_READ, null);
		BoundHashOperations hashOps = redisTemplate
			.boundHashOps(RedisConstant.PREX_WALLET_REPORT_DOWNLOAD);
		if (hashOps.hasKey(uniqueCode)) {
			String val = (String) hashOps.get(uniqueCode);
			ReportDownloadVo downloadVo = JsonUtil
				.toObject(val, ReportDownloadVo.class, getObjectMapper());
			downloadVo.setStatus(DownloadStatus.BUILDED.getValue());
			downloadVo.setLocation(getFileSrvPrefix() + fileKey);
			hashOps.put(uniqueCode, JsonUtil.toJSON(downloadVo, getObjectMapper()));
		}
		log.info("线程[{}]完成导出钱包[{}]订单明细报表[{}]", threadName, walletId, fileName);
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

	private ObjectSetter<ObjectMapper> getObjectMapper() {
		return objectMapper -> {
			objectMapper.setTimeZone(TimeZone.getDefault());
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		};
	}

	private String getFileSrvPrefix() {

		String prefix = (fileServer.getSvrEndpoint().endsWith(SymbolConstant.SYMBOL_SLASH) ?
			fileServer.getSvrEndpoint()
			: fileServer.getSvrEndpoint() + SymbolConstant.SYMBOL_SLASH);
		return prefix + "_f" + SymbolConstant.SYMBOL_SLASH + fileServer.getSrvBucket()
			+ SymbolConstant.SYMBOL_SLASH;
	}
}
