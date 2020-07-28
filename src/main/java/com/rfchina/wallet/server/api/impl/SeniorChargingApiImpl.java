package com.rfchina.wallet.server.api.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.biztools.functionnal.LockDone;
import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.biztools.fileserver.EnumFileAcl;
import com.rfchina.platform.biztools.fileserver.FileServer;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.DownloadStatus;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.server.api.SeniorChargingApi;
import com.rfchina.wallet.server.model.ext.ReportDownloadVo;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.msic.RedisConstant;
import com.rfchina.wallet.server.service.SeniorChargingService;
import java.util.Date;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeniorChargingApiImpl implements SeniorChargingApi {

	@Autowired
	private SeniorChargingService seniorChargingService;

	@Autowired
	private SimpleExclusiveLock lock;

	@Autowired
	private FileServer fileServer;

	@Autowired
	private RedisTemplate redisTemplate;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<StatCharging> queryCharging(String accessToken, Integer limit, Integer offset,
		Boolean stat) {
		return seniorChargingService.queryCharging(limit, offset, stat);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<StatChargingDetailVo> queryChargingDetail(String accessToken,
		String startTime,
		String endTime, Integer limit, Integer offset, Boolean stat, Boolean asc) {
		return seniorChargingService.queryChargingDetail(
			DateUtil.parse(startTime, DateUtil.STANDARD_DTAE_PATTERN),
			DateUtil.parse(endTime, DateUtil.STANDARD_DTAE_PATTERN),
			limit, offset, stat, asc);
	}

	@Log
//	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
//	@SignVerify
	@Override
	public void chargingRedo(String accessToken, Date startTime, Date endTime) {
		String key = String.format("%s_%s",
			DateUtil.formatDate(startTime, DateUtil.SHORT_DTAE_PATTERN),
			DateUtil.formatDate(endTime, DateUtil.SHORT_DTAE_PATTERN));
		new LockDone(lock).apply(key, 30, () -> {
			seniorChargingService.chargingRedo(startTime, endTime);
		});
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	public StatCharging queryChargingByCurrentMonth(String accessToken) {
		return seniorChargingService.queryChargingByCurrentMonth();
	}


	@Async
	@Override
	public void exportChargingDetail(String accessToken, String fileName, String uniqueCode,
		String startTime, String endTime) {

		String threadName = Thread.currentThread().getName();
		log.info("线程[{}]正在导出报表[{}]", threadName, fileName);
		Date start = DateUtil.parse(startTime, DateUtil.STANDARD_DTAE_PATTERN);
		Date end = DateUtil.parse(endTime, DateUtil.STANDARD_DTAE_PATTERN);
		byte[] bytes = seniorChargingService.exportChargingDetail(fileName, start, end);
		String fileKey = "report/" + fileName;
		fileServer
			.upload(fileKey, bytes, "application/octet-stream", EnumFileAcl.PUBLIC_READ, null);
		BoundHashOperations hashOps = redisTemplate.boundHashOps(RedisConstant.DOWNLOAD_OBJECT_KEY);
		if (hashOps.hasKey(uniqueCode)) {
			String val = (String) hashOps.get(uniqueCode);
			ReportDownloadVo downloadVo = JsonUtil
				.toObject(val, ReportDownloadVo.class, getObjectMapper());
			downloadVo.setStatus(DownloadStatus.BUILDED.getValue());
			downloadVo.setLocation(getFileSrvPrefix() + fileKey);
			hashOps.put(uniqueCode, JsonUtil.toJSON(downloadVo, getObjectMapper()));
		}
		log.info("线程[{}]完成导出报表[{}]", threadName, fileName);

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
