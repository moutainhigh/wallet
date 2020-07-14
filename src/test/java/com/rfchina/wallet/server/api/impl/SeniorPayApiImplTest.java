package com.rfchina.wallet.server.api.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.EnumDef.WalletType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletConfig;
import com.rfchina.wallet.server.SpringApiTest;
import com.rfchina.wallet.server.api.SeniorPayApi;
import com.rfchina.wallet.server.mapper.ext.WalletConfigExtDao;
import com.rfchina.wallet.server.model.ext.AgentPayReq;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.Reciever;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.DeductionReq;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.service.VerifyService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SeniorPayApiImplTest extends SpringApiTest {


	private Long payerWalletId = 10035L;
	private Long payeeWalletId = 10001L;
	private Long platWalletId = 10000L;
	private Long cardId = 12L;


	@Autowired
	private SeniorPayApi seniorPayApi;

	@Autowired
	private VerifyService verifyService;

	@Autowired
	private WalletConfigExtDao walletConfigDao;

	@Test
	public void withdrawLimit() {
		Long amount = 1L;
		// 判断最小手动提现金额
		Wallet wallet = verifyService.checkSeniorWallet(3L);
		WalletConfig config = walletConfigDao.selectUniCfg();
		if (config != null) {
			if (WalletType.COMPANY.getValue().byteValue() == wallet.getType()) {
				Optional.ofNullable(config.getManualWithdrawCompanyMin())
					.filter(c -> c.longValue() > amount)
					.ifPresent(c -> {
						throw new WalletResponseException(
							EnumWalletResponseCode.WALLET_WITHDRAW_NOT_ENOUGH,
							String.valueOf(c / 100));
					});
			} else if (WalletType.PERSON.getValue().byteValue() == wallet.getType()) {
				Optional.ofNullable(config.getManualWithdrawCompanyMin())
					.filter(c -> c.longValue() > amount)
					.ifPresent(c -> {
						throw new WalletResponseException(
							EnumWalletResponseCode.WALLET_WITHDRAW_NOT_ENOUGH,
							String.valueOf(c / 100));
					});
			}
		}
	}

	@Test
	public void collect() {
		CodePay codePay = CodePay.builder()
			.payType((byte) 41)
			.authcode("134841889691749460")
			.amount(3L)
			.build();

		Reciever reciever1 = Reciever.builder()
			.walletId(platWalletId)
			.amount(1L)
			.build();
		Reciever reciever2 = Reciever.builder()
			.walletId(payeeWalletId)
			.amount(2L)
			.build();
		CollectReq req = CollectReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(3L)
			.note("")
			.fee(0L)
			.validateType((byte) 0)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.recievers(Arrays.asList(reciever1, reciever2))
			.walletPayMethod(WalletPayMethod.builder().codePay(codePay).build())
			.build();

		WalletCollectResp resp = seniorPayApi.collect(super.accessToken, req, null, null, null);
		log.info("collect resp = {}", JsonUtil.toJSON(resp));
	}


	@Test
	public void agentPay() {
		String collectOrderNo = "TWC2020061897112306";

		AgentPayReq.Reciever reciever = new AgentPayReq.Reciever();
//		reciever.setWalletId(platWalletId);
//		reciever.setAmount(1L);
//		reciever.setFeeAmount(0L);
//		seniorPayApi.agentPay(super.accessToken, String.valueOf(System.currentTimeMillis()),
//			collectOrderNo, reciever, "", null);

		reciever.setWalletId(299L);
		reciever.setAmount(1L);
		reciever.setFeeAmount(0L);
		seniorPayApi.agentPay(super.accessToken, String.valueOf(System.currentTimeMillis()),
			collectOrderNo, reciever, "", null);
	}

	@Test
	public void collect2() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MINUTE, 30);
		Date expireTime = instance.getTime();
		String text = "{\"amount\":3,\"biz_no\":\"JUnitTest\",\"expire_time\":\"" + dateFormat
			.format(expireTime)
			+ "\",\"fee\":0,\"industry_code\":\"2422\",\"industry_name\":\"其他\",\"note\":\"JUnitTest\",\"recievers\":[{\"amount\":0,\"fee_amount\":0,\"wallet_id\":305,\"role_type\":1},{\"amount\":2,\"fee_amount\":0,\"wallet_id\":314,\"role_type\":4},{\"amount\":1,\"fee_amount\":0,\"wallet_id\":299,\"role_type\":4}],\"wallet_pay_method\":{\"pos\":{\"pay_type\":51,\"vsp_cusid\":\"56058104816U8U6\",\"amount\":3}},\"good_name\":\"JUnitTest\",\"good_desc\":\"JUnitTest\"}";
		CollectReq req = JsonUtil.toObject(text, CollectReq.class, (objectMapper) -> {
			objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
			objectMapper.setDateFormat(dateFormat);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		});
		WalletCollectResp resp = seniorPayApi.collect(super.accessToken, req, null, null, null);
		log.info("collect resp = {}", JsonUtil.toJSON(resp));
	}

	@Test
	public void deduction() {

		Balance balance = Balance.builder()
			.payerWalletId(payeeWalletId)
			.amount(1L)
			.build();
		DeductionReq req = DeductionReq.builder()
			.bizNo(String.valueOf(System.currentTimeMillis()))
			.amount(1L)
			.note("")
			.fee(0L)
			.expireTime(null)
			.industryCode("1010")
			.industryName("保险代理")
			.walletPayMethod(WalletPayMethod.builder().balance(balance).build())
			.build();
		WalletCollectResp resp = seniorPayApi.deduction(super.accessToken, req, null);
		log.info("Deduction resp = {}", JsonUtil.toJSON(resp));
	}

	@Test
	public void smsConfirm() {
		seniorPayApi.smsConfirm(super.accessToken, "021013e9-12a5-4267-9caa-e485d9972038", "669805",
			"192.168.197.28");
	}

	@Test
	public void checkBlankUrl() {
		String url = ((SeniorPayApiImpl) seniorPayApi)
			.checkBlankUrl("http://www.rfchina.com/index");
		log.info(url);
	}
}