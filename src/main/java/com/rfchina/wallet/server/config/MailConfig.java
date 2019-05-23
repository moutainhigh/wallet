package com.rfchina.wallet.server.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Bean(name = "javaMailSender")
	public JavaMailSenderImpl javaMailSender(
		@Value("${email.smtp}") String host,
		@Value("${email.port}") int port,
		@Value("${email.username}") String username,
		@Value("${email.password}") String password,
		@Value("${email.smtp.auth}") String auth,
		@Value("${email.smtp.timeout}") String timeout
	) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(host);
		sender.setPort(port);
		sender.setUsername(username);
		sender.setPassword(password);
		sender.setDefaultEncoding("utf-8");
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth",auth);
		properties.setProperty("mail.smtp.timeout",timeout);
		sender.setJavaMailProperties(properties);
		return sender;
	}
}
