package com.rfchina.wallet.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.rfchina.wallet.server")
@EnableAutoConfiguration
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class);
	}
}
