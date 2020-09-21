package com.peng.leyou.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("leyou.worker")
public class IdWorkerProperties {
	
	private Long workerId;
	private Long datacenterId;

}
