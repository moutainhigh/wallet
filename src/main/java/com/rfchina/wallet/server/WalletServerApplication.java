package com.rfchina.wallet.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages="com.rfchina.wallet.server")
@EnableAutoConfiguration
//@ImportResource("classpath:schedule-task.xml")
public class WalletServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletServerApplication.class);
	}
}
