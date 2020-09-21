package com.peng.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class LeyouUpload {
	public static void main(String[] args) {
		SpringApplication.run(LeyouUpload.class, args);
	}

}
