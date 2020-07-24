package com.rfchina.wallet.server;

import com.rfchina.scheduler.annotation.EnableFuScheduleTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.rfchina.wallet.server")
@EnableFuScheduleTask
@EnableAutoConfiguration
@EnableAsync
public class WalletServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletServerApplication.class);
	}
}
