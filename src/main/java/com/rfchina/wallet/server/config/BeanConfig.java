package com.rfchina.wallet.server.config;

import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.mch.sdk.api.ChargingCalculateRequest;
import com.rfchina.mch.sdk.api.ChargingGetConfigRequest;
import com.rfchina.mch.sdk.model.ChargeCalculateVo;
import com.rfchina.mch.sdk.model.ChargingConfig;
import com.rfchina.mch.sdk.model.ListChargingConfig;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.biztools.CacheHashMap;
import com.rfchina.platform.biztools.fileserver.FileServerAutoConfig;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.sdk2.ApiClient;
import com.rfchina.platform.sdk2.response.ResponseData;
import com.rfchina.platform.spring.SpringContext;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.ChargingUpdateVo;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.FeeConfigKey;
import com.rfchina.wallet.server.msic.EnumYunst.EnumAcctType;
import com.rfchina.wallet.server.msic.StringConstant;
import com.rfchina.wallet.server.service.AppService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tas.post.ParcelFactory;
import org.tas.post.worker.ParcelConsumer;
import org.tas.post.worker.ParcelProducer;


/**
 * 项目配置
 *
 * @author nzm
 */
@Slf4j
@Configuration
@Import({SimpleExclusiveLock.class, FileServerAutoConfig.class})
public class BeanConfig {


	@Autowired
	@Qualifier("apiTemplate")
	private ApiClient apiTemplate;

	@Autowired
	private AppService appService;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Bean
	public SpringContext springContext() {
		return new SpringContext();
	}

	@Bean
	public SessionThreadLocal sessionThreadLocal() {
		return new SessionThreadLocal();
	}

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
			.connectTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(60, TimeUnit.SECONDS)
			.build();
	}


	@Bean(name = "exactErrPredicate")
	public ExactErrPredicate exactErrPredicate(
		@Value("${wlpay.pudong.exactErr}") String exactErr) {
		ExactErrPredicate predicate = new ExactErrPredicate();
		predicate.parseText(exactErr);
		return predicate;
	}

	@Bean
	@Qualifier(value = "cacheExec")
	public ExecutorService cacheSrvExec() {
		return Executors.newFixedThreadPool(2,
			new BasicThreadFactory.Builder().namingPattern("CacheExec_%d").build());
	}

	@Bean
	@Qualifier(value = "walletApiExecutor")
	public ExecutorService walletApiExecutor() {
		return Executors.newFixedThreadPool(4,
			new BasicThreadFactory.Builder().namingPattern("WalletApiExec").build());
	}

	@Bean
	@Qualifier(value = "feeMap")
	public CacheHashMap<String, ChargingConfig> feeMap() {

		return new CacheHashMap<>(120L, (obj) -> {
			updateFeeConfig(obj, FeeConfigKey.YUNST_WITHDRAW.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_COMPANY_AUDIT.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_PERSON_AUDIT.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_POS_DEBIT_CARD.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_POS_CREDIT_CARD.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_POS_UNION_RCODE.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_POS_WECHAT.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_POS_ALIPAY.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_ALIPAY.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_WECHAT.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_DEBIT_CARD.getValue());
			updateFeeConfig(obj, FeeConfigKey.YUNST_CREDIT_CARD.getValue());
		});
	}

	private void updateFeeConfig(CacheHashMap<String, ChargingConfig> map, String key) {
		try {
			ChargingConfig config = getFeeConfig(key);
			if (config != null) {
				map.put(key, config);
			}
		} catch (Exception e) {
			log.error("加载手续费配置错误", e);
		}
	}

	private ChargingConfig getFeeConfig(String chargingKey) {
		ChargingGetConfigRequest req = ChargingGetConfigRequest.builder()
			.accessToken(appService.getAccessToken())
			.chargingKey(chargingKey)
			.build();
		ResponseData<ListChargingConfig> resp = apiTemplate.execute(req);
		return (!resp.getData().isEmpty()) ? resp.getData().get(0) : null;
	}


	@Bean
	public ParcelProducer parcelProducer() {

		ParcelConsumer parcelConsumer = new ParcelConsumer();
		parcelConsumer.regist(StringConstant.PARCEL_CHARGNIG_UPDATE, (parcelList) -> {
			if (parcelList == null) {
				return;
			}
			parcelList.forEach(parcel -> {
				ChargingUpdateVo charging = (ChargingUpdateVo) parcel.getPayLoad();

				try {
					if(MDC.get("traceId")==null){
						MDC.put("traceId", charging.getTraceId());
					}
					// 更新手续费用
					String prefix = charging.getCollectPayType().toChargingPrefix();
					if (CollectPayType.POS_UNION.getValue().byteValue() ==
						charging.getCollectPayType().getValue().byteValue()) {
						if (EnumAcctType.DEBIT.getValue().equals(charging.getAcctType())) {
							prefix += "_DEBIT";
						} else if (EnumAcctType.CREDIT.getValue().equals(charging.getAcctType())
							|| EnumAcctType.SEMI_CREDIT.getValue().equals(charging.getAcctType())) {
							prefix += "_CREDIT";
						}
					}
					log.info("[手续费]准备更新订单[{}]手续费", charging.getOrderNo());
					ChargingCalculateRequest req = ChargingCalculateRequest.builder()
						.accessToken(appService.getAccessToken())
						.amount(charging.getAmount())
						.chargingKey(prefix)
						.build();
					ResponseData<ChargeCalculateVo> resp = apiTemplate.execute(req);
					if (EnumResponseCode.COMMON_SUCCESS.getValue() == resp.getCode()) {
						Long fee = resp.getData().getFee();
						ChargingConfig config = resp.getData().getChargingConfig();
						log.info("[手续费]订单[{}]金额[{}]手续费[{}]", charging.getOrderNo(),
							charging.getAmount(), fee);
						walletOrderDao.updateFee(charging.getOrderNo(), config.getType(),
							config.getChargingValue(), fee);
					}

					log.info("[手续费]完成更新订单[{}]手续费", charging.getOrderNo());
				} catch (Exception e) {
					log.error("[手续费]更新失败", e);
				}
			});

		});

		return ParcelFactory.buildProducer(parcelConsumer, 1024);
	}
}
