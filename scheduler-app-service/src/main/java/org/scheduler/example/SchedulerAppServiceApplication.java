package org.scheduler.example;

import org.scheduler.example.configuration.PageProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(PageProperty.class)
public class SchedulerAppServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerAppServiceApplication.class, args);
	}

}
