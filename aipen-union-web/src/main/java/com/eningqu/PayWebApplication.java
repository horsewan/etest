package com.eningqu;

import com.eningqu.common.db.DynamicDataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
@ServletComponentScan
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableScheduling
public class PayWebApplication {

	private final static Logger logger = LoggerFactory.getLogger(PayWebApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PayWebApplication.class, args);
		logger.info("=========Union服务启动完成=========");
	}
}

