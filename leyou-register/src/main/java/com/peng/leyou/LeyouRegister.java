package com.peng.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer//开启eureka服务端
@SpringBootApplication
public class LeyouRegister {
	
	public static void main(String[] args) {
		SpringApplication.run(LeyouRegister.class, args);
	}
	

}
