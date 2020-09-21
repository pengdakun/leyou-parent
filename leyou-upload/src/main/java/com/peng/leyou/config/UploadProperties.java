package com.peng.leyou.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="leyou.upload")
public class UploadProperties {
	
	private String baseUrl;
	private List<String> allowTypes;
	
	

}
