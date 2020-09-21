package com.peng.leyou.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.peng.leyou.properties.IdWorkerProperties;
import com.peng.leyou.utils.IdWorker;

@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {
	
	@Bean
	public IdWorker idWorker(IdWorkerProperties idWorkerProperties) {
		return new IdWorker(idWorkerProperties.getWorkerId(), idWorkerProperties.getDatacenterId());
	}

}
