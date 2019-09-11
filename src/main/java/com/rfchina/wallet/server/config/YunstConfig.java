package com.rfchina.wallet.server.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ToString
public class YunstConfig {

	//serverUrl=http://116.228.64.55:6900/service/soa
	//#serverUrl=https://fintech.allinpay.com/service/soa
	//# Application sysid:
	//sysId=1902271423530473681
	//path=E:/work/allinpay/1902271423530473681.pfx
	//# pfx files' passwd
	//pwd=123456
	//alias=1902271423530473681
	//
	//version=1.0
	//tlCertPath=E:/work/allinpay/TLCert-test.cer
	@Value("${yunst.serverUrl}")
	private String serverUrl;
	@Value("${yunst.sysId}")
	private String sysId;
	@Value("${yunst.pfxPath}")
	private String pfxPath;
	@Value("${yunst.password}")
	private String password;
	@Value("${yunst.alias}")
	private String alias;
	@Value("${yunst.version}")
	private String version;
	@Value("${yunst.tlCertPath}")
	private String tlCertPath;
}
