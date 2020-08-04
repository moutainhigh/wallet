package com.rfchina.wallet.server.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfchina.biztools.functional.MaxIdIterator;
import com.rfchina.platform.biztool.excel.ExcelBean;
import com.rfchina.platform.biztool.excel.ExcelFactory;
import com.rfchina.platform.biztools.fileserver.EnumFileAcl;
import com.rfchina.platform.biztools.fileserver.FileServer;
import com.rfchina.platform.common.json.ObjectSetter;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.DownloadStatus;
import com.rfchina.wallet.domain.model.StatChargingDetail;
import com.rfchina.wallet.domain.model.StatChargingDetailCriteria;
import com.rfchina.wallet.domain.model.StatChargingDetailCriteria.Criteria;
import com.rfchina.wallet.server.mapper.ext.StatChargingDetailExtDao;
import com.rfchina.wallet.server.model.ext.ReportDownloadVo;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.model.ext.VerifyDetailExcelVo;
import com.rfchina.wallet.server.msic.EnumWallet.ExportType;
import com.rfchina.wallet.server.msic.EnumYunst.YunstMethodName;
import com.rfchina.wallet.server.msic.RedisConstant;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportService {

	@Autowired
	private StatChargingDetailExtDao statChargingDetailDao;

	@Autowired
	private FileServer fileServer;

	@Autowired
	private RedisTemplate redisTemplate;

	@Async
	public void exportChargingDetail(String uniqueCode, String fileName, Date startTime,
		Date endTime, ExportType exportType) {

		String threadName = Thread.currentThread().getName();
		log.info("线程[{}]正在导出报表[{}]", threadName, fileName);

		ExcelBean excelBean = ExcelFactory.build2007();
		Sheet sheet = excelBean.creatSheet(fileName);
		excelBean.addTitle(sheet, 0, StatChargingDetailVo.class);

		AtomicInteger cursor = new AtomicInteger(1);
		new MaxIdIterator<StatChargingDetail>().apply((maxId) -> {

			StatChargingDetailCriteria example = new StatChargingDetailCriteria();
			example.setOrderByClause("id asc");
			Criteria criteria = example.createCriteria();
			if (ExportType.VERIFY.getValue().byteValue() == exportType.getValue()) {
				criteria.andMethodNameIn(Arrays.asList(YunstMethodName.COMPANY_VERIFY.getValue(),
					YunstMethodName.PERSON_VERIFY.getValue()));
			}
			criteria
				.andBizTimeBetween(startTime, endTime)
				.andDeletedEqualTo((byte) 0)
				.andIdGreaterThan(maxId);
			return statChargingDetailDao
				.selectByExampleWithRowbounds(example, new RowBounds(0, 1000));
		}, (row) -> {

			if (ExportType.VERIFY.getValue().byteValue() == exportType.getValue()) {
				VerifyDetailExcelVo vo = BeanUtil.newInstance(row, VerifyDetailExcelVo.class);
				excelBean.addData(sheet, cursor.getAndIncrement(), Arrays.asList(vo));
			}

			return row.getId();
		});

		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			excelBean.getWorkbook().write(byteOut);

			String fileKey = "report/manager/" + fileName;
			if (ExportType.VERIFY.getValue().byteValue() == exportType.getValue()) {
				fileKey = "report/manager/" + fileName;
			}
			fileServer.upload(fileKey, byteOut.toByteArray(),
				"application/octet-stream", EnumFileAcl.PUBLIC_READ, null);
			BoundHashOperations hashOps = redisTemplate
				.boundHashOps(RedisConstant.DOWNLOAD_OBJECT_KEY);
			if (hashOps.hasKey(uniqueCode)) {
				String val = (String) hashOps.get(uniqueCode);
				ReportDownloadVo downloadVo = JsonUtil
					.toObject(val, ReportDownloadVo.class, getObjectMapper());
				downloadVo.setStatus(DownloadStatus.BUILDED.getValue());
				downloadVo.setLocation(getFileSrvPrefix() + fileKey);
				hashOps.put(uniqueCode, JsonUtil.toJSON(downloadVo, getObjectMapper()));
			}
			log.info("线程[{}]完成导出报表[{}]", threadName, fileName);

		} catch (Exception e) {
			log.error("", e);
		}

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
