package com.eningqu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableScheduling
public class AdminWebApplication {

	private final static Logger logger = LoggerFactory.getLogger(AdminWebApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AdminWebApplication.class, args);
		logger.info("=========服务启动完成=========");
	}

}

