package com.rfchina.wallet.server.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ConfigService {

	@Value(value = "${zipkin.service.enable}")
	private Boolean zipKinEnable;

	@Value(value = "${zipkin.collector.url}")
	private String zipkinUrl;

	@Value(value = "${zipkin.service.name}")
	private String zipkinName;

	@Value(value = "${app.base.url}")
	private String appBaseUrl;

	@Value(value = "${app.id}")
	private Long appId;

	@Value(value = "${app.secret}")
	private String appSecret;

	@Value(value = "${platform.api.secure.key}")
	private String platformApiSecureKey;

	@Value(value = "${wallet.web.home}")
	private String walletWebHome;

	@Value(value = "${wallet.verify.limit}")
	private Integer verifyLimitCount;

	@Value(value = "${sign.enable}")
	private boolean signEnable;

	// 银企直连
	@Value("${wlpay.pudong.signUrl}")
	private String signUrl;

	@Value("${wlpay.pudong.hostUrl}")
	private String hostUrl;

	@Value("${wlpay.pudong.masterid}")
	private String masterId;

	@Value("${wlpay.pudong.acctno}")
	private String acctNo;

	@Value("${wlpay.pudong.acctname}")
	private String acctName;

	@Value("${wlpay.pudong.acctAreaCode}")
	private String acctAreaCode;

	@Value("${wlpay.pudong.acctBankCode}")
	private String acctBankCode;

	@Value("${wlpay.pudong.auditMasterId}")
	private String auditMasterId;

	@Value("${wlpay.pudong.pkgSegment}")
	private Long pkgSegment;
	// 银企直连 END

	// 钱包支付
	@Value("${wlpay.paystatus.nextRoundSec}")
	private Integer nextRoundSec;

	@Value("${wlpay.quartz.updateSize}")
	private Integer batchUpdateSize;

	@Value("${wlpay.quartz.paySize}")
	private Integer batchPaySize;

	@Value("${wlpay.notify.devEmail}")
	private String notifyDevEmail;

	@Value("${wlpay.notify.bizEmail}")
	private String notifyBizEmail;

	@Value("${email.sender}")
	private String emailSender;

	@Value(value = "${active.env}")
	private String env;
	// 钱包支付 END

	// yunst start
	@Value("${yunst.serverUrl}")
	private String ystServerUrl;

	@Value("${yunst.sysId}")
	private String ystSysId;

	@Value("${yunst.pfxPath}")
	private String ystPfxPath;

	@Value("${yunst.password}")
	private String ystPassword;

	@Value("${yunst.alias}")
	private String ystAlias;

	@Value("${yunst.version}")
	private String ystVersion;

	@Value("${yunst.tlCertPath}")
	private String ystTlCertPath;

	@Value("${yunst.jumpUrl}")
	private String yunstResultJumpUrl;

	@Value("${yunst.jumpUrl.prefix}")
	private String yunstJumpUrlPrefix;

	@Value("${yunst.notify.backUrl}")
	private String yunstNotifybackUrl;

	@Value("${yunst.signContract.gateway.url}")
	private String yunstSignContractUrl;

	@Value("${yunst.balanceProtocol.name}")
	private String yunstBalanceProtocolName;

	@Value("${yunst.receiverId}")
	private String yunstReceiverId;

	@Value("${yunst.signBalaceProtocol.gateway.url}")
	private String yunstSignBalanceProtocolUrl;

	@Value("${yunst.setPayPassword.gateway.url}")
	private String yunstSetPayPasswordUrl;

	@Value("${yunst.personChangeBindPhone.gateway.url}")
	private String yunstPersonChangeBindPhoneUrl;

	@Value("${yunst.userAccSet:200126}")
	private String userAccSet;

	@Value("${yunst.entAccSet:100001}")
	private String entAccSet;

	@Value("${yunst.tradeCodeBuy:3001}")
	private String tradeCodeCollect;

	@Value("${yunst.tradeCodeBuy:4001}")
	private String tradeCodePay;

	@Value("${yunst.recall.prefix}")
	private String yunstRecallPrefix;

	@Value("${wallet.storage.dir}")
	private String storageDir;

// yunst end

}
